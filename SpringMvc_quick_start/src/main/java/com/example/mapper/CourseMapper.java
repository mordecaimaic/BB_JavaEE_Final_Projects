package com.example.mapper;

import com.example.model.Course;
// import org.apache.ibatis.annotations.Select; // 不再需要注解
import java.util.List;

public interface CourseMapper {
    // @Select("SELECT * FROM courses WHERE course_id IN (SELECT course_id FROM user_courses WHERE user_id = #{userId})")
    List<Course> getCoursesByUserId(int userId);

    // @Select("SELECT * FROM courses")
    List<Course> getAllAvailableCourses();
    // 其他方法...
}