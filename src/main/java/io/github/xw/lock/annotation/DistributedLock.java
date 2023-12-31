package io.github.xw.lock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xw
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {


    /**
     * 锁KEY
     * @return
     */
    String lockKey();


    /**
     * 值
     * @return
     */
    String value() ;


    /**
     * 单位：秒
     * 锁定时间,默认1分钟
     * @return
     */
    long durationWithSecond() default 60;


    /**
     * 当加锁失败是够抛出异常,默认不抛出异常
     * @return
     */
    boolean throwExceptionWhenLockFailed() default false;
}
