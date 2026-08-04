package com.tengo.core.xi;

import com.tengo.core.pojo.User;
import com.tengo.core.R;
import com.tengo.core.pojo.TengoAuthUser;

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
    R<User> authenticate(String username, String password);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    TengoAuthUser getUserById(java.lang.Long userId);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    TengoAuthUser getUserByUsername(String username);
}
