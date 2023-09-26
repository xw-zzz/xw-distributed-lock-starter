package com.xw.lock.service.impl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import com.xw.lock.service.DistributedLockService;

/**
 * @author yangliu@tiduyun.com
 * @date 2023/9/26
 */
public class RedisClientDistributedLockService implements DistributedLockService {

    private RedissonClient redissonClient;

    public RedisClientDistributedLockService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public Boolean tryLock(String key, String value, Long seconds) {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean locked = lock.tryLock(seconds, TimeUnit.SECONDS);
            return locked;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean releaseLock(String key, String value) {
        RLock lock = redissonClient.getLock(key);
        if (null != lock && lock.isHeldByCurrentThread()) {
            lock.unlock();
            return true;
        } else {
            return false;
        }
    }
}
