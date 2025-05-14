package com.example.service;

import com.example.model.Course;
// 假设你的 CourseDAO 迁移后变成了 MyBatis Mapper 接口
// 或者你仍然使用原始的 CourseDAO，但需要将其声明为 Spring Bean
import com.example.mapper.CourseMapper; // 或者 com.example.dao.CourseDAO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 如果需要事务管理

import java.util.List;

@Service // 声明为 Spring Service Bean
public class CourseServiceImpl implements CourseService {

    // 方案 A: 如果你的 CourseDAO 迁移成了 MyBatis Mapper 接口
    private final CourseMapper courseMapper; // MyBatis Mapper

    @Autowired
    public CourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    @Transactional(readOnly = true) // 示例：只读事务
    public List<Course> getCoursesByUserId(int userId) {
        System.out.println("CourseServiceImpl: 调用 courseMapper.getCoursesByUserId for userId: " + userId);
        return courseMapper.getCoursesByUserId(userId); // 调用 Mapper 方法
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllAvailableCourses() {
        System.out.println("CourseServiceImpl: 调用 courseMapper.getAllAvailableCourses");
        return courseMapper.getAllAvailableCourses(); // 调用 Mapper 方法
    }
}