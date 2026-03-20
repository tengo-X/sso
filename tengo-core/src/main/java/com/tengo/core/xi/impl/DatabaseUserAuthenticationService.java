package com.tengo.core.xi.impl;

import com.tengo.core.R;
import com.tengo.core.mapper.UserRepository;
import com.tengo.core.pojo.AuthUser;
import com.tengo.core.pojo.User;
import com.tengo.core.xi.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dengxiao
 * @date 2023-12-14
 */
@Service
public class DatabaseUserAuthenticationService implements UserAuthenticationService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    @Qualifier("bcryptPasswordEncoder")
//    private PasswordEncoder passwordEncoder;

    @Override
    public R<User> authenticate(String username, String password) {
        // 1. 查询用户
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new R<>(500, "用户名不存在", null);
        }

        // 2. 检查账户状态
        if (!user.getEnabled()) {
            return new R<>(500, "账户已被禁用", null);
        }

        if (user.getLocked()) {
            return new R<>(500, "账户已被锁定", null);
        }

        if (user.getExpired()) {
            return new R<>(500, "账户已过期", null);
        }

        // 3. 验证密码
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            return new R<>(500, "密码错误", null);
//        }
        if (!password.equals(user.getPassword())) {
            return new R<>(500, "密码错误", null);
        }

        return new R<>(200,user);
    }

    @Override
    public AuthUser getUserById(java.lang.Long userId) {
        User userEntity = userRepository.findById(userId);
        return userEntity != null ? convertToUserInfo(userEntity) : null;
    }

    @Override
    public AuthUser getUserByUsername(String username) {
        User userEntity = userRepository.findByUsername(username);
        return userEntity != null ? convertToUserInfo(userEntity) : null;
    }

    /**
     * 将数据库实体转换为UserInfo
     */
    private AuthUser convertToUserInfo(User userEntity) {
        AuthUser userInfo = new AuthUser();
        userInfo.setUserId(userEntity.getId()+"");
        userInfo.setUsername(userEntity.getUsername());
        userInfo.setEmail(userEntity.getEmail());
        userInfo.setPhone(userEntity.getPhone());
        return userInfo;
    }
}
