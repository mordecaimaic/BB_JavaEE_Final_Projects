package com.example.service.impl;

import com.example.mapper.MaterialMapper;
import com.example.model.Material;
import com.example.model.User;
import com.example.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // 用于注入配置属性
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct; // 用于初始化方法
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class MaterialServiceImpl implements MaterialService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialServiceImpl.class);
    private final MaterialMapper materialMapper;

    // 配置文件存储路径，可以配置在 application.properties 或直接硬编码 (不推荐硬编码)
    // 例如，在 src/main/resources/application.properties (如果使用 Spring Boot)
    // material.upload-dir=/path/to/your/uploads/materials
    // 如果不是Spring Boot，可以在XML中配置一个bean或直接在这里定义
    // @Value("${material.upload-dir}")
    private String uploadDir; // 将在构造函数或初始化方法中设置

    private Path uploadPath;

    @Autowired
    public MaterialServiceImpl(MaterialMapper materialMapper,
                               @Value("${material.upload.dir:/default/upload/path}") String configuredUploadDir) { // 从属性文件读取或使用默认值
        this.materialMapper = materialMapper;
        // 优先使用配置的路径，如果为空，则使用基于项目部署的相对路径 (需要ServletContext)
        // 由于Service层通常不直接访问ServletContext，路径最好外部配置或在Web层处理后传入
        // 此处简化：假设路径已正确配置或在初始化时确定
        this.uploadDir = configuredUploadDir;
        if (this.uploadDir.equals("/default/upload/path") || this.uploadDir.isEmpty()) {
            // 这是一个非常临时的备用方案，实际应用中应该有明确的配置
            // 在非web环境或测试中，getRealPath可能返回null
            // String webappRoot = System.getProperty("catalina.base"); // Tomcat specific, not always reliable
            // if(webappRoot != null) {
            // this.uploadDir = webappRoot + File.separator + "webapps" + File.separator + "your_app_name_uploads" + File.separator + "materials";
            // } else {
            // For non-server environments or if real path isn't available:
            this.uploadDir = System.getProperty("java.io.tmpdir") + File.separator + "campus_assistant_uploads" + File.separator + "materials";
            logger.warn("Material upload directory not configured, using temporary directory: {}", this.uploadDir);
            // }
        }
        this.uploadPath = Paths.get(this.uploadDir);
        initializeUploadDirectory();
    }

    @PostConstruct // 或者在构造函数中调用
    public void initializeUploadDirectory() {
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created material upload directory: {}", uploadPath.toAbsolutePath());
            } else {
                logger.info("Material upload directory already exists: {}", uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Could not create material upload directory: {}", uploadPath.toAbsolutePath(), e);
            // 考虑抛出运行时异常，因为文件上传功能会受影响
            throw new RuntimeException("Could not initialize material upload directory", e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<Material> getAllMaterialsWithDetails() {
        return materialMapper.getAllMaterialsWithDetails();
    }

    @Override
    @Transactional(readOnly = true)
    public Material getMaterialById(int materialId) {
        return materialMapper.findMaterialById(materialId);
    }

    @Override
    @Transactional
    public boolean addMaterial(Material materialDetails, MultipartFile file, User uploader) throws IOException {
        if (file.isEmpty()) {
            logger.warn("Uploaded file is empty for material: {}", materialDetails.getFileName());
            return false;
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.lastIndexOf('.') > 0) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        }
        String uniqueFileName = UUID.randomUUID().toString() + (originalFileName != null && originalFileName.lastIndexOf('.') > 0 ? originalFileName.substring(originalFileName.lastIndexOf('.')) : "");
        long fileSize = file.getSize(); // Bytes

        Path destinationFile = this.uploadPath.resolve(uniqueFileName).normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(this.uploadPath.toAbsolutePath())) {
            // Security check
            throw new IOException("Cannot store file outside current directory.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Successfully uploaded file: {} to path: {}", originalFileName, destinationFile);
        } catch (IOException e) {
            logger.error("Failed to store file {} for material upload.", originalFileName, e);
            throw new IOException("Failed to store file " + originalFileName, e); // Re-throw to be handled by controller
        }

        materialDetails.setUploaderId(uploader.getUserId());
        materialDetails.setFileName(originalFileName); // 原始文件名
        materialDetails.setFilePath(uniqueFileName);   // 服务器上存储的唯一文件名
        materialDetails.setFileType(fileExtension);
        materialDetails.setFileSize((int) Math.ceil(fileSize / 1024.0)); // KB
        materialDetails.setUploadTime(new Timestamp(System.currentTimeMillis()));
        // courseId 和 description 应该由 Controller 填充到 materialDetails 对象中

        return materialMapper.insertMaterial(materialDetails) > 0;
    }

    @Override
    @Transactional
    public boolean incrementDownloadCount(int materialId) {
        return materialMapper.incrementDownloadCount(materialId) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMaterial(int materialId, int currentUserId) {
        Material material = materialMapper.findMaterialById(materialId);
        if (material == null) {
            logger.warn("Attempted to delete non-existent material with ID: {}", materialId);
            return false;
        }
        // 权限校验：例如，只有上传者或管理员可以删除
        // User uploader = userService.findById(material.getUploaderId()); // 假设有UserService
        // User currentUser = userService.findById(currentUserId);
        // if (material.getUploaderId() != currentUserId && !(currentUser != null && "admin".equals(currentUser.getRole()))) {
        //     logger.warn("User {} does not have permission to delete material ID: {}", currentUserId, materialId);
        //     return false;
        // }

        // 暂不实现复杂权限，仅校验上传者
        if (material.getUploaderId() != currentUserId) {
            logger.warn("User {} attempted to delete material ID: {} owned by user {}", currentUserId, materialId, material.getUploaderId());
            return false; // 或者抛出权限异常
        }


        try {
            Path filePathToDelete = this.uploadPath.resolve(material.getFilePath()).normalize();
            Files.deleteIfExists(filePathToDelete);
            logger.info("Successfully deleted file from filesystem: {}", filePathToDelete);
        } catch (IOException e) {
            logger.error("Failed to delete file from filesystem: {}. Material ID: {}", material.getFilePath(), materialId, e);
            // 根据业务决定是否继续删除数据库记录，或者回滚/标记为错误
            // return false; // 如果文件删除失败则不删除数据库记录
        }

        return materialMapper.deleteMaterialById(materialId) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getMaterialsByCourseId(int courseId) {
        return materialMapper.findMaterialsByCourseId(courseId);
    }

    @Override
    public String getMaterialPhysicalPath(Material material) {
        if (material == null || material.getFilePath() == null) {
            return null;
        }
        return this.uploadPath.resolve(material.getFilePath()).normalize().toAbsolutePath().toString();
    }
}