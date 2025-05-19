package com.example.service.impl;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // 假设您使用Spring Security的密码编码器
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.mapper.UserMapper;
import com.example.model.User;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder; // 假设您使用Spring Security的密码编码器

    @Autowired
    public UserServiceImpl(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        // 假设 UserMapper 中有方法 selectByUsername
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional // 注册操作通常需要事务管理
    public boolean register(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            // 实际项目中应抛出自定义异常或返回更详细的错误信息
            return false;
        }
        // 检查用户名是否已存在 (可选，但推荐)
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            // 用户名已存在
            return false;
        }
        // 哈希密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 假设 UserMapper 中有方法 insertUser，并返回影响的行数
        return userMapper.insertUser(user) > 0;
    }

    @Override
    public User getUserById(int userId) {
        // 假设 UserMapper 中有方法 selectUserById 或类似名称的方法
        // 请确保 UserMapper 接口及其 XML/注解 实现中存在此方法
        return userMapper.selectUserById(userId);
    }

    @Override
    @Transactional // 更新操作通常需要事务管理
    public boolean updateUserProfile(User user) {
        if (user == null) {
            return false;
        }
        // 可以在这里添加更多验证逻辑，比如邮箱格式、电话号码格式等
        // 假设 UserMapper 中有方法 updateUser 或类似名称的方法，并返回影响的行数
        // 这个方法应该只更新允许用户修改的字段，例如 phone 和 email
        // SQL 语句示例: UPDATE users SET phone = #{phone}, email = #{email} WHERE user_id = #{userId}
        return userMapper.updateUserProfileDetails(user) > 0; // 假设您Mapper中的方法名为updateUserProfileDetails
    }

    // 如果您还需要实现密码修改等功能，可以在这里添加，并确保在 UserService 接口中声明
    // 例如:
    // @Override
    // @Transactional
    // public boolean changePassword(int userId, String currentRawPassword, String newRawPassword) {
    //     User user = userMapper.selectUserById(userId);
    //     if (user == null || user.getPassword() == null) {
    //         return false; // 用户不存在或密码未设置
    //     }
    //
    //     if (passwordEncoder.matches(currentRawPassword, user.getPassword())) {
    //         String newHashedPassword = passwordEncoder.encode(newRawPassword);
    //         // 假设 UserMapper 中有方法 updatePassword
    //         return userMapper.updatePassword(userId, newHashedPassword) > 0;
    //     }
    //     return false; // 当前密码不正确
    // }
}