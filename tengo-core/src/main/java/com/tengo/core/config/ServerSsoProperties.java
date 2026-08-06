package com.tengo.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SSO 服务端配置属性
 * 安全加固：密钥必须通过环境变量覆盖，禁止使用硬编码的弱密钥
 */
@ConfigurationProperties(prefix = "tengo.sso")
@Configuration
public class ServerSsoProperties {

    /**
     * SSO TOKEN 有效时间 默认是30分钟 单位：秒
     */
    private long tokenExpire = 30 * 60;

    /**
     * SSO TOKEN 默认续命一天 单位：秒
     */
    private long refreshTokenExpire = 24 * 60 * 60;

    private String xKey;

    private String rKey;

    private String algorithm = "HS256";

    private String serverUrl = "http://localhost:8080/tengo-sso-server";

    public long getTokenExpire() { return tokenExpire; }
    public void setTokenExpire(long tokenExpire) { this.tokenExpire = tokenExpire * 1000; }

    public long getRefreshTokenExpire() { return refreshTokenExpire; }
    public void setRefreshTokenExpire(long refreshTokenExpire) { this.refreshTokenExpire = refreshTokenExpire * 1000; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getXKey() {
        return xKey;
    }

    public void setXKey(String xKey) {
        this.xKey = xKey;
    }

    public String getRKey() {
        return rKey;
    }

    public void setRKey(String rKey) {
        this.rKey = rKey;
    }
}
