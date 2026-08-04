package com.tengo.service;

import com.tengo.core.R;
import com.tengo.core.config.ClientSsoProperties;
import com.tengo.core.config.KeyConf;
import com.tengo.core.config.UrlConf;
import com.tengo.core.exception.TengoSsoException;
import com.tengo.core.pojo.TengoSsoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 客户端 Token 验证服务
 * 通过远程调用 SSO 服务端 /sso/verify 接口完成 token 认证和黑名单校验
 */
@Service
public class ClientTokenManager {

    @Autowired
    private WebClient webClient;

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
