package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@SessionAttributes("loggedInUser")
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        User profileUser = userService.getUserById(loggedInUser.getUserId());
        if (profileUser != null) {
            model.addAttribute("profileUser", profileUser);
        } else {
            model.addAttribute("errorMessage", "无法加载您的个人信息。");
        }
        return "profile";
    }

    // 处理个人信息更新
    @PostMapping(params = "action=updateProfile")
    public String handleUpdateProfile(@RequestParam("phone") String phone, // (1) 显式指定参数名
                                      @RequestParam("email") String email, // (1) 显式指定参数名
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            redirectAttributes.addFlashAttribute("errorMessage", "请输入有效的邮箱地址！");
            return "redirect:/profile";
        }

        User userToUpdate = new User();
        userToUpdate.setUserId(loggedInUser.getUserId());
        userToUpdate.setPhone(phone);
        userToUpdate.setEmail(email);

        if (userService.updateUserProfile(userToUpdate)) {
            redirectAttributes.addFlashAttribute("successMessage", "个人信息更新成功！");
            User updatedUserFromDB = userService.getUserById(loggedInUser.getUserId());
            if (updatedUserFromDB != null) {
                session.setAttribute("user", updatedUserFromDB);
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "个人信息更新失败，请稍后重试。");
        }
        return "redirect:/profile";
    }

    // (2) 新增：处理密码修改的方法 (如果还没有的话)
    @PostMapping(params = "action=changePassword")
    public String handleChangePassword(@RequestParam("currentPassword") String currentPassword,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // 基本的非空和长度校验
        if (currentPassword == null || currentPassword.isEmpty() ||
                newPassword == null || newPassword.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "所有密码字段都不能为空！");
            return "redirect:/profile";
        }

        if (newPassword.length() < 6) { // 假设密码最小长度为6
            redirectAttributes.addFlashAttribute("errorMessage", "新密码长度不能少于6位！");
            return "redirect:/profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "两次输入的新密码不一致！");
            return "redirect:/profile";
        }

        // 调用 Service 层处理密码修改逻辑
        // 假设 UserService 中有 changePassword 方法:
        // boolean success = userService.changePassword(loggedInUser.getUserId(), currentPassword, newPassword);

        // **** 示例：假设 UserService 中有 changePassword 方法 ****
        // 你需要在 UserService 和 UserServiceImpl 中实现这个方法
        // 这里暂时用一个占位符表示成功或失败
        boolean passwordChangeSuccess = true; // **替换为实际的 userService.changePassword(...) 调用**
        // 例如:
        // boolean passwordChangeSuccess = userService.changePassword(loggedInUser.getUserId(), currentPassword, newPassword);
        // if (passwordChangeSuccess) { ... } else { ... }

        if (passwordChangeSuccess) {
            redirectAttributes.addFlashAttribute("successMessage", "密码修改成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "密码修改失败，请检查当前密码或稍后重试。");
        }
        return "redirect:/profile";
    }
}