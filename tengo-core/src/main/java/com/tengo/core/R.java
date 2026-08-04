package com.tengo.core;

import java.io.Serializable;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public class R<T> implements Serializable {

    private boolean isSuccess;
    private int code;
    private String message;
    private T data;

    public static final R FAIL = new R<>(500,null);
    public static final R SUCCESS = new R<>(200,null);
    public static final R NOT_LOGIN = new R<>(401,null);

    public R() {}

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.isSuccess = (code == 200);
    }

    public R(int code, T data) {
        this.code = code;
        this.data = data;
        this.isSuccess = (code == 200);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
