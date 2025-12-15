package com.tego.sso.security;

import com.tego.sso.filter.SsoTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SsoSecurityConfig  {

    @Autowired
    private SsoTokenFilter ssoTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers("/sso/**", "/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(ssoTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
