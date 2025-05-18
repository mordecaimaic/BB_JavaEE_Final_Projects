package com.example.model; // 修改包名

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    private int materialId;
    private int courseId;
    private String courseName; // 关联课程名称
    private int uploaderId;
    private String uploaderName; // 上传者用户名
    private String fileName;    // 上传时的原始文件名
    private String filePath;    // 服务器上存储的路径或唯一文件名
    private String fileType;    // 文件类型
    private int fileSize;     // 文件大小 (KB)
    private String description;
    private Timestamp uploadTime;
    private int downloadCount;
}