package com.xw.lock.aspect;

import com.xw.lock.service.DistributedLockService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author yangliu@tiduyun.com
 * @date 2023/9/26
 */
@Aspect
public class DistributedLockAspect {

    Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

    private DistributedLockService distributedLockService;

    // 配置织入点
    @Pointcut("@annotation(com.xw.lock.annotation.DistributedLock)")
    public void lockPointCut() {

    }

    @Around("lockPointCut()")
    public Object doInvoke(ProceedingJoinPoint point) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        com.xw.lock.annotation.DistributedLock redisLock =
            targetMethod.getAnnotation(com.xw.lock.annotation.DistributedLock.class);

        String lockKey = redisLock.lockKey();
        if (StringUtils.isEmpty(lockKey)) {
            lockKey = point.getTarget().getClass().getName() + "." + targetMethod.getName();
        }
        String value = redisLock.value();
        if (StringUtils.isEmpty(value)) {
            value = String.valueOf(Thread.currentThread().getId());
        }
        Boolean locked = distributedLockService.tryLock(lockKey, value, redisLock.durationWithSecond());
        if (!locked) {
            logger.info("lock failed,class[{}],method[{}],lockKey[{}]", point.getTarget().getClass().getName(),
                targetMethod.getName(), lockKey);
            if (redisLock.throwExceptionWhenLockFailed()) {
                throw new RuntimeException("lock failed,lock key is " + lockKey);
            }
        }
        try {
            return point.proceed();
        } catch (Exception e) {
            logger.error("execute locked method occurrence an exception", e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            distributedLockService.releaseLock(lockKey, value);
        }
        return null;
    }

}
