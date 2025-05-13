package com.example.service;

import com.example.model.AssignmentWithSubmission;
import com.example.model.Submission;
import java.util.List;

public interface AssignmentService {
    List<AssignmentWithSubmission> getAssignmentsWithSubmissionsForUser(int userId);
    boolean upsertSubmission(Submission submission);
    // 其他作业相关业务方法
}