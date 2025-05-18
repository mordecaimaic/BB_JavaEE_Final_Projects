package com.example.mapper;

import com.example.model.User;
import org.apache.ibatis.annotations.Param; // 建议为多参数或复杂类型参数添加此注解

public interface UserMapper {

    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    int insertUser(User user);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象，如果未找到则返回null
     */
    User selectByUsername(String username);

    /**
     * 根据用户名获取密码哈希 (如果您的登录逻辑需要先获取密码再比较)
     * @param username 用户名
     * @return 密码哈希字符串
     */
    String getPasswordHash(String username); // 这个方法在您当前的ProfileController中没有直接用到，但可能在登录逻辑中

    // ========= 新增的方法声明 =========

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户对象，如果未找到则返回null
     */
    User selectUserById(int userId);

    /**
     * 更新用户的个人资料（例如电话和邮箱）
     * @param user 包含要更新的用户信息的对象 (至少包含 userId, phone, email)
     * @return 影响的数据库行数 (通常是 1 表示成功, 0 表示未更新或失败)
     */
    int updateUserProfileDetails(User user);

}