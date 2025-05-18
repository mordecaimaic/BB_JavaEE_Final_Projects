package com.example.mapper;

import com.example.model.Material;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MaterialMapper {
    List<Material> getAllMaterialsWithDetails();
    Material findMaterialById(@Param("materialId") int materialId);
    int insertMaterial(Material material);
    int incrementDownloadCount(@Param("materialId") int materialId);
    int deleteMaterialById(@Param("materialId") int materialId); // 假设只根据ID删除数据库记录
    // 如果需要根据 courseId 获取资料
    List<Material> findMaterialsByCourseId(@Param("courseId") int courseId);
}