package com.tego.sso.security;

import com.tego.sso.TokenInfo;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.util.Collections;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public class SsoAuthentication implements Authentication {

    private final TokenInfo tokenInfo;
    private boolean authenticated = true;

    public SsoAuthentication(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 可以根据用户信息返回相应的权限
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Object getCredentials() {
        return tokenInfo;
    }

    @Override
    public Object getDetails() {
        return tokenInfo;
    }

    @Override
    public Object getPrincipal() {
        return tokenInfo.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return tokenInfo.getUsername();
    }
}
