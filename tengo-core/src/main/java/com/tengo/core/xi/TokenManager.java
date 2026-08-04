package com.tengo.core.xi;

import com.tengo.core.pojo.TengoSsoToken;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public interface TokenManager {

    /**
     * 创建Token
     * @return Token字符串
     */
    String createToken(String userId, String username);

    /**
     * 删除Token
     * @param token Token字符串
     */
    void removeToken(String token);

    TengoSsoToken verifyToken(String token);
}
