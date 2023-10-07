package io.github.xw.lock.service.impl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import io.github.xw.lock.service.DistributedLockService;

/**
 * @author xw
 */
@Service
public class RedisTemplateDistributedLockService implements DistributedLockService {

    private final StringRedisTemplate redisTemplate;

    public RedisTemplateDistributedLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final String RELEASE_SCRIPT =
        "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    public Boolean tryLock(String key, String value, Long seconds) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS);
    }

    public Boolean releaseLock(String key, String value) {
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(RELEASE_SCRIPT, Long.class),
            Arrays.asList(key), value);
        return result == 1;
    }
}
