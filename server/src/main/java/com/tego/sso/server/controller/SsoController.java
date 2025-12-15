package com.tego.sso.server.controller;

import com.tego.sso.AuthenticationResult;
import com.tego.sso.TokenInfo;
import com.tego.sso.UserAuthenticationService;
import com.tego.sso.UserInfo;
import com.tego.sso.server.JwtTokenManager;
import com.tego.sso.server.service.KeyPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@RestController
@RequestMapping("/sso")
public class SsoController {

    @Autowired
    private JwtTokenManager tokenManager;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private KeyPairService keyPairService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password) {
        AuthenticationResult result = userAuthenticationService.authenticate(username, password);

        Map<String, Object> response = new HashMap<>();
        if (result.isSuccess()) {
            UserInfo userInfo = result.getUserInfo();

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserId(userInfo.getUserId());
            tokenInfo.setUsername(userInfo.getUsername());
            tokenInfo.setIssuedAt(new Date());
            tokenInfo.setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)); // 30分钟
            tokenInfo.setClaims(userInfo.getAttributes());

            String token = tokenManager.createToken(tokenInfo);

            response.put("success", true);
            response.put("token", token);
            response.put("userInfo", userInfo);
        } else {
            response.put("success", false);
            response.put("message", result.getMessage());
        }

        return response;
    }

    /**
     * 验证Token
     */
    @PostMapping("/verify")
    public Map<String, Object> verifyToken(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            TokenInfo tokenInfo = tokenManager.verifyToken(token);
            response.put("valid", true);
            response.put("userInfo", tokenInfo);
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public Map<String, Object> refreshToken(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String newToken = tokenManager.refreshToken(token);
            response.put("success", true);
            response.put("token", newToken);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    /**
     * 获取公钥
     */
    @GetMapping("/public-key")
    public Map<String, Object> getPublicKey() {
        Map<String, Object> response = new HashMap<>();
        response.put("publicKey", keyPairService.getPublicKeyBase64());
        return response;
    }
}
