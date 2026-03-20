package com.tengo.service;

import com.tengo.core.pojo.TokenInfo;
import com.tengo.core.pojo.User;
import com.tengo.core.xi.TokenManager;
import org.springframework.stereotype.Service;

/**
 * @author dx
 * @date 2023/3/14
 */
@Service
public class ClientTokenManager implements TokenManager {
    @Override
    public String createToken(String userId) {
        return null;
    }

    @Override
    public TokenInfo verifyToken(String token) {
        return null;
    }

    @Override
    public String refreshToken(String token) {
        return null;
    }

    @Override
    public void removeToken(String token) {

    }
}
