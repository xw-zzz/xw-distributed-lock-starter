package io.github.xw.lock.service;

/**
 * @author xw
 */
public interface DistributedLockService {

    /**
     * 尝试加锁
     * @param key key
     * @param seconds 加锁秒数
     * @return true加锁成功，false加锁失败
     */
    Boolean tryLock(String key,String value,Long seconds);


    /**
     *
     * 释放锁
     *
     * @param key 加锁key
     * @param value value
     * @return true释放锁成功，false释放锁失败
     */
    Boolean releaseLock(String key,String value);

}
