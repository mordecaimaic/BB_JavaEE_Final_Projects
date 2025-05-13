package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentWithSubmission {
    private Assignment assignment; // 作业详情
    private Submission submission; // 当前用户的提交信息 (可能为 null)
}
