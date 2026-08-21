package com.tengo.server.handler;

import com.tengo.core.cache.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的分布式 Token 缓存实现
 */
@Component
public class RedisTokenCache implements TokenCache {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void put(String key, Serializable value, long expire, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, expire, unit);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void expire(String key, long expire, TimeUnit unit) {
        redisTemplate.expire(key, expire, unit);
    }
}
