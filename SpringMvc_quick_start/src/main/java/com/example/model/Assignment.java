package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp; // 使用 java.sql.Timestamp for DATETIME

@Data // Lombok: @ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    private int assignmentId;
    private int courseId;
    private String courseName; // 从关联的 courses 表获取，方便显示
    private String title;
    private String description;
    private Timestamp deadline; // DATETIME 对应 Timestamp
    private Integer maxScore;   // INT 可以为 null，使用 Integer
    private Timestamp createdAt;
}
