package com.tengo.core.mapper;

import com.tengo.core.pojo.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dengxiao
 * @date 2025-12-16
 */
@Service
public class UserRepository {

    private static final List<User> USER_LIST = new ArrayList<>();

    static {
        for (int i = 0; i < 10; i++) {
            User u = new User();
            u.setUserId((100+i) + "");
            u.setId(Long.parseLong((100+i)+""));
            u.setUsername("user"+i);
            u.setPassword("000000");
            USER_LIST.add(u);
        }
    }

    public User findByUsername(String username) {

        for (User user : USER_LIST) {
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        return new User();
    }

    public void save(User user) {

    }

    public User findById(Long userId) {

        return new User();
    }
}
