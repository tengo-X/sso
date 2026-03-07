package com.tego.sso.core.xi.impl;

import com.tego.sso.core.R;
import com.tego.sso.core.xi.UserAuthenticationService;
import com.tego.sso.core.pojo.AuthUser;
import com.tego.sso.core.mapper.UserRepository;
import com.tego.sso.core.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    public R authenticate(String username, String password) {
        // 1. 查询用户
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            return new R(false, "用户名不存在", null);
        }

        // 2. 检查账户状态
        if (!userEntity.getEnabled()) {
            return new R(false, "账户已被禁用", null);
        }

        if (userEntity.getLocked()) {
            return new R(false, "账户已被锁定", null);
        }

        if (userEntity.getExpired()) {
            return new R(false, "账户已过期", null);
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
            return new R(false, "密码错误", null);
        }

        // 4. 重置失败次数
//        userEntity.setFailedLoginAttempts(0);
//        userEntity.setLastLoginTime(new Date());
        userRepository.save(userEntity);

        // 5. 转换为UserInfo
        AuthUser userInfo = convertToUserInfo(userEntity);
        return new R(true, "登录成功", userInfo);
    }

    @Override
//    @Transactional(readOnly = true)
    public AuthUser getUserById(Long userId) {
        User userEntity = userRepository.findById(userId);
        return userEntity != null ? convertToUserInfo(userEntity) : null;
    }

    @Override
//    @Transactional(readOnly = true)
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
