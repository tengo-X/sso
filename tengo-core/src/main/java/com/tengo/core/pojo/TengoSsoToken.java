package com.tengo.core.pojo;

import com.tengo.core.config.KeyConf;

import java.io.Serializable;
import java.util.UUID;

/**
 * Token 信息载体
 */
public class TengoSsoToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String username;

    /**
     * 会话唯一标识，用于用户维度的会话索引和踢人
     */
    private String sessionId;

    // 最终过期时间
    private long expired;

    public TengoSsoToken() {
    }

    public TengoSsoToken(String userId) {
        this.userId = userId;
    }

    public TengoSsoToken(String userId, String username, long expire) {
        this.userId = userId;
        this.username = username;
        this.sessionId = userId + KeyConf.SEPARATOR + UUID.randomUUID().toString().replaceAll("-", "");
        this.expired = expire + System.currentTimeMillis();
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }
}
