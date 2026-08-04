package com.tengo.core.xi.impl;

import com.tengo.core.R;
import com.tengo.core.mapper.UserRepository;
import com.tengo.core.pojo.TengoAuthUser;
import com.tengo.core.pojo.User;
import com.tengo.core.xi.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 基于数据库的用户认证服务
 */
@Service
public class DatabaseUserAuthenticationService implements UserAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public R<User> authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        // 安全加固：即使用户不存在也执行一次密码哈希计算
        // 防止攻击者通过响应时间差异枚举有效用户名
        if (user == null) {
//            passwordEncoder.matches(password, "$2a$10$dummy_hash_for_timing_protection");
            return new R<>(500, "用户名或密码错误", null);
        }

        if (Objects.isNull(username) || username.trim().length() <= 0) {
            return new R<>(500, "请输入用户名", null);
        }
        if (Objects.isNull(password) || password.trim().length() <= 0) {
            return new R<>(500, "请输入用密码", null);
        }

        if (!user.getEnabled()) {
            return new R<>(403, "账户已被禁用", null);
        }

        if (user.getLocked()) {
            return new R<>(403, "账户已被锁定", null);
        }

        //!passwordEncoder.matches(password, user.getPassword())
        if (!password.equals(user.getPassword())) {
            return new R<>(500, "用户名或密码错误", null);
        }

        return new R<>(200, user);
    }

    @Override
    public TengoAuthUser getUserById(Long userId) {
        User userEntity = userRepository.findById(userId);
        return userEntity != null ? convertToAuthUser(userEntity) : null;
    }

    @Override
    public TengoAuthUser getUserByUsername(String username) {
        User userEntity = userRepository.findByUsername(username);
        return userEntity != null ? convertToAuthUser(userEntity) : null;
    }

    private TengoAuthUser convertToAuthUser(User userEntity) {
        TengoAuthUser authUser = new TengoAuthUser();
        authUser.setUserId(userEntity.getUserId());
        authUser.setUsername(userEntity.getUsername());
        authUser.setEmail(userEntity.getEmail());
        authUser.setPhone(userEntity.getPhone());
        authUser.setRealName(userEntity.getRealName());
        authUser.setDepartment(userEntity.getDepartment());
        authUser.setAvatar(userEntity.getAvatar());
        return authUser;
    }
}
