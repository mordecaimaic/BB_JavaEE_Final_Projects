package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    private int submissionId;
    private int assignmentId;
    private int userId;
    private String filePath; // 存储文件在服务器上的相对或绝对路径
    private Timestamp submitTime;
    private String status; // ENUM 类型映射为 String
    private Integer score;
    private String feedback;
}
