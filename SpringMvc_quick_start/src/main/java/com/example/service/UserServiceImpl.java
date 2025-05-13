package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.mapper.UserMapper;
import com.example.model.User;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public boolean register(User user) {
        return userMapper.insertUser(user) == 1;
    }
}