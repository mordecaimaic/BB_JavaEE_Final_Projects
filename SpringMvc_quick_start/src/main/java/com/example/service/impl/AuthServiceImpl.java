package com.example.service.impl;

import com.example.mapper.PersistentLoginMapper;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper              userMapper;
    private final PersistentLoginMapper   tokenMapper; // 假设用于“记住我”功能，保留

    // 注意：由于使用了 @RequiredArgsConstructor，理论上 passwordEncoder 也应为 final
    // 但若您习惯 @Autowired，也可保留。为保持一致性，统一为 final + 构造器注入。
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 重写了接口的 login 方法，使用了正确的参数
     */
    @Override
    @Transactional(readOnly = true)
    public User login(String username, String rawPassword, String role) {
        // 1. 修改：调用新的 Mapper 方法，同时按用户名和角色查询
        User dbUser = userMapper.selectByUsernameAndRole(username, role);

        // 2. 验证用户是否存在，以及密码是否匹配
        if (dbUser != null && passwordEncoder.matches(rawPassword, dbUser.getPassword())) {
            return dbUser;          // OK! 登录成功
        }
        return null;                // 失败
    }

    @Override
    @Transactional
    public void logout(String username) {
        // 此处的逻辑保持不变，用于处理“记住我”的令牌
        if (username != null) {
            tokenMapper.deleteByUsername(username);
        }
    }
}