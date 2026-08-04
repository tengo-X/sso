package com.tengo.core.cache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 分布式 Token 缓存接口，支持多节点部署
 */
public interface TokenCache {

    void put(String key, Serializable value, long expire, TimeUnit unit);

    Object get(String key);

    void delete(String key);

    boolean hasKey(String key);

    //续命
    void expire(String key, long expire, TimeUnit unit);
}
