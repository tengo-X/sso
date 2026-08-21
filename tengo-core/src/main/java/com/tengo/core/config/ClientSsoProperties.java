package com.tengo.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * 客户端 SSO 配置属性
 */
@ConfigurationProperties(prefix = "client.tengo.sso")
public class ClientSsoProperties {

    /**
     * SSO 认证中心服务地址
     */
    private String serverUrl;

    /**
     * SSO clientId
     */
    private String clientId = UUID.randomUUID().toString().replaceAll("-","");

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
