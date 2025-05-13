<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- 引入 JSTL fmt 库用于日期格式化 --%>
<%@ page import="com.campus.assistant.model.User" %>
<%@ page import="java.sql.Timestamp" %> <%-- 引入 Timestamp 用于比较 --%>
<%@ page import="java.util.Date" %> <%-- 引入 Date 用于比较 --%>

<%-- 1. 登录状态检查 --%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    // 获取当前时间用于比较截止日期
    long now = System.currentTimeMillis();
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>作业提交 - 校园小助手</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .deadline-past { color: red; font-weight: bold; }
        .status-submitted { color: green; }
        .status-pending { color: orange; }
        .status-graded { color: blue; }
        .upload-form { margin-top: 0.5rem; }
        th, td { vertical-align: middle; }
    </style>
</head>
<body>

<%-- 2. 包含通用的导航栏 --%>
<jsp:include page="header.jsp" />

<%-- 3. 主体内容区域 --%>
<div class="container mt-4">
    <h2 class="mb-3">我的作业</h2>

    <%-- 显示上传成功/失败的消息 --%>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="table-responsive">
        <table class="table table-striped table-hover table-bordered">
            <thead class="table-light">
            <tr>
                <th>课程名称</th>
                <th>作业标题</th>
                <th>截止日期</th>
                <th>提交状态</th>
                <th>得分</th>
                <th>操作/详情</th>
            </tr>
            </thead>
            <tbody>
            <%-- 遍历 Servlet 传递过来的 assignmentsInfo 列表 --%>
            <c:choose>
                <c:when test="${not empty assignmentsInfo}">
                    <c:forEach var="item" items="${assignmentsInfo}">
                        <%-- 判断是否已过截止日期 --%>
                        <c:set var="deadlineTimestamp" value="${item.assignment.deadline}"/>
                        <c:set var="isPastDeadline" value="${deadlineTimestamp != null && deadlineTimestamp.getTime() < now}"/>

                        <tr>
                            <td><c:out value="${item.assignment.courseName}" /></td>
                            <td>
                                <c:out value="${item.assignment.title}" />
                                <c:if test="${not empty item.assignment.description}">
                                    <small class="d-block text-muted" title="作业描述">
                                        描述: <c:out value="${item.assignment.description}" />
                                    </small>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${deadlineTimestamp != null}">
                                            <span class="${isPastDeadline ? 'deadline-past' : ''}">
                                                <fmt:formatDate value="${deadlineTimestamp}" pattern="yyyy-MM-dd HH:mm" />
                                            </span>
                                        <c:if test="${isPastDeadline}"> (已截止)</c:if>
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                    <%-- 显示提交状态 --%>
                                <c:choose>
                                    <c:when test="${item.submission != null}">
                                            <span class="status-${item.submission.status == '已提交' ? 'submitted' : (item.submission.score != null ? 'graded' : 'submitted')}">
                                                <c:out value="${item.submission.status}" />
                                                <c:if test="${item.submission.status == '已提交'}">
                                                    <small class="d-block text-muted">
                                                        @ <fmt:formatDate value="${item.submission.submitTime}" pattern="MM-dd HH:mm"/>
                                                    </small>
                                                </c:if>
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pending">${isPastDeadline ? '已逾期未交' : '未提交'}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                    <%-- 显示得分 --%>
                                <c:choose>
                                    <c:when test="${item.submission != null && item.submission.score != null}">
                                        <c:out value="${item.submission.score}" /> / <c:out value="${item.assignment.maxScore != null ? item.assignment.maxScore : '-'}" />
                                        <c:if test="${not empty item.submission.feedback}">
                                            <small class="d-block text-muted" title="教师评语">
                                                反馈: <c:out value="${item.submission.feedback}"/>
                                            </small>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${item.submission != null}">
                                        - <%-- 已提交但未评分 --%>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise> <%-- 未提交 --%>
                                </c:choose>
                            </td>
                            <td>
                                    <%-- 文件上传表单 --%>
                                    <%-- 只有在截止日期前或允许逾期提交时才显示上传表单 --%>
                                <c:if test="${!isPastDeadline || true}"> <%--  根据需要修改这里的逻辑，例如允许逾期提交则设为 true --%>
                                    <form action="${pageContext.request.contextPath}/assignments" method="post" enctype="multipart/form-data" class="upload-form">
                                            <%-- 隐藏字段传递作业 ID --%>
                                        <input type="hidden" name="assignmentId" value="${item.assignment.assignmentId}">
                                        <div class="input-group input-group-sm">
                                            <input type="file" class="form-control" name="file" required
                                                   id="fileInput-${item.assignment.assignmentId}" <%-- 给个唯一ID方便JS操作(如果需要)--%>
                                                   <c:if test="${isPastDeadline}">disabled title="已过截止日期"</c:if> <%-- 如果不允许逾期提交，禁用 --%>
                                            >
                                            <button type="submit" class="btn btn-primary"
                                                    <c:if test="${isPastDeadline}">disabled</c:if> <%-- 如果不允许逾期提交，禁用 --%>
                                            >
                                                <c:choose>
                                                    <c:when test="${item.submission != null}">重新提交</c:when>
                                                    <c:otherwise>提交</c:otherwise>
                                                </c:choose>
                                            </button>
                                        </div>
                                        <c:if test="${item.submission != null && not empty item.submission.filePath}">
                                            <small class="text-muted d-block mt-1">
                                                已提交: <a href="#" title="这里可以链接到下载（需后端实现）">${item.submission.filePath}</a> <%-- 显示已提交的文件名/路径 --%>
                                            </small>
                                        </c:if>
                                    </form>
                                </c:if>
                                <c:if test="${isPastDeadline && false}"> <%-- 如果不允许逾期提交，显示提示 --%>
                                    <span class="text-danger d-block mt-1">已过截止日期</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="6" class="text-center text-muted">当前没有需要提交的作业。</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

    <div class="mt-4 text-center">
        <a href="dashboard.jsp" class="btn btn-secondary">返回仪表板</a>
    </div>

</div> <%-- End of .container --%>

<%-- 引入 Bootstrap JS --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>