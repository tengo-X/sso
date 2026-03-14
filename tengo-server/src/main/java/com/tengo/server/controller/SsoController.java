package com.tengo.server.controller;

import com.tengo.core.R;
import com.tengo.core.config.UrlConf;
import com.tengo.core.pojo.User;
import com.tengo.core.xi.UserAuthenticationService;
import com.tengo.core.pojo.TokenInfo;
import com.tengo.server.service.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    /**
     * 用户登录
     */
    @RequestMapping(UrlConf.LOGIN)
    public R<String> login(String username, String password) {
        R<User> result = userAuthenticationService.authenticate(username, password);

        if (!result.isSuccess()) {
            return new R<>(result.getCode(),result.getMessage());
        }

        User user = result.getData();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(user.getUserId());
        tokenInfo.setUsername(user.getUsername());
        tokenInfo.setIssuedAt(new Date());
        tokenInfo.setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)); // 30分钟

        String token = tokenManager.createToken(tokenInfo);

        return new R<>(200,token);
    }

    /**
     * 验证Token
     */
    @RequestMapping(UrlConf.VERIFY)
    public R<TokenInfo> verifyToken(@RequestParam String token) {
        try {
            TokenInfo tokenInfo = tokenManager.verifyToken(token);
            return new R<>(200,tokenInfo);
        } catch (Exception e) {
            return R.NOT_LOGIN;
        }
    }

    /**
     * 刷新Token
     */
    @RequestMapping(UrlConf.REFRESH)
    public R<String> refreshToken(@RequestParam String token) {
        try {
            String newToken = tokenManager.refreshToken(token);
            return new R<>(200,newToken);
        } catch (Exception e) {
            return R.NOT_LOGIN;
        }
    }
}
