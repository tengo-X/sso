package com.tengo.core.xi;

import com.tengo.core.pojo.TokenInfo;

/**
 * @author dx
 * @date 2026/4/21
 */
public interface VerifyTokenManager {

    /**
     * 验证Token
     * @param token Token字符串
     * @param signKey
     * @return Token信息
     */
    TokenInfo verifyToken(String token,String signKey);
}
