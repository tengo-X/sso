package com.tengo.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * SSO 服务端配置属性
 * 安全加固：密钥必须通过环境变量覆盖，禁止使用硬编码的弱密钥
 */
@ConfigurationProperties(prefix = "tengo.sso")
@Configuration
public class ServerSsoProperties {

    //默认是30分钟 单位：秒
    private long tokenExpire = 30 * 60;

    //默认续命一天 单位：秒
    private long refreshTokenExpire = 24 * 60 * 60;

    private String xKey;

    private String rKey;

    private String algorithm = "HS256";

    private String serverUrl = "http://localhost:8080/tengo-sso-server";

    public long getTokenExpire() { return tokenExpire * 1000; }
    public void setTokenExpire(long tokenExpire) { this.tokenExpire = tokenExpire; }

    public long getRefreshTokenExpire() { return refreshTokenExpire * 1000; }
    public void setRefreshTokenExpire(long refreshTokenExpire) { this.refreshTokenExpire = refreshTokenExpire; }

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
