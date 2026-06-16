package com.tengo.service;

import com.tengo.core.pojo.TokenInfo;
import com.tengo.core.xi.VerifyTokenManager;
import org.springframework.stereotype.Service;

/**
 * @author dx
 * @date 2023/3/14
 */
@Service
public class ClientTokenManager implements VerifyTokenManager {

    @Override
    public TokenInfo verifyToken(String token,String signKey) {
        return null;
    }

}
