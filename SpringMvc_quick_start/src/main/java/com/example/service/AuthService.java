package com.example.service;

import com.example.model.User;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    /**
     * 根据用户名、原始密码和角色进行登录验证
     * @param username 用户名
     * @param rawPassword 原始密码
     * @param role 角色
     * @return 登录成功返回 User 对象, 失败返回 null
     */
    User login(String username, String rawPassword, String role);

    /** 清除登录（退出登录） */
    void logout(String username);
}