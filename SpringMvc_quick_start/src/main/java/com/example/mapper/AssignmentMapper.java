package com.example.mapper; // 假设你的Mapper接口都在这个包下

import com.example.model.AssignmentWithSubmission;
import com.example.model.Submission;
import org.apache.ibatis.annotations.Param; // 如果有多个参数，建议使用 @Param

import java.util.List;

public interface AssignmentMapper {

    /**
     * 根据用户ID获取该用户所有作业及其提交情况。
     * 对应的SQL在 AssignmentMapper.xml 中 id="getAssignmentsWithSubmissionsForUser"
     * @param userId 用户ID
     * @return 包含作业和对应提交信息的列表
     */
    List<AssignmentWithSubmission> getAssignmentsWithSubmissionsForUser(int userId);

    /**
     * 插入或更新作业提交记录。
     * 对应的SQL在 AssignmentMapper.xml 中 id="upsertSubmission"
     * (或者你可能将其拆分为 insertSubmission 和 updateSubmission)
     * @param submission 提交对象
     * @return 返回受影响的行数，通常用于判断操作是否成功
     */
    int upsertSubmission(Submission submission);

    /**
     * (可选) 如果你将 upsert 分解为 insert 和 update，可以定义单独的方法：
     */
    // int insertSubmission(Submission submission);
    // int updateSubmission(Submission submission);

    /**
     * (可选) 根据作业ID和用户ID查询单个提交记录，用于判断是插入还是更新
     * @param assignmentId 作业ID
     * @param userId 用户ID
     * @return 提交记录，如果不存在则返回 null
     */
    Submission findSubmissionByAssignmentAndUser(@Param("assignmentId") int assignmentId, @Param("userId") int userId);

    // 你可以根据 AssignmentDAO 中的其他方法，在这里添加对应的接口方法
    // 例如，如果 AssignmentDAO 有获取所有作业的方法:
    // List<Assignment> getAllAssignments();

    // 如果有根据课程ID获取作业的方法:
    // List<Assignment> getAssignmentsByCourseId(int courseId);
}