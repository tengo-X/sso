package com.tengo.filter;

import com.tengo.core.R;
import com.tengo.core.config.KeyConf;
import com.tengo.core.pojo.TengoSsoToken;
import com.tengo.service.ClientTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * SSO Token 过滤器
 * 通过远程调用 SSO 服务端 /sso/verify 接口完成 token 认证
 */
@Component
public class SsoTokenFilter extends OncePerRequestFilter {

    @Autowired
    private ClientTokenManager clientTokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (StringUtils.hasText(token)) {
            TengoSsoToken tokenInfo = clientTokenManager.verifyToken(token);
            if (Objects.isNull(tokenInfo)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().println("{\"code\": 401, \"message\": \"请先登录\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 提取 Token（安全加固：禁用 query param 方式，防止 token 泄露到 URL/日志/Referer）
     */
    private String extractToken(HttpServletRequest request) {
        // 优先从 Authorization header 提取
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }

        // 其次从 Cookie 提取
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (KeyConf.COOKIES.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
