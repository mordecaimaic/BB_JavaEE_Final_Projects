package com.example.service;
import com.example.model.User;

public interface UserService {
    User findByUsername(String username);
    boolean register(User user);
}