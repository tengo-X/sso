package com.tengo.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author dx
 * @date 2023/3/14
 */
@ConfigurationProperties(prefix = "client.tengo.sso")
@Configuration
public class ClientSsoProperties implements Serializable {

    private String serverUrl;

    private String clientId;

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
