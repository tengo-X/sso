package com.tego.sso;

import java.io.Serializable;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public class AuthenticationResult implements Serializable {

    private boolean success;
    private String message;
    private UserInfo userInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
