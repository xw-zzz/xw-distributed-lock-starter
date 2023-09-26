package com.xw.lock.service.impl;

import com.xw.lock.service.DistributedLockService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author yangliu@tiduyun.com
 * @date 2023/9/26
 */
public class RedisTemplateDistributedLockService implements DistributedLockService {

    private StringRedisTemplate redisTemplate;

    public RedisTemplateDistributedLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final String RELEASE_SCRIPT =
        "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    public Boolean tryLock(String key, String value, Long seconds) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS);
    }

    public Boolean releaseLock(String key, String value) {
        Integer result = redisTemplate.execute(new DefaultRedisScript<Integer>(RELEASE_SCRIPT, Integer.class),
            Arrays.asList(key), value);
        return result == 1;
    }
}
