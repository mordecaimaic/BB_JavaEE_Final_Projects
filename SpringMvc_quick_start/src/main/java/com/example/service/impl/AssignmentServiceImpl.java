package com.example.service.impl;

import com.example.model.AssignmentWithSubmission;
import com.example.model.Submission;
// 假设你的 AssignmentDAO 迁移成了 MyBatis Mapper 接口 AssignmentMapper
// 或者你仍然使用原始的 AssignmentDAO，但需要将其声明为 Spring Bean
import com.example.mapper.AssignmentMapper; // 或 com.example.dao.AssignmentDAO
import com.example.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentMapper assignmentMapper; // 或 AssignmentDAO

    @Autowired
    public AssignmentServiceImpl(AssignmentMapper assignmentMapper) { // 或 AssignmentDAO
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentWithSubmission> getAssignmentsWithSubmissionsForUser(int userId) {
        System.out.println("AssignmentServiceImpl: 调用 assignmentMapper.getAssignmentsWithSubmissionsForUser for userId: " + userId);
        return assignmentMapper.getAssignmentsWithSubmissionsForUser(userId);
    }

    @Override
    @Transactional // 通常写操作需要事务
    public boolean upsertSubmission(Submission submission) {
        System.out.println("AssignmentServiceImpl: 调用 assignmentMapper.upsertSubmission");
        // 这里的逻辑可能需要根据你的DAO/Mapper方法调整
        // 例如，如果 upsertSubmission 返回受影响的行数
        int affectedRows = assignmentMapper.upsertSubmission(submission);
        return affectedRows > 0;
    }
}