package com.example.mapper;

import com.example.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    int insertUser(User user);

    User selectByUsername(String username);

    String getPasswordHash(String username);

    // 新增：按用户名和角色查询的方法
    User selectByUsernameAndRole(@Param("username") String username, @Param("role") String role);

    User selectUserById(int userId);

    int updateUserProfileDetails(User user);

}