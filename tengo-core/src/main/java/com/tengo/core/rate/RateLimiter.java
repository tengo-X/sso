package com.tengo.core.rate;

/**
 * 限流接口
 */
public interface RateLimiter {

    /**
     * 尝试获取许可，返回 true 表示允许通过，false 表示触发限流
     */
    boolean tryAcquire(String key);
}
