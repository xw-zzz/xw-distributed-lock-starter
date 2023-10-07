package io.github.xw.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xw
 */

@ConfigurationProperties(prefix = "distributed-lock")
public class DistributedConfig {

    private Boolean enabled = true;


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
