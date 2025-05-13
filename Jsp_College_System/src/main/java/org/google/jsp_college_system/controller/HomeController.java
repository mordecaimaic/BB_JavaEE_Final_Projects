package org.google.jsp_college_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 映射到根路径 / 或者一个欢迎页面，例如 index.jsp
    @GetMapping("/")
    public String showIndexPage() {
        return "index"; // 对应 /WEB-INF/jsp/index.jsp
    }

    // 映射到 /login 路径，显示登录页面
    @GetMapping("/login")
    public String showLoginPage() {
        // 这里暂时不需要传递 Model 数据，只是显示页面
        return "login"; // 这会解析为 /WEB-INF/jsp/login.jsp
    }

    // 你可以为其他你想先看到的页面添加类似的简单映射
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // 对应 /WEB-INF/jsp/register.jsp
    }

    @GetMapping("/dashboard")
    public String showDashboardPage() {
        // 实际应用中，这里通常需要检查用户是否登录
        return "dashboard"; // 对应 /WEB-INF/jsp/dashboard.jsp
    }
}