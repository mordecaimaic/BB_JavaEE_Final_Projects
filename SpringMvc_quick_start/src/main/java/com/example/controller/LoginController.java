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
        return "login";
    }

    /** POST /login —— 处理登录 */
    @PostMapping
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("role") String role, // 新增：接收角色参数
                          HttpSession session,
                          Model model) {

        // 修改：调用登录服务时传入角色
        User user = authService.login(username, password, role);

        if (user == null) {
            // 修改：更新错误信息
            model.addAttribute("errorMessage", "用户名、密码或角色错误");
            return "login";
        }
        // 存 session
        session.setAttribute("user", user);
        return "redirect:/index"; // 建议重定向到 dashboard 或 index
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "REMEMBER_SERIES", required = false) String series,
                         @CookieValue(value = "REMEMBER_TOKEN",  required = false) String token,
                         HttpServletResponse resp) {

        // 建议在这里也清空 session
        // session.invalidate();

        // 清空 Cookie
        for (String n : new String[]{"REMEMBER_SERIES", "REMEMBER_TOKEN"}) {
            Cookie c = new Cookie(n, null);
            c.setMaxAge(0); c.setPath("/");
            resp.addCookie(c);
        }
        return "redirect:/login";
    }
}