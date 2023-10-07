package io.github.xw.lock.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.github.xw.lock.annotation.DistributedLock;
import io.github.xw.lock.service.DistributedLockService;
import io.github.xw.lock.util.SpelUtil;

/**
 * @author xw
 */
@Aspect
@Component
public class DistributedLockAspect {

    Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

    @Autowired
    private DistributedLockService distributedLockService;


    // 配置织入点
    @Pointcut("@annotation(io.github.xw.lock.annotation.DistributedLock)")
    public void lockPointCut() {

    }

    @Around("lockPointCut()")
    public Object doInvoke(ProceedingJoinPoint point) throws Exception {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        DistributedLock redisLock = targetMethod.getAnnotation(DistributedLock.class);

        String lockKey = SpelUtil.generateKeyBySpEL(redisLock.lockKey(),point);
        if (StringUtils.isEmpty(lockKey)) {
            lockKey = point.getTarget().getClass().getName() + "." + targetMethod.getName();
        }
        String value = redisLock.value();
        if (StringUtils.isEmpty(value)) {
            value = String.valueOf(Thread.currentThread().getId());
        }
        Boolean locked = distributedLockService.tryLock(lockKey, value, redisLock.durationWithSecond());
        if (!locked) {
            logger.warn("get lock failed,class[{}],method[{}],lockKey[{}]", point.getTarget().getClass().getName(),
                targetMethod.getName(), lockKey);
            if (redisLock.throwExceptionWhenLockFailed()) {
                throw new RuntimeException("get lock failed,lock key is " + lockKey);
            }
        }
        try {
            return point.proceed();
        } catch (Exception e) {
            logger.error("execute locked method [{}] occurrence an exception", targetMethod.getName(), e);
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            Boolean releaseLock = distributedLockService.releaseLock(lockKey, value);
            if (!releaseLock) {
                logger.debug("release lock failed,class[{}],method[{}],lockKey[{}]",
                    point.getTarget().getClass().getName(), targetMethod.getName(), lockKey);
            }
        }
    }

}
