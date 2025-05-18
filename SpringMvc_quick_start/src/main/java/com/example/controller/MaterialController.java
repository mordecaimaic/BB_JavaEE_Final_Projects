package com.example.controller;

import com.example.model.Course;
import com.example.model.Material;
import com.example.model.User;
import com.example.service.CourseService;
import com.example.service.MaterialService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/materials") // 类级别的 @RequestMapping 保持不变
public class MaterialController {

    private static final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    private final MaterialService materialService;
    private final CourseService courseService;

    @Autowired
    public MaterialController(MaterialService materialService, CourseService courseService) {
        this.materialService = materialService;
        this.courseService = courseService;
    }

    @GetMapping // 这个方法处理 GET /materials 请求 (显示列表和上传表单)
    public String listMaterials(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            logger.warn("User not logged in, redirecting to login.");
            return "redirect:/login";
        }

        try {
            List<Material> materials = materialService.getAllMaterialsWithDetails();
            model.addAttribute("materials", materials);

            List<Course> courses = courseService.getAllAvailableCourses();
            model.addAttribute("courses", courses);

            // 为上传表单准备一个对象，如果JSP使用Spring Form标签会用到
            // 如果JSP不使用Spring Form标签，这行对于普通字段不是必需的
            model.addAttribute("newMaterial", new Material());

        } catch (Exception e) {
            logger.error("Error loading materials page", e);
            model.addAttribute("errorMessage", "加载资料信息时出错: " + e.getMessage());
        }
        return "materials"; // JSP文件名
    }

    // 修改这里：将 @PostMapping("/upload") 改为 @PostMapping
    // 现在这个方法会处理发送到 /materials 的 POST 请求
    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("courseId") Integer courseId,
                                   @RequestParam(value = "description", required = false) String description,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        logger.info("POST /materials (upload) - Received file: {}, courseId: {}, description: {}",
                file.getOriginalFilename(), courseId, description);

        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "请先登录再上传文件。");
            return "redirect:/login";
        }

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "上传文件不能为空。");
            logger.warn("File upload attempt failed: File is empty.");
            return "redirect:/materials";
        }
        if (courseId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "必须选择一个课程。");
            logger.warn("File upload attempt failed: Course ID is null.");
            return "redirect:/materials";
        }

        Material materialDetails = new Material();
        materialDetails.setCourseId(courseId);
        materialDetails.setDescription(description);
        // uploaderId, fileName, filePath, fileType, fileSize, uploadTime 将在 service 层设置

        try {
            boolean success = materialService.addMaterial(materialDetails, file, loggedInUser);
            if (success) {
                logger.info("File '{}' uploaded successfully by user '{}'.", file.getOriginalFilename(), loggedInUser.getUsername());
                redirectAttributes.addFlashAttribute("successMessage", "文件 '" + file.getOriginalFilename() + "' 上传成功！");
            } else {
                logger.error("File upload failed for user '{}', material service returned false.", loggedInUser.getUsername());
                redirectAttributes.addFlashAttribute("errorMessage", "文件上传失败（业务逻辑处理失败）。");
            }
        } catch (IOException e) {
            logger.error("IOException during file upload for user: {}", loggedInUser.getUsername(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "文件上传处理失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred during file upload for user: {}", loggedInUser.getUsername(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "上传过程中发生未知错误。");
        }
        return "redirect:/materials"; // 重定向回列表页面 (GET /materials)
    }

    @GetMapping("/download/{materialId}")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Integer materialId, HttpSession session) { // HttpServletResponse 通常不需要了
        logger.info("GET /materials/download/{}", materialId);
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            logger.warn("Unauthorized download attempt for material ID: {}, user not logged in.", materialId);
            return ResponseEntity.status(401).build();
        }

        if (materialId == null) {
            logger.warn("Download attempt with null material ID.");
            return ResponseEntity.badRequest().build();
        }

        Material material = materialService.getMaterialById(materialId);
        if (material == null) {
            logger.warn("Attempt to download non-existent material ID: {}", materialId);
            return ResponseEntity.notFound().build();
        }

        String physicalPath = materialService.getMaterialPhysicalPath(material);
        if (physicalPath == null) {
            logger.error("Physical path not found for material ID: {}", materialId);
            return ResponseEntity.status(500).build();
        }

        File file = new File(physicalPath);
        if (!file.exists() || !file.canRead()) {
            logger.error("File not found or not readable at path: {} for material ID: {}", physicalPath, materialId);
            return ResponseEntity.status(404).build();
        }

        Resource resource = new FileSystemResource(file);

        materialService.incrementDownloadCount(materialId); // 尝试增加下载计数

        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(material.getFileName(), StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        } catch (Exception e) {
            logger.warn("Could not encode filename: {}, using original. Error: {}", material.getFileName(), e.getMessage());
            encodedFileName = material.getFileName().replaceAll("[^a-zA-Z0-9._-]+", "_"); // Fallback
        }

        logger.info("Providing file '{}' for download. Physical path: {}", material.getFileName(), physicalPath);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                .contentLength(file.length())
                .body(resource);
    }


    @GetMapping("/delete/{materialId}")
    public String deleteMaterial(@PathVariable Integer materialId, HttpSession session, RedirectAttributes redirectAttributes) {
        logger.info("GET /materials/delete/{}", materialId);
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "请先登录。");
            return "redirect:/login";
        }
        if (materialId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "无效的资料ID。");
            return "redirect:/materials";
        }

        boolean success = materialService.deleteMaterial(materialId, loggedInUser.getUserId());
        if (success) {
            logger.info("Material ID: {} deleted successfully by user ID: {}", materialId, loggedInUser.getUserId());
            redirectAttributes.addFlashAttribute("successMessage", "资料删除成功！");
        } else {
            logger.warn("Failed to delete material ID: {} by user ID: {} (or no permission).", materialId, loggedInUser.getUserId());
            redirectAttributes.addFlashAttribute("errorMessage", "删除资料失败或无权操作。");
        }
        return "redirect:/materials";
    }
}