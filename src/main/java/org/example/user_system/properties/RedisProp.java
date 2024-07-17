package org.example.user_system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisProp {
    /**
     * 是否启用redis
     */
    private Boolean enabled;
}
