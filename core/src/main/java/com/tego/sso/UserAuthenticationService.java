package com.tego.sso;

/**
 * @author dengxiao
 * @date 2023-12-12
 */
public interface UserAuthenticationService {

    /**
     * 用户登录认证
     * @param username 用户名
     * @param password 密码
     * @return 认证结果
     */
    AuthenticationResult authenticate(String username, String password);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfo getUserById(Long userId);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserInfo getUserByUsername(String username);
}
