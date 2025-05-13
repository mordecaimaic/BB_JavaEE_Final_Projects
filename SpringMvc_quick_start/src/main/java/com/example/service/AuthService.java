package com.example.service;

import com.example.model.User;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    /** 登录成功返回 true */
    User login(String username, String rawPassword);

    /** 清除登录（退出登录） */
    void logout(String username);
}