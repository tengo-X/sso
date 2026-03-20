package com.tengo.core.xi;

import com.tengo.core.pojo.TokenInfo;
import com.tengo.core.pojo.User;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public interface TokenManager {

    /**
     * 创建Token
     * @return Token字符串
     */
    String createToken(String userId);

    /**
     * 验证Token
     * @param token Token字符串
     * @return Token信息
     */
    TokenInfo verifyToken(String token);

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
