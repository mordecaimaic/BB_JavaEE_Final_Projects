package com.example.controller;

import com.example.model.User;
import com.example.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    /** POST /login —— 处理登录 */
    @PostMapping
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          HttpSession session,
                          Model model) {

        User user = authService.login(username, password);
        if (user == null) {
            model.addAttribute("errorMessage", "用户名或密码错误");
            return "login";
        }
        // 存 session
        session.setAttribute("user", user);
        return "redirect:/index";
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