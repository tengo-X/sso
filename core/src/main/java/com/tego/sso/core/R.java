package com.tego.sso.core;

import com.tego.sso.core.pojo.AuthUser;

import java.io.Serializable;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public class R implements Serializable {

    private boolean success;
    private String message;
    private AuthUser authUser;

    public R() {
    }

    public R(boolean success, String message, AuthUser authUser) {
        this.success = success;
        this.message = message;
        this.authUser = authUser;
    }

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

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }
}
