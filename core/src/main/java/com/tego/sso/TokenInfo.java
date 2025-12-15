package com.tego.sso;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public class TokenInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 签发时间
     */
    private Date issuedAt;

    /**
     * 过期时间
     */
    private Date expiration;

    /**
     * 扩展属性
     */
    private Map<String, Object> claims;

    // getters and setters...
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Date getIssuedAt() { return issuedAt; }
    public void setIssuedAt(Date issuedAt) { this.issuedAt = issuedAt; }

    public Date getExpiration() { return expiration; }
    public void setExpiration(Date expiration) { this.expiration = expiration; }

    public Map<String, Object> getClaims() { return claims; }
    public void setClaims(Map<String, Object> claims) { this.claims = claims; }
}
