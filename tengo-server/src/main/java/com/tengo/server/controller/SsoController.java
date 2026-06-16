package com.tengo.server.controller;

import com.tengo.core.R;
import com.tengo.core.config.KeyConf;
import com.tengo.core.config.UrlConf;
import com.tengo.core.pojo.TokenInfo;
import com.tengo.core.pojo.User;
import com.tengo.core.xi.UserAuthenticationService;
import com.tengo.server.service.JwtTokenManager;
import com.tengo.server.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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

    /**
     * 用户登录
     */
    @RequestMapping(UrlConf.LOGIN)
    public R<String> login(String username, String password, HttpServletResponse response) {
        R<User> result = userAuthenticationService.authenticate(username, password);

        if (!result.isSuccess()) {
            return new R<>(result.getCode(),result.getMessage(), null);
        }

        User user = result.getData();

        String token = tokenManager.createToken(user.getId()+"");

        String refreshToken = tokenManager.createRefreshToken(user.getUserId() + "");
        JWTUtil.genRefreshTokenCookie(refreshToken,response,7*24*60*60);

        return new R<>(200,token);
    }

    /**
     * 验证Token
     */
    @RequestMapping(UrlConf.VERIFY)
    public R<TokenInfo> verifyToken(@RequestParam String token) {
        try {
            TokenInfo tokenInfo = tokenManager.verifyToken(token, KeyConf.AT);
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
