package com.tego.sso.core.mapper;

import com.tego.sso.core.pojo.User;
import org.springframework.stereotype.Service;

/**
 * @author dengxiao
 * @date 2025-12-16
 */
@Service
public class UserRepository {


    public User findByUsername(String username) {

        return new User();
    }

    public void save(User user) {

    }

    public User findById(Long userId) {

        return new User();
    }
}
