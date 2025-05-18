package com.example.controller; // 请确保包名正确

import com.example.model.Schedule;
import com.example.model.User;
import com.example.service.ScheduleService; // 使用你的 ScheduleService
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Spring MVC的Flash消息机制

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    private final ScheduleService scheduleService;

    // 与Servlet中一致的日期时间格式化器
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
        logger.info("ScheduleController initialized with ScheduleService.");
    }

    // 模仿 Servlet 的 doGet 方法
    @GetMapping
    public String showSchedulePage(@RequestParam(value = "action", required = false) String actionParam,
                                   @RequestParam(value = "id", required = false) Integer scheduleIdParam,
                                   HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            logger.warn("User not logged in, redirecting to login.");
            return "redirect:/login"; // 假设登录页的路径是 /login
        }
        logger.info("User '{}' accessing schedule page. Action: {}, ID: {}", loggedInUser.getUsername(), actionParam, scheduleIdParam);

        String viewName = "schedule"; // 对应 /WEB-INF/jsp/schedule.jsp (或其他配置的视图路径)

        try {
            if ("edit".equals(actionParam) && scheduleIdParam != null) {
                logger.info("Attempting to edit schedule ID: {}", scheduleIdParam);
                Schedule scheduleToEdit = scheduleService.getScheduleByIdAndUserId(scheduleIdParam, loggedInUser.getUserId());
                if (scheduleToEdit != null) {
                    model.addAttribute("scheduleToEdit", scheduleToEdit);
                    logger.info("Schedule ID {} found for editing.", scheduleIdParam);
                } else {
                    model.addAttribute("errorMessage", "找不到要编辑的日程或无权访问。");
                    logger.warn("Schedule ID {} not found or user '{}' has no access.", scheduleIdParam, loggedInUser.getUsername());
                }
            }
            // 总是加载日程列表 (即使在编辑模式下，JSP也显示列表)
            List<Schedule> schedules = scheduleService.getSchedulesByUserId(loggedInUser.getUserId());
            model.addAttribute("schedules", schedules);
            logger.info("Loaded {} schedules for user '{}'.", schedules.size(), loggedInUser.getUsername());

        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "无效的日程 ID 格式。");
            logger.error("Invalid schedule ID format in GET request.", e);
            // 即使出错，也尝试加载列表
            try {
                List<Schedule> schedules = scheduleService.getSchedulesByUserId(loggedInUser.getUserId());
                model.addAttribute("schedules", schedules);
            } catch (Exception ex) {
                logger.error("Error loading schedules after NumberFormatException.", ex);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "加载日程信息时出错：" + e.getMessage());
            logger.error("Error loading schedule information in GET request.", e);
        }

        // 处理来自 POST 重定向的 flash messages (模仿Servlet的session flash)
        // Spring MVC的RedirectAttributes更优雅，但为了模仿，我们用session
        String successMessage = (String) session.getAttribute("scheduleSuccessMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("scheduleSuccessMessage");
            logger.debug("Displayed success message from session: {}", successMessage);
        }
        String errorMessageFromPost = (String) session.getAttribute("scheduleErrorMessage");
        if (errorMessageFromPost != null) {
            // 合并或优先显示POST的错误消息
            String currentErrorMessage = (String) model.getAttribute("errorMessage");
            model.addAttribute("errorMessage", (currentErrorMessage == null ? "" : currentErrorMessage + " ") + errorMessageFromPost);
            session.removeAttribute("scheduleErrorMessage");
            logger.debug("Displayed error message from session: {}", errorMessageFromPost);
        }

        return viewName;
    }

    // 模仿 Servlet 的 doPost 方法
    @PostMapping
    public String handleScheduleAction(
            @RequestParam(value = "action", required = false, defaultValue = "add") String formAction, // 默认为 "add"
            // 所有可能的表单字段，与JSP的name属性匹配
            @RequestParam(value = "scheduleId", required = false) Integer scheduleId,
            @RequestParam(value = "title", required = false) String title, // 标记为非必须，以便在方法内部校验
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "startTime", required = false) String startTimeStr,
            @RequestParam(value = "endTime", required = false) String endTimeStr,
            @RequestParam(value = "remindTime", required = false) String remindTimeStr,
            @RequestParam(value = "repeatRule", required = false) String repeatRule,
            HttpSession session,
            Model model) { // Model用于在验证失败时不重定向，而是返回视图（如果需要）
        // 但这里我们严格模仿Servlet，总是重定向

        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            logger.warn("User not logged in during POST, sending error.");
            // Servlet返回SC_UNAUTHORIZED，Spring中可以配置全局异常处理器或直接重定向
            session.setAttribute("scheduleErrorMessage", "用户未登录，请重新登录后再操作。");
            return "redirect:/schedule"; // 或者 "redirect:/login"
        }
        logger.info("User '{}' performing POST action: {}. Schedule ID (if any): {}", loggedInUser.getUsername(), formAction, scheduleId);

        String successFlashMessage = null;
        String errorFlashMessage = null;

        try {
            switch (formAction) {
                case "add":
                case "update": // 添加和更新共享大部分创建逻辑
                    // 基本校验 (JSP的required属性是客户端的，服务器端也需要校验)
                    if (title == null || title.trim().isEmpty()) {
                        errorFlashMessage = "标题不能为空。";
                        break;
                    }
                    if (startTimeStr == null || startTimeStr.isEmpty()) {
                        errorFlashMessage = "开始时间不能为空。";
                        break;
                    }
                    if (endTimeStr == null || endTimeStr.isEmpty()) {
                        errorFlashMessage = "结束时间不能为空。";
                        break;
                    }
                    // 对于更新，scheduleId 必须存在
                    if ("update".equals(formAction) && (scheduleId == null)) {
                        errorFlashMessage = "更新日程失败，缺少日程ID。";
                        break;
                    }

                    Schedule schedule = new Schedule();
                    schedule.setUserId(loggedInUser.getUserId());
                    schedule.setTitle(title);
                    schedule.setType(type != null ? type : "学习"); // 给个默认值
                    schedule.setDescription(description);
                    schedule.setRepeatRule(repeatRule);

                    // 解析时间
                    schedule.setStartTime(Timestamp.valueOf(LocalDateTime.parse(startTimeStr, DATETIME_FORMATTER)));
                    schedule.setEndTime(Timestamp.valueOf(LocalDateTime.parse(endTimeStr, DATETIME_FORMATTER)));
                    if (remindTimeStr != null && !remindTimeStr.isEmpty()) {
                        schedule.setRemindTime(Timestamp.valueOf(LocalDateTime.parse(remindTimeStr, DATETIME_FORMATTER)));
                    }

                    // 校验结束时间不早于开始时间
                    if (schedule.getEndTime().before(schedule.getStartTime())) {
                        errorFlashMessage = "结束时间不能早于开始时间。";
                        break;
                    }

                    if ("add".equals(formAction)) {
                        logger.info("Attempting to add schedule: {}", schedule.getTitle());
                        if (scheduleService.addSchedule(schedule)) {
                            successFlashMessage = "日程 '" + schedule.getTitle() + "' 添加成功！";
                            logger.info("Schedule '{}' added successfully.", schedule.getTitle());
                        } else {
                            errorFlashMessage = "添加日程失败，请检查输入或稍后再试。";
                            logger.error("Failed to add schedule '{}'.", schedule.getTitle());
                        }
                    } else { // "update"
                        schedule.setScheduleId(scheduleId); // 确保ID已设置
                        logger.info("Attempting to update schedule ID: {}", schedule.getScheduleId());
                        if (scheduleService.updateSchedule(schedule)) {
                            successFlashMessage = "日程 '" + schedule.getTitle() + "' 更新成功！";
                            logger.info("Schedule ID {} updated successfully.", schedule.getScheduleId());
                        } else {
                            errorFlashMessage = "更新日程失败或无权操作。";
                            logger.error("Failed to update schedule ID {} or no access.", schedule.getScheduleId());
                        }
                    }
                    break;

                case "delete":
                    if (scheduleId == null) {
                        errorFlashMessage = "删除日程失败，缺少日程ID。";
                        break;
                    }
                    logger.info("Attempting to delete schedule ID: {}", scheduleId);
                    if (scheduleService.deleteSchedule(scheduleId, loggedInUser.getUserId())) {
                        successFlashMessage = "日程删除成功！";
                        logger.info("Schedule ID {} deleted successfully.", scheduleId);
                    } else {
                        errorFlashMessage = "删除日程失败或无权操作。";
                        logger.error("Failed to delete schedule ID {} or no access.", scheduleId);
                    }
                    break;

                default:
                    errorFlashMessage = "无效的操作请求：" + formAction;
                    logger.warn("Invalid action received in POST: {}", formAction);
            }
        } catch (DateTimeParseException e) {
            String fieldName = "未知日期";
            if (e.getParsedString().equals(startTimeStr)) fieldName = "开始时间";
            else if (e.getParsedString().equals(endTimeStr)) fieldName = "结束时间";
            else if (e.getParsedString().equals(remindTimeStr)) fieldName = "提醒时间";
            errorFlashMessage = fieldName + "的格式无效。请使用 'yyyy-MM-ddTHH:mm' 格式。";
            logger.error("DateTimeParseException during POST action {}: {}", formAction, e.getMessage());
        } catch (NumberFormatException e) {
            errorFlashMessage = "无效的 ID 格式。";
            logger.error("NumberFormatException during POST action {}: {}", formAction, e.getMessage());
        } catch (Exception e) {
            errorFlashMessage = "处理日程操作时发生未知错误：" + e.getMessage();
            logger.error("Unexpected exception during POST action " + formAction, e);
        }

        // 存储 flash 消息到 session
        if (successFlashMessage != null) {
            session.setAttribute("scheduleSuccessMessage", successFlashMessage);
        }
        if (errorFlashMessage != null) {
            session.setAttribute("scheduleErrorMessage", errorFlashMessage);
        }

        // 总是重定向回 GET /schedule (模仿Servlet行为)
        return "redirect:/schedule";
    }
}