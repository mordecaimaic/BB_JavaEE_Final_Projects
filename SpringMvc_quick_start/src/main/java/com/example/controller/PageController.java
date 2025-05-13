package com.example.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/index"})
    public String index(HttpSession session) {
        if (session.getAttribute("loginUser") != null) {
            // 已登录 → 去仪表盘
            return "redirect:/dashboard";
        }
        // 未登录 → 去登录页
        return "redirect:/login";
    }

//    @GetMapping("/hello")
//    public String hello() {
//        return "hello";
//    }
//
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
//
//    @GetMapping("/announcements")
//    public String announcements() {
//        return "announcements";
//    }
//
//    @GetMapping("/assignments")
//    public String assignments() {
//        return "assignments";
//    }
//
//    @GetMapping("/courses")
//    public String courses() {
//        return "courses";
//    }
//
//    @GetMapping("/materials")
//    public String materials() {
//        return "materials";
//    }
//
//    @GetMapping("/profile")
//    public String profile() {
//        return "profile";
//    }
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String register() {
//        return "register";
//    }
//
//    @GetMapping("/schedule")
//    public String schedule() {
//        return "schedule";
//    }

    // header.jsp 通常是片段 include，不单独映射
}