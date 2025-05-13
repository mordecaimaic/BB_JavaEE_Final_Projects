package com.example.service;

import com.example.model.Course;
import java.util.List;

public interface CourseService {
    List<Course> getCoursesByUserId(int userId);
    List<Course> getAllAvailableCourses();
    // 其他与课程相关的业务方法，例如 addCourse, deleteCourse 等
}