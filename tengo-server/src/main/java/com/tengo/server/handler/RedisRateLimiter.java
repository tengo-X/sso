package com.tengo.server.handler;

import com.tengo.core.rate.RateLimiter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 滑动窗口的限流器
 * 使用 Redis INCR + EXPIRE 实现简单计数器，适合高并发场景
 */
@Component
public class RedisRateLimiter implements RateLimiter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String RATE_PREFIX = "RATE:";

    /**
     * 默认窗口大小（秒）
     */
    private final long windowSeconds;

    /**
     * 默认窗口内最大请求数
     */
    private final int maxRequests;

    public RedisRateLimiter() {
        this.windowSeconds = 60;
        this.maxRequests = 30;
    }

    public RedisRateLimiter(long windowSeconds, int maxRequests) {
        this.windowSeconds = windowSeconds;
        this.maxRequests = maxRequests;
    }

    @Override
    public boolean tryAcquire(String key) {
        String redisKey = RATE_PREFIX + key;
        Long count = redisTemplate.opsForValue().increment(redisKey);
        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, windowSeconds, TimeUnit.SECONDS);
        }
        return count != null && count <= maxRequests;
    }
}
