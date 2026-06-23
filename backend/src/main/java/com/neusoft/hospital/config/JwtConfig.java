package com.neusoft.hospital.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT（JSON Web Token）配置类
 * <p>
 * 从 application.yml 中读取以 "jwt" 为前缀的配置项，
 * 用于配置JWT签名的密钥和令牌有效期。
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    /** JWT签名密钥，用于生成和验证Token的签名 */
    private String secret;
    /** Token过期时间（毫秒），超过此时间Token将失效 */
    private long expiration;
}
