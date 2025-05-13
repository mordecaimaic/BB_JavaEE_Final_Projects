package com.example.service;

import com.example.mapper.PersistentLoginMapper;
import com.example.mapper.UserMapper;
import com.example.model.PersistentLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    @Override
    @Transactional(readOnly = true)
    public boolean login(String username,
                         String rawPassword,
                         boolean rememberMe,
                         HttpServletResponse resp) {

        String hash = userMapper.getPasswordHash(username);
        if (hash == null || !BCrypt.checkpw(rawPassword, hash)) {
            return false;                 // 用户名不存在或密码错误
        }

        if (rememberMe) { // 生成 token 保存到 Cookie + DB
            String series = UUID.randomUUID().toString().replace("-", "");
            String token  = UUID.randomUUID().toString().replace("-", "");

            PersistentLogin pl = new PersistentLogin();
            pl.setSeries(series);
            pl.setUsername(username);
            pl.setToken(token);
            pl.setLastUsed(new Timestamp(System.currentTimeMillis()));
            tokenMapper.save(pl);

            Cookie c1 = new Cookie("REMEMBER_SERIES", series);
            Cookie c2 = new Cookie("REMEMBER_TOKEN",  token);
            int maxAge = 60 * 60 * 24 * 7; // 7 天
            c1.setMaxAge(maxAge);  c2.setMaxAge(maxAge);
            c1.setPath("/");       c2.setPath("/");
            resp.addCookie(c1);    resp.addCookie(c2);
        }
        return true;
    }

    @Override
    @Transactional
    public void logout(String username) {
        tokenMapper.deleteByUsername(username);
        // 这里把 Cookie 清除的动作放在 Controller 里做
    }
}