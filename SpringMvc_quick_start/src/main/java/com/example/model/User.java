package com.example.model;

import lombok.Data; // 引入 Lombok 的 @Data 注解
import lombok.NoArgsConstructor; // 引入 Lombok 的 @NoArgsConstructor 注解
import lombok.AllArgsConstructor; // 引入 Lombok 的 @AllArgsConstructor 注解
import lombok.ToString; // 引入 Lombok 的 @ToString 注解 (用于排除字段)
import lombok.EqualsAndHashCode; // 引入 Lombok 的 @EqualsAndHashCode 注解 (用于排除字段)

import java.sql.Timestamp; // 保留 Timestamp 导入

@Data // 包含了 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@NoArgsConstructor // 生成无参构造函数
@AllArgsConstructor // 生成包含所有字段的构造函数
public class User {

    private int userId;          // 对应 user_id (INT PK AI)
    private String username;     // 对应 username (VARCHAR(50) NOT NULL UNIQUE)

    @ToString.Exclude // 不在 toString() 方法中包含密码哈希，防止日志泄露
    @EqualsAndHashCode.Exclude // 通常不应基于密码哈希来判断对象是否相等
    private String password;     // 对应 password (VARCHAR(255) NOT NULL) - 存储哈希密码

    private String phone;        // 对应 phone (VARCHAR(20))
    private String email;        // 对应 email (VARCHAR(100))
    private String studentId;    // 对应 student_id (VARCHAR(20) UNIQUE)
    private String department;   // 对应 department (VARCHAR(50))
    private String role;         // 对应 role (ENUM as String: 'student', 'teacher', 'admin')

    @EqualsAndHashCode.Exclude // 创建时间通常不参与对象相等性比较
    private Timestamp createdAt; // 对应 created_at (TIMESTAMP)
}