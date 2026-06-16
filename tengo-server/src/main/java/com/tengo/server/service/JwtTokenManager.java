package com.tengo.server.service;

import com.tengo.core.config.KeyConf;
import com.tengo.core.config.ServerSsoProperties;
import com.tengo.core.exception.TengoSsoException;
import com.tengo.core.pojo.User;
import com.tengo.core.xi.TokenManager;
import com.tengo.core.pojo.TokenInfo;
import com.tengo.server.util.JWTUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.JwtSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@Component
public class JwtTokenManager implements TokenManager {

    @Autowired
    private ServerSsoProperties ssoProperties;

    @Override
    public String createToken(String userId) {

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        String xKey = ssoProperties.getxKey();
        long tokenExpire = ssoProperties.getTokenExpire();
        SignatureAlgorithm algorithm = SignatureAlgorithm.valueOf(ssoProperties.getAlgorithm());

        String accessToken = JWTUtil.genToken(xKey,tokenExpire,algorithm,userId,uuid);
        return accessToken;
    }

    @Override
    public String createRefreshToken(String userId) {

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        String rKey = ssoProperties.getrKey();
        long tokenExpire = ssoProperties.getRefreshTokenExpire();
        SignatureAlgorithm algorithm = SignatureAlgorithm.valueOf(ssoProperties.getAlgorithm());

        return JWTUtil.genToken(rKey,tokenExpire,algorithm,userId,uuid);
    }

    @Override
    public TokenInfo verifyToken(String token,String signKey) {
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(getSignKey(signKey))
                    .parseClaimsJws(token)
                    .getBody();

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserId(claims.getSubject());
            tokenInfo.setIssuedAt(claims.getIssuedAt());
            tokenInfo.setExpiration(claims.getExpiration());

            return tokenInfo;
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            String jti = claims.getId();
            String userId = claims.getSubject();
            //放入黑名单

            throw new TengoSsoException("Token已过期", e);
        } catch (Exception e) {
            throw new TengoSsoException("Token验证失败", e);
        }
    }

    @Override
    public String refreshToken(String refreshToken) {

        //解析刷新token

        TokenInfo tokenInfo = verifyToken(refreshToken,KeyConf.RT);
        if (Objects.isNull(tokenInfo)) {
            //非法刷新token 将刷新token 加入黑名单
            throw new TengoSsoException("非法刷新token");
        }

        return createToken(tokenInfo.getUserId());
    }

    @Override
    public void removeToken(String token) {
        // 服务端可以维护一个黑名单，这里简单实现
        // 生产环境应该使用Redis等存储黑名单
    }

    private String getSignKey(String signKey) {

        return Objects.equals(KeyConf.AT,signKey) ? ssoProperties.getxKey() : ssoProperties.getrKey();
    }
}
