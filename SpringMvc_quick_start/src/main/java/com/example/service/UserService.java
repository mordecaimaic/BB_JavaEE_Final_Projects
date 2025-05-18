package com.example.service;

import com.example.model.User;

public interface UserService {
    User findByUsername(String username);

    boolean register(User user);

    User getUserById(int userId); // <--- 添加此行
    boolean updateUserProfile(User user); // <--- 添加此行

}