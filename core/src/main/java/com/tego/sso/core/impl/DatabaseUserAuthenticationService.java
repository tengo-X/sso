package com.tego.sso.core.impl;

import com.tego.sso.AuthenticationResult;
import com.tego.sso.UserAuthenticationService;
import com.tego.sso.UserInfo;
import com.tego.sso.core.mapper.UserRepository;
import com.tego.sso.pojo.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dengxiao
 * @date 2023-12-14
 */
@Service
public class DatabaseUserAuthenticationService implements UserAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("bcryptPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
//    @Transactional(readOnly = true)
    public AuthenticationResult authenticate(String username, String password) {
        // 1. 查询用户
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            return new AuthenticationResult(false, "用户名不存在", null);
        }

        // 2. 检查账户状态
        if (!userEntity.getEnabled()) {
            return new AuthenticationResult(false, "账户已被禁用", null);
        }

        if (userEntity.getLocked()) {
            return new AuthenticationResult(false, "账户已被锁定", null);
        }

        if (userEntity.getExpired()) {
            return new AuthenticationResult(false, "账户已过期", null);
        }

        // 3. 验证密码
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            // 记录密码错误次数
//            userEntity.setFailedLoginAttempts(userEntity.getFailedLoginAttempts() + 1);

            // 如果错误次数超过阈值，锁定账户
//            if (userEntity.getFailedLoginAttempts() >= 5) {
//                userEntity.setLocked(true);
//                userEntity.setLockTime(new Date());
//            }

            userRepository.save(userEntity);
            return new AuthenticationResult(false, "密码错误", null);
        }

        // 4. 重置失败次数
//        userEntity.setFailedLoginAttempts(0);
//        userEntity.setLastLoginTime(new Date());
        userRepository.save(userEntity);

        // 5. 转换为UserInfo
        UserInfo userInfo = convertToUserInfo(userEntity);
        return new AuthenticationResult(true, "登录成功", userInfo);
    }

    @Override
//    @Transactional(readOnly = true)
    public UserInfo getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId);
        return userEntity != null ? convertToUserInfo(userEntity) : null;
    }

    @Override
//    @Transactional(readOnly = true)
    public UserInfo getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        return userEntity != null ? convertToUserInfo(userEntity) : null;
    }

    /**
     * 将数据库实体转换为UserInfo
     */
    private UserInfo convertToUserInfo(UserEntity userEntity) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userEntity.getId()+"");
        userInfo.setUsername(userEntity.getUsername());
        userInfo.setEmail(userEntity.getEmail());
        userInfo.setPhone(userEntity.getPhone());

        // 构建attributes
        Map<String, Object> attributes = new HashMap<>();

        // 角色信息
//        List<String> roles = userEntity.getRoles().stream()
//                .map(UserRole::getRoleName)
//                .collect(Collectors.toList());
//        attributes.put("roles", roles);

        // 权限信息
//        List<String> permissions = userEntity.getPermissions().stream()
//                .map(UserPermission::getPermissionCode)
//                .collect(Collectors.toList());
//        attributes.put("permissions", permissions);

        // 其他属性
        attributes.put("realName", userEntity.getRealName());
        attributes.put("department", userEntity.getDepartment());
        attributes.put("avatar", userEntity.getAvatar());
//        attributes.put("lastLoginTime", userEntity.getLastLoginTime());

        userInfo.setAttributes(attributes);
        return userInfo;
    }
}
