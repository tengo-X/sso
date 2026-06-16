package com.tengo.server.util;

import com.tengo.core.config.KeyConf;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author dx
 * @date 2024/4/11
 */
public class JWTUtil {

    public static void genRefreshTokenCookie(String refreshToken, HttpServletResponse response,long refreshTokenExpired) {
        ResponseCookie refreshCookie = ResponseCookie.from(KeyConf.RT, refreshToken)
//                .domain(domain)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(refreshTokenExpired)
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());
        response.setStatus(200);
    }

    public static String genToken(String signKey, long expired, SignatureAlgorithm algorithm, String subject, String jti) {
        return Jwts.builder()
                .setSubject(subject)
                .setId(jti)
                .signWith(algorithm,signKey)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expired))
                .compact();
    }
}
