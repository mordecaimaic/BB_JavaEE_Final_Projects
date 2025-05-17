package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data // Lombok: @ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private int scheduleId;
    private int userId; // 该日程所属的用户ID
    private String title;
    private String description;
    private Timestamp startTime; // DATETIME 对应 Timestamp
    private Timestamp endTime;   // DATETIME 对应 Timestamp
    private String type = "学习"; // 默认值 '学习'
    private Timestamp remindTime; // 可选的提醒时间
    private String repeatRule;   // 可选的重复规则 (暂不实现复杂逻辑)
}