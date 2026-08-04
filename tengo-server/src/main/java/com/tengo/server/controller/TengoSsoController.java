package com.tengo.server.controller;

import com.tengo.core.R;
import com.tengo.core.config.KeyConf;
import com.tengo.core.config.UrlConf;
import com.tengo.core.exception.TengoSsoException;
import com.tengo.core.pojo.TengoAuthUser;
import com.tengo.core.pojo.TengoSsoToken;
import com.tengo.core.pojo.User;
import com.tengo.core.rate.RateLimiter;
import com.tengo.core.xi.UserAuthenticationService;
import com.tengo.server.service.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SSO 认证控制器
 */
@RestController
@RequestMapping("/sso")
public class TengoSsoController {

    @Autowired
    private JwtTokenManager tokenManager;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private RateLimiter rateLimiter;

    /**
     * 用户登录 — 返回 access token，通过 Cookie 下发 refresh token
     */
    @GetMapping(UrlConf.LOGIN)
    public R<Map<String, Object>> login(String username,String password) {

        if (!rateLimiter.tryAcquire("login:" + username)) {
            throw new TengoSsoException("请求过于频繁，请稍后再试");
        }

        R<User> result = userAuthenticationService.authenticate(username, password);

        if (!result.isSuccess()) {
            return new R<>(result.getCode(), result.getMessage(), null);
        }

        User user = result.getData();
        String userId = user.getId() + "";

        // access token 使用业务 userId
        String accessToken = tokenManager.createToken(userId, user.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", accessToken);

        return new R<>(200, data);
    }

    /**
     * 验证 Access Token
     */
    @GetMapping(UrlConf.VERIFY)
    public R<TengoSsoToken> verifyToken(@RequestHeader(KeyConf.AUTHORIZATION) String accessToken) {
        try {
            TengoSsoToken tokenInfo = tokenManager.verifyToken(accessToken);
            if (Objects.isNull(tokenInfo)) {
                return R.NOT_LOGIN;
            }
            return new R<>(200, tokenInfo);
        } catch (Exception e) {
            return R.NOT_LOGIN;
        }
    }

    /**
     * 登出 — 吊销当前 token 并清除 Cookie
     */
    @GetMapping(UrlConf.LOGOUT)
    public R<Void> logout(@RequestHeader(KeyConf.AUTHORIZATION) String accessToken) {
        // 吊销 access token
        if (accessToken != null) {
            tokenManager.removeToken(accessToken);
        }

        return new R<>(200, null);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userinfo")
    public R<TengoAuthUser> getUserInfo(@RequestHeader(KeyConf.AUTHORIZATION) String token) {
        try {
            TengoSsoToken tokenInfo = tokenManager.verifyToken(token);
            TengoAuthUser authUser = userAuthenticationService.getUserByUsername(tokenInfo.getUsername());
            if (authUser == null) {
                return R.NOT_LOGIN;
            }
            return new R<>(200, authUser);
        } catch (Exception e) {
            return R.NOT_LOGIN;
        }
    }

}
