package org.example.user_system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    /**
     * 生成jwt令牌相关配置
     */
    private String secretKey;
    /**
     * 过期时间（单位：秒）
     */
    private long ttl;
    /**
     * 设置前端传递过来的令牌名称
     */
    private String userTokenName;

}
