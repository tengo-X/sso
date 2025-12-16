package com.tego.sso.core.mapper;

import com.tego.sso.pojo.UserEntity;
import org.springframework.stereotype.Service;

/**
 * @author dengxiao
 * @date 2025-12-16
 */
@Service
public class UserRepository {


    public UserEntity findByUsername(String username) {

        return new UserEntity();
    }

    public void save(UserEntity userEntity) {

    }

    public UserEntity findById(Long userId) {

        return new UserEntity();
    }
}
