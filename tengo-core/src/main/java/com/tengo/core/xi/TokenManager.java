package com.tengo.core.xi;

import com.tengo.core.pojo.TokenInfo;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public interface TokenManager extends VerifyTokenManager {

    /**
     * 创建Token
     * @return Token字符串
     */
    String createToken(String userId);

    /**
     * 创建refreshToken
     *
     * @param userId
     * @return
     */
    String createRefreshToken(String userId);

    /**
     * 刷新Token
     * @param token 原Token
     * @return 新Token
     */
    String refreshToken(String token);

    /**
     * 删除Token
     * @param token Token字符串
     */
    void removeToken(String token);
}
