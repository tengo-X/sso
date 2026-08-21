package com.tengo.client.config;

import com.tengo.client.filter.TengoSsoTokenFilter;
import com.tengo.client.service.TengoClientTokenManager;
import com.tengo.core.config.ClientSsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * 客户端 HTTP 配置
 */
@EnableConfigurationProperties(ClientSsoProperties.class)
@ConditionalOnClass(WebClient.class)
@ConditionalOnWebApplication(type = SERVLET)
@ConditionalOnProperty(prefix = "client.tengo.sso", name = "server-url")
@AutoConfiguration
public class TengoSsoAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "client.tengo.sso", name = "server-url")
    public WebClient webClient(ClientSsoProperties clientSsoProperties) {
        return WebClient.builder()
                .baseUrl(clientSsoProperties.getServerUrl())
                .build();
    }

    @Bean
    @ConditionalOnBean(WebClient.class)
    public TengoClientTokenManager clientTokenManager(WebClient webClient) {

        return new TengoClientTokenManager(webClient);
    }

    @Bean
    @ConditionalOnBean(TengoClientTokenManager.class)
    public TengoSsoTokenFilter tengoSsoTokenFilter(TengoClientTokenManager tengoClientTokenManager) {

        return new TengoSsoTokenFilter(tengoClientTokenManager);
    }

    @Bean
    @ConditionalOnBean(TengoSsoTokenFilter.class)
    public FilterRegistrationBean<TengoSsoTokenFilter> tengoSsoTokenFilterRegistration(TengoSsoTokenFilter tengoSsoTokenFilter) {
        FilterRegistrationBean<TengoSsoTokenFilter> registration = new FilterRegistrationBean<>(tengoSsoTokenFilter);
        registration.addUrlPatterns("/**");
        registration.setOrder(1);
        registration.setName("tengoSsoTokenFilter");
        return registration;
    }
}
