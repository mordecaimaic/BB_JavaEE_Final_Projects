package org.google.jsp_college_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";  // 会去 /WEB-INF/jsp/hello.jsp 找模板
    }
}