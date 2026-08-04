package com.tengo.core.mapper;

import com.tengo.core.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserRepository {

    @Select("SELECT * FROM sso_user WHERE username = #{username} LIMIT 1")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM sso_user WHERE id = #{id} LIMIT 1")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM sso_user WHERE user_id = #{userId} LIMIT 1")
    User findByUserId(@Param("userId") String userId);

    @Update("UPDATE sso_user SET password = #{password}, updated_at = NOW() WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
