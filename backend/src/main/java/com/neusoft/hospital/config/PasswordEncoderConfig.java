package com.neusoft.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码编码器配置类
 * <p>
 * 将BCryptPasswordEncoder注册为Spring Bean，用于用户密码的加密和验证。
 * BCrypt是一种适合密码存储的哈希算法，自动加入随机盐值以抵御彩虹表攻击。
 * </p>
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * 创建BCrypt密码编码器Bean
     *
     * @return BCryptPasswordEncoder实例
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
