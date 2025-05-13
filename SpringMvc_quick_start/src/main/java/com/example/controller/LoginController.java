package com.example.controller;

import com.example.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    @GetMapping
    public String showLoginForm() {
        return "login";  // /WEB-INF/views/login.jsp
    }

    @PostMapping
    public String doLogin(@RequestParam("username") String username, // 显式指定请求参数名
                          @RequestParam("password") String password, // 显式指定请求参数名
                          @RequestParam(value = "rememberMe", defaultValue = "false")
                          boolean rememberMe,
                          Model model,
                          HttpServletResponse resp) {

        boolean ok = authService.login(username, password, rememberMe, resp);

        if (!ok) {
            model.addAttribute("errorMessage", "用户名或密码错误");
            return "login";
        }
        return "redirect:/home";   // 登录成功跳首页
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "REMEMBER_SERIES", required = false) String series,
                         @CookieValue(value = "REMEMBER_TOKEN",  required = false) String token,
                         HttpServletResponse resp) {

        // 清空 Cookie
        for (String n : new String[]{"REMEMBER_SERIES", "REMEMBER_TOKEN"}) {
            Cookie c = new Cookie(n, null);
            c.setMaxAge(0); c.setPath("/");
            resp.addCookie(c);
        }
        // 如有需要可调用 authService.logout(username) 这里没 username 可省略
        return "redirect:/login";
    }
}