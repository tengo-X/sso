package com.tego.sso.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dengxiao
 * @date 2025-12-15
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * 方案1: BCryptPasswordEncoder (推荐)
     * 强度: 4-31, 默认10
     */
    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        int strength = 10; // 强度因子，值越大加密越慢但更安全
        return new BCryptPasswordEncoder(strength);
    }

    /**
     * 方案2: DelegatingPasswordEncoder (支持多种算法)
     */
    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("argon2", new Argon2PasswordEncoder());

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    /**
     * 方案3: 工厂方法创建 DelegatingPasswordEncoder
     */
    @Bean
    public PasswordEncoder defaultPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // 默认使用 bcrypt 算法
        // 密码格式: {bcrypt}encodedPassword
    }

    /**
     * 方案4: Pbkdf2PasswordEncoder
     */
    @Bean
    public PasswordEncoder pbkdf2PasswordEncoder() {
        String secret = "my-secret-key"; // 可选密钥
        int iterations = 185000;        // 迭代次数
        int hashWidth = 256;            // 哈希宽度

        return new Pbkdf2PasswordEncoder(secret, iterations, hashWidth);
    }

    /**
     * 方案5: 无密码编码器（不推荐，仅用于测试）
     */
//    @Bean
//    public PasswordEncoder noOpPasswordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // 明文存储，不安全！
//    }
}
