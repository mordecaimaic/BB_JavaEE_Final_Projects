package com.example.controller;

import com.example.model.Announcement;
import com.example.model.User;
import com.example.service.AnnouncementService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public String showList(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }
        List<Announcement> list = announcementService.listForUser(loginUser.getDepartment());
        model.addAttribute("announcements", list);
        return "announcements";   // 视图：/WEB-INF/views/announcements.jsp
    }
}