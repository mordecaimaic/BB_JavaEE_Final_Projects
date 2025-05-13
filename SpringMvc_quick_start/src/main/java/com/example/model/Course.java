package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private int courseId;
    private String courseName;
    private int teacherId; // 教师ID
    private String teacherName; // 教师名称 (通过联表查询获取)
    private String classroom;
    private String schedule;
    private int credit; // 注意 SQL 中是 TINYINT，Java 用 int 即可
    private String type; // '必修' 或 '选修'
    private String description;
    private String semester; // 学生选课的学期 (通过联表查询获取)


}
