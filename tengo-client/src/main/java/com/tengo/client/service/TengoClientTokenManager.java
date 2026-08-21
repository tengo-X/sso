package com.tengo.client.service;

import com.tengo.core.R;
import com.tengo.core.config.KeyConf;
import com.tengo.core.config.UrlConf;
import com.tengo.core.pojo.TengoSsoToken;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 客户端 Token 验证服务
 * 通过远程调用 SSO 服务端 /sso/verify 接口完成 token 认证和黑名单校验
 */
public class TengoClientTokenManager {

    private WebClient webClient;

    public TengoClientTokenManager(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 远程调用 SSO 服务端 /sso/verify 接口验证 token
     */
    public TengoSsoToken verifyToken(String token) {

        try {
            R<TengoSsoToken> tengoRet = webClient.get()
                    .uri(UrlConf.VERIFY)
                    .header(KeyConf.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(R.class)
                    .block();

            if (tengoRet != null && tengoRet.isSuccess()) {
                return tengoRet.getData();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
