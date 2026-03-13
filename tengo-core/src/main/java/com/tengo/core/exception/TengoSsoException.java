package com.tengo.core.exception;

/**
 * @author dx
 * @date 2023/3/7
 */
public class TengoSsoException extends RuntimeException{

    public TengoSsoException(String msg) {
        super(msg);
    }

    public TengoSsoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TengoSsoException(Throwable cause) {
        super(cause);
    }
}
