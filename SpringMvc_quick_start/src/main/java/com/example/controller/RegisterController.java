package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    /** GET /register   显示页面 */
    @GetMapping
    public String showForm() {
        return "register";
    }

    /** POST /register  提交表单 */
    @PostMapping
    public String doRegister(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("confirmPassword") String confirmPassword,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam("role") String role, // 新增：接收角色参数
                             Model model) {

        // 1. 基础校验
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "两次密码不一致！");
            return "register";
        }
        // 注意：这里的查询应该也考虑角色，但为简化，我们假设用户名在整个系统是唯一的
        if (userService.findByUsername(username) != null) {
            model.addAttribute("errorMessage", "用户名已存在！");
            return "register";
        }

        // 2. 组装实体
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);     // *生产环境记得加密*
        user.setEmail(email);
        user.setRole(role); // 修改：使用前端传递的角色

        // 3. 调 Service -> Mapper -> INSERT
        boolean ok = userService.register(user);
        if (!ok) {
            model.addAttribute("errorMessage", "注册失败，请稍后重试");
            return "register";
        }

        // 4. 成功跳转
        return "redirect:/login";
    }
}