package com.example.controller;

import com.example.model.Schedule;
import com.example.model.User; // 假设User模型在新项目中路径正确
import com.example.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/schedule") // 基础映射路径，与旧Servlet的 @WebServlet("/schedule") 对应
public class ScheduleController {

    private final ScheduleService scheduleService;
    // 旧Servlet中的DateTimeFormatter，如果JSP提交的日期时间格式是 yyyy-MM-ddTHH:mm
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 对应旧Servlet的doGet方法中显示列表和编辑表单的逻辑
    @GetMapping
    public String showSchedulePage(@RequestParam(value = "action", required = false) String action,
                                   @RequestParam(value = "id", required = false) Integer scheduleId,
                                   HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login"; // login.jsp的Controller映射路径可能是/login
        }

        if ("edit".equals(action) && scheduleId != null) {
            Schedule scheduleToEdit = scheduleService.getScheduleByIdAndUserId(scheduleId, loggedInUser.getUserId());
            if (scheduleToEdit != null) {
                model.addAttribute("scheduleToEdit", scheduleToEdit);
            } else {
                model.addAttribute("errorMessage", "找不到要编辑的日程或无权访问。");
            }
        }

        List<Schedule> schedules = scheduleService.getSchedulesByUserId(loggedInUser.getUserId());
        model.addAttribute("schedules", schedules);
        // 为了让添加表单能绑定对象，可以预先放一个空的Schedule对象
        if (!model.containsAttribute("scheduleToEdit")) { // 只有在不是编辑模式时才添加空的newSchedule
            model.addAttribute("newSchedule", new Schedule()); // 用于表单绑定
        }


        // 处理来自POST重定向的 flash messages (已通过 RedirectAttributes 实现)
        // Spring会自动将 RedirectAttributes 中的 flash attribute 添加到 model
        return "schedule"; // 对应 /WEB-INF/jsp/schedule.jsp
    }

    // 对应旧Servlet的doPost方法中的 "add" action
    @PostMapping("/add")
    public String addSchedule(@ModelAttribute("newSchedule") Schedule schedule, // Spring自动从请求参数创建Schedule对象
                              // 如果JSP中的日期时间字段名与Schedule类不直接匹配或格式特殊，需要单独接收
                              @RequestParam("startTimeString") String startTimeStr,
                              @RequestParam("endTimeString") String endTimeStr,
                              @RequestParam(value = "remindTimeString", required = false) String remindTimeStr,
                              HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            // 一般来说，如果需要登录才能访问，应该用拦截器提前处理
            return "redirect:/login";
        }
        schedule.setUserId(loggedInUser.getUserId());

        try {
            // 手动解析和设置时间，因为@ModelAttribute可能无法直接处理自定义格式的字符串到Timestamp
            if (startTimeStr != null && !startTimeStr.isEmpty()) {
                schedule.setStartTime(Timestamp.valueOf(LocalDateTime.parse(startTimeStr, DATETIME_FORMATTER)));
            }
            if (endTimeStr != null && !endTimeStr.isEmpty()) {
                schedule.setEndTime(Timestamp.valueOf(LocalDateTime.parse(endTimeStr, DATETIME_FORMATTER)));
            }
            if (remindTimeStr != null && !remindTimeStr.isEmpty()) {
                schedule.setRemindTime(Timestamp.valueOf(LocalDateTime.parse(remindTimeStr, DATETIME_FORMATTER)));
            }
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "日期或时间格式无效。");
            return "redirect:/schedule";
        }

        if (scheduleService.addSchedule(schedule)) {
            redirectAttributes.addFlashAttribute("successMessage", "日程 '" + schedule.getTitle() + "' 添加成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "添加日程失败。");
        }
        return "redirect:/schedule";
    }

    // 对应旧Servlet的doPost方法中的 "update" action
    @PostMapping("/update")
    public String updateSchedule(@ModelAttribute("scheduleToEdit") Schedule schedule, // 假设编辑表单绑定到 scheduleToEdit
                                 @RequestParam("startTimeString") String startTimeStr,
                                 @RequestParam("endTimeString") String endTimeStr,
                                 @RequestParam(value = "remindTimeString", required = false) String remindTimeStr,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        schedule.setUserId(loggedInUser.getUserId()); // 非常重要，确保更新的是当前用户的日程

        try {
            if (startTimeStr != null && !startTimeStr.isEmpty()) {
                schedule.setStartTime(Timestamp.valueOf(LocalDateTime.parse(startTimeStr, DATETIME_FORMATTER)));
            }
            if (endTimeStr != null && !endTimeStr.isEmpty()) {
                schedule.setEndTime(Timestamp.valueOf(LocalDateTime.parse(endTimeStr, DATETIME_FORMATTER)));
            }
            if (remindTimeStr != null && !remindTimeStr.isEmpty()) {
                schedule.setRemindTime(Timestamp.valueOf(LocalDateTime.parse(remindTimeStr, DATETIME_FORMATTER)));
            }
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "更新时日期或时间格式无效。");
            return "redirect:/schedule"; // 或者返回编辑页面并显示错误
        }


        if (scheduleService.updateSchedule(schedule)) {
            redirectAttributes.addFlashAttribute("successMessage", "日程 '" + schedule.getTitle() + "' 更新成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "更新日程失败或无权操作。");
        }
        return "redirect:/schedule";
    }

    // 对应旧Servlet的doPost方法中的 "delete" action
    // 使用GET请求进行删除在RESTful设计中不推荐，但为了快速迁移旧Servlet行为，可以暂时保留
    // 更好的方式是使用 @DeleteMapping 并通过表单或JS发送DELETE请求
    @GetMapping("/delete/{scheduleId}") // 使用路径变量更RESTful
    public String deleteSchedule(@PathVariable("scheduleId") int scheduleId,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        if (scheduleService.deleteSchedule(scheduleId, loggedInUser.getUserId())) {
            redirectAttributes.addFlashAttribute("successMessage", "日程删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "删除日程失败或无权操作。");
        }
        return "redirect:/schedule";
    }
}