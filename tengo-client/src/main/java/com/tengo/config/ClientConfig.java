package com.tengo.config;

import com.tengo.core.config.ClientSsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 客户端 HTTP 配置
 */
@Configuration
public class ClientConfig {

    @Autowired
    private ClientSsoProperties clientSsoProperties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(clientSsoProperties.getServerUrl())
                .build();
    }
}
