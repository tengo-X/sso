package com.tego.sso.server.autoconfigure;

import com.tego.sso.core.SsoProperties;
import com.tego.sso.server.service.KeyPairService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@Configuration
@EnableConfigurationProperties(SsoProperties.class)
public class SsoServerAutoConfiguration {

    @Bean
    public KeyPairService keyPairService() {
        return new KeyPairService();
    }
}
