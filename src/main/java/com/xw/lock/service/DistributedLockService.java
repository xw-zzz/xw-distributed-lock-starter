package com.xw.lock.service;

/**
 * @author yangliu@tiduyun.com
 * @date 2023/9/26
 */
public interface DistributedLockService {

    /**
     * 尝试加锁
     * @param key
     * @param seconds
     * @return
     */
    Boolean tryLock(String key,String value,Long seconds);


    /**
     *
     * 释放锁
     *
     * @param key
     * @param value
     * @return
     */
    Boolean releaseLock(String key,String value);

}
