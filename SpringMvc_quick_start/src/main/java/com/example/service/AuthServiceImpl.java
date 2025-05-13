package com.example.service;

import com.example.mapper.PersistentLoginMapper;
import com.example.mapper.UserMapper;
import com.example.model.PersistentLogin;
import com.example.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor        // Lombok – 注入构造器
public class AuthServiceImpl implements AuthService {

    private final UserMapper              userMapper;
    private final PersistentLoginMapper   tokenMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User login(String username, String rawPassword) {
        User dbUser = userMapper.selectByUsername(username);
        if (dbUser != null && passwordEncoder.matches(rawPassword, dbUser.getPassword())) {
            return dbUser;          // OK!
        }
        return null;                // 失败
    }

    @Override
    @Transactional
    public void logout(String username) {
        tokenMapper.deleteByUsername(username);
        // 这里把 Cookie 清除的动作放在 Controller 里做
    }
}