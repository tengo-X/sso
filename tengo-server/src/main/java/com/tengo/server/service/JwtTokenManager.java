package com.tengo.server.service;

import com.tengo.core.cache.TokenCache;
import com.tengo.core.config.KeyConf;
import com.tengo.core.config.ServerSsoProperties;
import com.tengo.core.pojo.TengoSsoToken;
import com.tengo.core.xi.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式 JWT Token 管理器，基于 Redis 实现多节点共享黑名单
 *
 * 两层 key 设计：
 * 1. Session 存储: TENGO_SSO_SESSION:{uuid} → TengoSsoToken(userId, username, sessionId)
 * 2. 用户索引:     TENGO_SSO_USER:{userId}:{sessionId} → TENGO_SSO_SESSION:{uuid}
 *
 * 好处：
 * - key 不再暴露 userId，避免信息泄露
 * - 支持用户维度的会话管理（查活跃会话、踢人等）
 * - sessionId 自动由 TengoSsoToken 构造时生成
 */
@Component
public class JwtTokenManager implements TokenManager {

    @Autowired
    private ServerSsoProperties ssoProperties;

    @Autowired
    private TokenCache tokenCache;

    @Override
    public String createToken(String userId, String username) {

        long tokenExpire = ssoProperties.getTokenExpire();

        // 构建会话对象，sessionId 在构造时自动生成
        TengoSsoToken ssoToken = new TengoSsoToken(userId, username, tokenExpire);

        // Layer 1: 存储会话数据
        String sessionKey = KeyConf.PREFIX + userId;
        tokenCache.put(sessionKey, ssoToken, tokenExpire, TimeUnit.MILLISECONDS);

        return ssoToken.getSessionId();
    }

    @Override
    public TengoSsoToken verifyToken(String token) {

        String userId = ofUserIdByToken(token);

        String sessionKey = KeyConf.PREFIX + userId;

        Object ssoTokenObj = tokenCache.get(sessionKey);
        if (Objects.isNull(ssoTokenObj)) {
            return null;
        }
        TengoSsoToken ssoToken = (TengoSsoToken) ssoTokenObj;
        if (!token.equals(ssoToken.getSessionId())) {
            return null;
        }
        long refreshTokenExpire = ssoProperties.getRefreshTokenExpire();
        long expired = ssoToken.getExpired();
        long finalEx = expired + refreshTokenExpire;

        tokenCache.expire(sessionKey, finalEx, TimeUnit.MILLISECONDS);
        return ssoToken;
    }

    @Override
    public void removeToken(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }
        try {

            String userId = ofUserIdByToken(token);

            // 读取会话对象以获取 sessionId，然后删除用户索引
            String sessionKey = KeyConf.PREFIX + userId;

            tokenCache.delete(sessionKey);
        } catch (Exception e) {
            // ignore
        }
    }

    private static String ofUserIdByToken(String token) {
        if (Objects.isNull(token) || token.trim().length() <= 0 || !token.contains(KeyConf.SEPARATOR)) {
            return null;
        }

        String[] split = token.split(KeyConf.SEPARATOR);
        if (split.length != 2) {
            return null;
        }
        String userId = split[0];
        return userId.trim();
    }
}
