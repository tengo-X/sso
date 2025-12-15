package com.tego.sso.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@ConfigurationProperties(prefix = "tengo.sso")
@Configuration
public class SsoProperties {

    /**
     * 是否启用SSO
     */
    private boolean enabled = true;

    /**
     * Token过期时间(分钟)
     */
    private int tokenExpire = 30;

    /**
     * 刷新Token过期时间(小时)
     */
    private int refreshTokenExpire = 24 * 7;

    /**
     * Token签名算法
     */
    private String algorithm = "RS256";

    /**
     * 私钥路径
     */
    private String privateKeyPath;

    /**
     * 公钥路径
     */
    private String publicKeyPath;

    /**
     * SSO服务端地址
     */
    private String serverUrl;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    // getters and setters...
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public int getTokenExpire() { return tokenExpire; }
    public void setTokenExpire(int tokenExpire) { this.tokenExpire = tokenExpire; }

    public int getRefreshTokenExpire() { return refreshTokenExpire; }
    public void setRefreshTokenExpire(int refreshTokenExpire) { this.refreshTokenExpire = refreshTokenExpire; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getPrivateKeyPath() { return privateKeyPath; }
    public void setPrivateKeyPath(String privateKeyPath) { this.privateKeyPath = privateKeyPath; }

    public String getPublicKeyPath() { return publicKeyPath; }
    public void setPublicKeyPath(String publicKeyPath) { this.publicKeyPath = publicKeyPath; }

    public String getServerUrl() { return serverUrl; }
    public void setServerUrl(String serverUrl) { this.serverUrl = serverUrl; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
}
