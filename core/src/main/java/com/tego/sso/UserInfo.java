package com.tego.sso;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public class UserInfo implements Serializable {

    private String userId;
    private String username;
    private String email;
    private String phone;
    private Map<String, Object> attributes;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
