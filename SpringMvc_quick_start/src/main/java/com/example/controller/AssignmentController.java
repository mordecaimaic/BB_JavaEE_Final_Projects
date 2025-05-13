package com.example.controller; // 你的Spring MVC项目包结构

import com.example.model.AssignmentWithSubmission;
import com.example.model.Submission;
import com.example.model.User;
import com.example.service.AssignmentService;
import jakarta.servlet.ServletContext; // 引入 ServletContext
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
// import java.nio.file.Paths; // 可以不需要，直接用 File 构造
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final String finalUploadDir; // 最终计算得到的绝对上传路径

    @Autowired
    public AssignmentController(AssignmentService assignmentService, ServletContext servletContext) {
        this.assignmentService = assignmentService;

        // 定义相对于 webapp 根目录的路径
        // 文件将保存在 webapp部署目录/uploads/assignments/ 目录下
        String relativePath = File.separator + "uploads" + File.separator + "assignments";

        // 获取部署后 webapp 在服务器上的真实文件系统根路径
        String webappRootPath = servletContext.getRealPath("/");

        if (webappRootPath != null) {
            this.finalUploadDir = webappRootPath + relativePath;
        } else {
            // 如果 getRealPath("/") 返回 null (例如部署为未解压的WAR，或某些服务器配置)
            // 这种情况对于相对路径存储是致命的，这里提供一个非常临时的备用方案
            // 在实际开发中，你需要更优雅地处理这种情况或确保应用总是解压部署
            System.err.println("CRITICAL: Could not determine webapp real path. Upload directory might not be set correctly using relative path.");
            // 为了让程序不完全崩溃，可以设置一个临时目录，但这非常不推荐用于实际场景
            this.finalUploadDir = System.getProperty("java.io.tmpdir") + File.separator + "spring_mvc_uploads" + relativePath;
            System.err.println("Falling back to temporary upload directory: " + this.finalUploadDir);
        }

        // 确保上传目录存在
        File uploadDirFile = new File(this.finalUploadDir);
        if (!uploadDirFile.exists()) {
            if (uploadDirFile.mkdirs()) {
                System.out.println("AssignmentController: Upload directory created at: " + this.finalUploadDir);
            } else {
                System.err.println("AssignmentController: Could not create upload directory at: " + this.finalUploadDir + ". Check permissions or path.");
            }
        }
        System.out.println("AssignmentController initialized. Upload directory set to: " + this.finalUploadDir);
    }

    @GetMapping
    public String listAssignments(HttpSession session, Model model) {
        System.out.println("AssignmentController: Processing GET /assignments request...");
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            System.out.println("AssignmentController: User not logged in, redirecting to /login");
            return "redirect:/login";
        }
        // 确保userId被正确加载
        if (loggedInUser.getUserId() == 0) {
            System.err.println("AssignmentController: Logged in user ID is 0. Check user loading from session/database.");
            model.addAttribute("errorMessage", "无法获取有效的用户信息，请重新登录。");
            return "assignments"; // 或者重定向到错误页/登录页
        }


        try {
            System.out.println("AssignmentController: Fetching assignments for user ID: " + loggedInUser.getUserId());
            List<AssignmentWithSubmission> assignmentsInfo = assignmentService.getAssignmentsWithSubmissionsForUser(loggedInUser.getUserId());
            model.addAttribute("assignmentsInfo", assignmentsInfo);
            System.out.println("AssignmentController: Fetched " + (assignmentsInfo != null ? assignmentsInfo.size() : 0) + " assignment(s).");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "加载作业信息时出错，请稍后重试。");
            System.err.println("AssignmentController (GET): Error fetching assignments: " + e.getMessage());
            e.printStackTrace(); // 使用日志框架记录完整错误
        }

        System.out.println("AssignmentController: Returning 'assignments' view.");
        return "assignments";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("assignmentId") int assignmentId,
                                   @RequestParam("file") MultipartFile file,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("AssignmentController: Processing POST /assignments/submit request...");
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("uploadErrorMessage", "用户未登录，无法提交作业。");
            System.out.println("AssignmentController (POST): User not logged in.");
            return "redirect:/login";
        }
        // 确保userId被正确加载
        if (loggedInUser.getUserId() == 0) {
            System.err.println("AssignmentController (POST): Logged in user ID is 0. Cannot submit assignment.");
            redirectAttributes.addFlashAttribute("uploadErrorMessage", "无法获取有效的用户信息，无法提交作业。");
            return "redirect:/assignments";
        }


        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("uploadErrorMessage", "未选择上传文件或文件为空。");
            System.out.println("AssignmentController (POST): No file uploaded or file is empty.");
            return "redirect:/assignments";
        }

        String originalFileName = file.getOriginalFilename();
        String submittedFileNameForDB = null; // 用于存储在数据库中的文件名或相对路径
        File savedFileOnDisk = null;       // 上传到磁盘的实际文件对象

        try {
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // 目标保存文件
            savedFileOnDisk = new File(this.finalUploadDir, uniqueFileName);

            System.out.println("AssignmentController (POST): Attempting to save file to: " + savedFileOnDisk.getAbsolutePath());
            file.transferTo(savedFileOnDisk); // 保存文件到磁盘
            System.out.println("AssignmentController (POST): File saved successfully: " + savedFileOnDisk.getName());

            // 决定存储在数据库中的路径/文件名
            // 对于测试，只存文件名可能足够，结合 this.finalUploadDir 可以找到它
            // 如果 this.finalUploadDir 是部署目录内的，那么这个文件名在重新部署后也意义不大了
            submittedFileNameForDB = uniqueFileName;

            Submission submission = new Submission();
            submission.setAssignmentId(assignmentId);
            submission.setUserId(loggedInUser.getUserId());
            submission.setFilePath(submittedFileNameForDB); // 存储文件名或相对标识

            if (assignmentService.upsertSubmission(submission)) {
                redirectAttributes.addFlashAttribute("uploadSuccessMessage", "作业 '" + originalFileName + "' 提交成功！");
                System.out.println("AssignmentController (POST): Submission for user " + loggedInUser.getUsername() + " saved to database.");
            } else {
                redirectAttributes.addFlashAttribute("uploadErrorMessage", "提交作业失败，无法更新数据库记录。");
                System.err.println("AssignmentController (POST): Failed to save submission to database.");
                // 如果数据库操作失败，删除已上传的文件
                if (savedFileOnDisk != null && savedFileOnDisk.exists()) {
                    Files.deleteIfExists(savedFileOnDisk.toPath());
                    System.out.println("AssignmentController (POST): Deleted uploaded file due to DB error: " + savedFileOnDisk.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("uploadErrorMessage", "文件上传IO操作失败：" + e.getMessage());
            System.err.println("AssignmentController (POST): IOException during file upload: " + e.getMessage());
            e.printStackTrace();
            // 如果文件已部分创建或创建失败，尝试删除
            if (savedFileOnDisk != null && savedFileOnDisk.exists()) {
                try { Files.deleteIfExists(savedFileOnDisk.toPath()); } catch (IOException ex) { System.err.println("Error deleting partial file: " + ex.getMessage());}
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("uploadErrorMessage", "提交过程中发生未知错误：" + e.getMessage());
            System.err.println("AssignmentController (POST): Unknown error during submission: " + e.getMessage());
            e.printStackTrace();
            if (savedFileOnDisk != null && savedFileOnDisk.exists()) {
                try { Files.deleteIfExists(savedFileOnDisk.toPath()); } catch (IOException ex) { System.err.println("Error deleting partial file on unknown error: " + ex.getMessage());}
            }
        }
        return "redirect:/assignments";
    }
}