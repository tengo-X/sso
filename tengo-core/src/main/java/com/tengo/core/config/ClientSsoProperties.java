package com.tengo.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 客户端 SSO 配置属性
 */
@ConfigurationProperties(prefix = "client.tengo.sso")
@Configuration
public class ClientSsoProperties {

    /**
     * SSO 认证中心服务地址
     */
    private String serverUrl;

    /**
     * SSO 签名密钥（用于本地 JWT 验签，优先级低于环境变量 TENGOSSO_XKEY）
     */
    private String signingKey;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }
}
