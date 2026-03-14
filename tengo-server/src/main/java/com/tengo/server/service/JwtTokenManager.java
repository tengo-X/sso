package com.tengo.server.service;

import com.tengo.core.config.ServerSsoProperties;
import com.tengo.core.exception.TengoSsoException;
import com.tengo.core.xi.TokenManager;
import com.tengo.core.pojo.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@Component
public class JwtTokenManager implements TokenManager {
    @Autowired
    private ServerSsoProperties ssoProperties;

    @Override
    public String createToken(TokenInfo tokenInfo) {

        return Jwts.builder()
                .setSubject(tokenInfo.getUserId())
                .signWith(SignatureAlgorithm.HS256,ssoProperties.getxKey())
                .claim("X-ID", tokenInfo.getUserId())
                .setIssuedAt(tokenInfo.getIssuedAt())
                .setExpiration(tokenInfo.getExpiration())
                .compact();
    }

    @Override
    public TokenInfo verifyToken(String token) {
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(ssoProperties.getxKey())
                    .parseClaimsJws(token)
                    .getBody();

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserId(claims.getSubject());
            tokenInfo.setUsername(claims.get("X-ID", String.class));
            tokenInfo.setIssuedAt(claims.getIssuedAt());
            tokenInfo.setExpiration(claims.getExpiration());

            return tokenInfo;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token已过期", e);
        } catch (Exception e) {
            throw new TengoSsoException("Token验证失败", e);
        }
    }

    @Override
    public String refreshToken(String token) {
        TokenInfo tokenInfo = verifyToken(token);

        // 更新过期时间
        Date now = new Date();
        tokenInfo.setIssuedAt(now);
        tokenInfo.setExpiration(new Date(now.getTime() +
                ssoProperties.getTokenExpire() * 60 * 1000L));

        return createToken(tokenInfo);
    }

    @Override
    public void removeToken(String token) {
        // 服务端可以维护一个黑名单，这里简单实现
        // 生产环境应该使用Redis等存储黑名单
    }
}
