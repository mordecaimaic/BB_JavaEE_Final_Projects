package com.example.mapper;

import com.example.model.User;

public interface UserMapper {
    int insertUser(User user);
    User selectByUsername(String username);
    String getPasswordHash(String username);
}
