package com.example.service;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    /** 登录成功返回 true */
    boolean login(String username, String rawPassword,
                  boolean rememberMe, HttpServletResponse resp);

    /** 清除登录（退出登录） */
    void logout(String username);
}