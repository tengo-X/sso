package com.tengo.server.handler;

import com.tengo.core.R;
import com.tengo.core.exception.TengoSsoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TengoSsoException.class)
    public R<Void> handleTengoSsoException(TengoSsoException e) {
        log.warn("SSO安全异常: {}", e.getMessage());
        // 安全加固：不向客户端暴露内部错误详情
        return new R<>(401, "认证失败", null);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.FAIL;
    }
}
