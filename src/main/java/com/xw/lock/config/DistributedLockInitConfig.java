package com.xw.lock.config;

import com.xw.lock.service.DistributedLockService;
import com.xw.lock.service.impl.RedisClientDistributedLockService;
import com.xw.lock.service.impl.RedisTemplateDistributedLockService;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author yangliu@tiduyun.com
 * @date 2023/9/26
 */

public class DistributedLockInitConfig {

    @ConditionalOnMissingBean(DistributedLockService.class)
    @ConditionalOnBean(StringRedisTemplate.class)
    public DistributedLockService redisTemplateDistributedLockService(StringRedisTemplate stringRedisTemplate) {
        return new RedisTemplateDistributedLockService(stringRedisTemplate);
    }


    @ConditionalOnMissingBean(DistributedLockService.class)
    @ConditionalOnBean(StringRedisTemplate.class)
    public DistributedLockService redisClientDistributedLockService(RedissonClient redissonClient) {
        return new RedisClientDistributedLockService(redissonClient);
    }


}
