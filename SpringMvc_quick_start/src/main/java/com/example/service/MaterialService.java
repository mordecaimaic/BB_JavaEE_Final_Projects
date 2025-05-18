package com.example.service;

import com.example.model.Material;
import com.example.model.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface MaterialService {
    List<Material> getAllMaterialsWithDetails();
    Material getMaterialById(int materialId);
    boolean addMaterial(Material material, MultipartFile file, User uploader) throws IOException;
    boolean incrementDownloadCount(int materialId);
    boolean deleteMaterial(int materialId, int currentUserId); // 业务层处理权限和文件删除
    List<Material> getMaterialsByCourseId(int courseId);

    // 获取文件存储的物理路径，用于文件下载
    String getMaterialPhysicalPath(Material material);
}