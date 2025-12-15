package com.tego.sso.filter;

import com.tego.sso.TokenInfo;
import com.tego.sso.TokenManager;
import com.tego.sso.security.SsoAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@Component
public class SsoTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (StringUtils.hasText(token)) {
            try {
                // 验证Token
                TokenInfo tokenInfo = tokenManager.verifyToken(token);

                // 创建认证对象
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (tokenInfo.getClaims() != null) {
                    Map<String, Object> claims = tokenInfo.getClaims();
                    if (claims.containsKey("roles")) {
                        Object roles = claims.get("roles");
                        if (roles instanceof List) {
                            ((List<?>) roles).forEach(role ->
                                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
                            );
                        }
                    }
                }

                if (authorities.isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                tokenInfo.getUsername(),
                                null,
                                authorities
                        );

                authentication.setDetails(tokenInfo);

                // 设置到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                logger.error("SSO Token验证失败: "+e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return request.getParameter("token");
    }
}
