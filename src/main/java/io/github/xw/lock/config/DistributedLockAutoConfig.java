package io.github.xw.lock.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xw
 */
@Configuration
@EnableConfigurationProperties(DistributedConfig.class)
@ComponentScan(basePackages = "io.github.xw.lock")
@ConditionalOnProperty(value = "distributed-lock.enabled",havingValue = "true",matchIfMissing = true)
public class DistributedLockAutoConfig {

}
