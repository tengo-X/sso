package com.tego.sso.server;

import com.tego.sso.TokenInfo;
import com.tego.sso.TokenManager;
import com.tego.sso.core.SsoProperties;
import com.tego.sso.server.service.KeyPairService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@Component
public class JwtTokenManager implements TokenManager {
    @Autowired
    private SsoProperties ssoProperties;

    @Autowired
    private KeyPairService keyPairService;

    @Override
    public String createToken(TokenInfo tokenInfo) {
        PrivateKey privateKey = keyPairService.getPrivateKey();

        return Jwts.builder()
                .setSubject(tokenInfo.getUserId())
                .claim("username", tokenInfo.getUsername())
                .claim("claims", tokenInfo.getClaims())
                .setIssuedAt(tokenInfo.getIssuedAt())
                .setExpiration(tokenInfo.getExpiration())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    @Override
    public TokenInfo verifyToken(String token) {
        try {
            PublicKey publicKey = keyPairService.getPublicKey();

            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserId(claims.getSubject());
            tokenInfo.setUsername(claims.get("username", String.class));
            tokenInfo.setIssuedAt(claims.getIssuedAt());
            tokenInfo.setExpiration(claims.getExpiration());
            tokenInfo.setClaims(claims.get("claims", Map.class));

            return tokenInfo;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token已过期", e);
        } catch (Exception e) {
            throw new RuntimeException("Token验证失败", e);
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
