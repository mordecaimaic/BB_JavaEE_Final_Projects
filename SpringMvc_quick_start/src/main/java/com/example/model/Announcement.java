package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    private int announcementId;
    private String title;
    private String content;
    private int publisherId;
    private String publisherName;
    private String scope;         // '全校', '院系', '班级'
    private String department;    // 如果 scope 是 '院系'
    private boolean urgent;       // 是否紧急
    private Timestamp publishTime;
}