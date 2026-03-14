package com.tengo.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@ConfigurationProperties(prefix = "tengo.sso")
@Configuration
public class ServerSsoProperties {

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

    private String xKey = UUID.randomUUID().toString().replace("-","");

    /**
     * Token签名算法
     */
    private String algorithm = "RS256";

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public int getTokenExpire() { return tokenExpire; }
    public void setTokenExpire(int tokenExpire) { this.tokenExpire = tokenExpire; }

    public int getRefreshTokenExpire() { return refreshTokenExpire; }
    public void setRefreshTokenExpire(int refreshTokenExpire) { this.refreshTokenExpire = refreshTokenExpire; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getxKey() {
        return xKey;
    }

    public void setxKey(String xKey) {
        this.xKey = xKey;
    }
}
