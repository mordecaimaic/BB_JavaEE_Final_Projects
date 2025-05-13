package com.example.controller; // 假设你的 Spring MVC 项目包结构

import com.example.model.Course; // 你的 Course 模型类
import com.example.model.User;   // 你的 User 模型类
import com.example.service.CourseService; // 新建的 CourseService 接口
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/courses") // 映射到 /courses路径
public class CourseController {

    private final CourseService courseService; // 注入 CourseService

    @Autowired // 构造器注入
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping // 处理 GET 请求
    public String listCourses(HttpSession session, Model model) {
        System.out.println("CourseController: 处理 GET /courses 请求...");

        // 1. 检查用户是否登录 (从 HttpSession 获取 "user" 属性)
        User loggedInUser = (User) session.getAttribute("user"); // 与你 LoginController 中设置的属性名一致

        if (loggedInUser == null) {
            System.out.println("CourseController: 用户未登录，重定向到登录页面");
            return "redirect:/login"; // 重定向到登录页的 Controller 映射
        }

        System.out.println("CourseController: 用户 " + loggedInUser.getUsername() + " (ID: " + loggedInUser.getUserId() + ") 已登录。");

        // 2. 调用 Service 获取数据
        List<Course> myCourses = null;
        List<Course> availableCourses = null;
        String errorMessage = null;

        try {
            System.out.println("CourseController: 正在调用 courseService 获取用户课程...");
            myCourses = courseService.getCoursesByUserId(loggedInUser.getUserId());
            System.out.println("CourseController: 获取到 " + (myCourses != null ? myCourses.size() : 0) + " 门用户课程。");

            System.out.println("CourseController: 正在调用 courseService 获取所有可用课程...");
            availableCourses = courseService.getAllAvailableCourses();
            System.out.println("CourseController: 获取到 " + (availableCourses != null ? availableCourses.size() : 0) + " 门可用课程。");

        } catch (Exception e) {
            errorMessage = "加载课程信息时出错，请稍后重试。";
            System.err.println("CourseController: 调用 CourseService 时发生错误: " + e.getMessage());
            e.printStackTrace(); // 在实际应用中，这里应该使用日志框架记录错误
        }

        // 3. 将数据添加到 Model (替代 request.setAttribute)
        model.addAttribute("myCourses", myCourses);
        model.addAttribute("availableCourses", availableCourses);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        // 4. 返回 JSP 页面的名称 (视图解析器会处理路径)
        System.out.println("CourseController: 准备返回 courses 视图");
        return "courses"; // 对应 /WEB-INF/jsp/courses.jsp (根据你的视图解析器配置)
    }

    // 如果需要处理 POST 请求 (例如添加课程)，可以添加 @PostMapping 方法
    // @PostMapping("/add")
    // public String addCourse(...) { ... }
}