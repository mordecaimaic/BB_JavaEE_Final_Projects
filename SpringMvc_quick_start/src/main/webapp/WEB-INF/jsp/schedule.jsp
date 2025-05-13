<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.campus.assistant.model.User" %>
<%@ page import="com.campus.assistant.model.Schedule" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>

<%-- 1. 登录检查与数据准备 --%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    // 获取待编辑的日程对象
    Schedule scheduleToEdit = (Schedule) request.getAttribute("scheduleToEdit");
    boolean isEditing = (scheduleToEdit != null);

    // 定义日期时间格式化器
    final DateTimeFormatter htmlDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    // ★★★ 如果是编辑状态，预先格式化日期时间为字符串，并存入 request 作用域 ★★★
    if (isEditing) {
        Timestamp startTimeTs = scheduleToEdit.getStartTime();
        Timestamp endTimeTs = scheduleToEdit.getEndTime();
        Timestamp remindTimeTs = scheduleToEdit.getRemindTime();

        String formattedStartTime = "";
        if (startTimeTs != null) {
            formattedStartTime = startTimeTs.toLocalDateTime().format(htmlDateTimeFormatter);
        }
        String formattedEndTime = "";
        if (endTimeTs != null) {
            formattedEndTime = endTimeTs.toLocalDateTime().format(htmlDateTimeFormatter);
        }
        String formattedRemindTime = "";
        if (remindTimeTs != null) {
            formattedRemindTime = remindTimeTs.toLocalDateTime().format(htmlDateTimeFormatter);
        }

        // 将格式化后的字符串放入 request 作用域
        request.setAttribute("formattedStartTime", formattedStartTime);
        request.setAttribute("formattedEndTime", formattedEndTime);
        request.setAttribute("formattedRemindTime", formattedRemindTime);
    }
    // ★★★ 不再需要将 Function 放入 pageContext ★★★
    // pageContext.setAttribute("toHtmlDateTime", new Function<Timestamp, String>() { ... }); // 这行已删除
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人日程 - 校园小助手</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .schedule-item { border-left: 4px solid #17a2b8; margin-bottom: 1rem; }
        .schedule-item .card-body { padding: 1rem; }
        .schedule-meta { font-size: 0.9rem; color: #6c757d; }
        .form-section { background-color: #f8f9fa; padding: 1.5rem; border-radius: 5px; margin-bottom: 2rem; }
        th, td { vertical-align: middle; }
    </style>
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-4">
    <h2 class="mb-4">个人日程管理</h2>

    <%-- 显示成功/失败消息 --%>
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

    <%-- 添加/编辑日程表单区域 --%>
    <div class="form-section">
        <h4>${isEditing ? '编辑日程' : '添加新日程'}</h4>
        <form action="${pageContext.request.contextPath}/schedule" method="post">
            <input type="hidden" name="action" value="${isEditing ? 'update' : 'add'}">
            <c:if test="${isEditing}">
                <input type="hidden" name="scheduleId" value="${scheduleToEdit.scheduleId}">
            </c:if>

            <div class="row g-3">
                <div class="col-md-6">
                    <label for="title" class="form-label">标题 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="title" name="title" required
                           value="${isEditing ? scheduleToEdit.title : ''}">
                </div>
                <div class="col-md-6">
                    <label for="type" class="form-label">类型</label>
                    <select id="type" name="type" class="form-select">
                        <option value="学习" ${ (isEditing && scheduleToEdit.type == '学习') || !isEditing ? 'selected' : '' }>学习</option>
                        <option value="生活" ${ isEditing && scheduleToEdit.type == '生活' ? 'selected' : '' }>生活</option>
                    </select>
                </div>
                <div class="col-12">
                    <label for="description" class="form-label">描述</label>
                    <textarea class="form-control" id="description" name="description" rows="2">${isEditing ? scheduleToEdit.description : ''}</textarea>
                </div>
                <div class="col-md-6">
                    <label for="startTime" class="form-label">开始时间 <span class="text-danger">*</span></label>
                    <%-- ★★★ 直接使用 requestScope 中的预格式化字符串 ★★★ --%>
                    <input type="datetime-local" class="form-control" id="startTime" name="startTime" required
                           value="${isEditing ? formattedStartTime : ''}">
                </div>
                <div class="col-md-6">
                    <label for="endTime" class="form-label">结束时间 <span class="text-danger">*</span></label>
                    <%-- ★★★ 直接使用 requestScope 中的预格式化字符串 ★★★ --%>
                    <input type="datetime-local" class="form-control" id="endTime" name="endTime" required
                           value="${isEditing ? formattedEndTime : ''}">
                </div>
                <div class="col-md-6">
                    <label for="remindTime" class="form-label">提醒时间 (可选)</label>
                    <%-- ★★★ 直接使用 requestScope 中的预格式化字符串 ★★★ --%>
                    <input type="datetime-local" class="form-control" id="remindTime" name="remindTime"
                           value="${isEditing ? formattedRemindTime : ''}">
                </div>
                <div class="col-md-6">
                    <label for="repeatRule" class="form-label">重复规则 (可选)</label>
                    <input type="text" class="form-control" id="repeatRule" name="repeatRule" placeholder="例如：每周一"
                           value="${isEditing ? scheduleToEdit.repeatRule : ''}">
                </div>
            </div>
            <div class="mt-3">
                <button type="submit" class="btn btn-primary">${isEditing ? '更新日程' : '添加日程'}</button>
                <c:if test="${isEditing}">
                    <a href="${pageContext.request.contextPath}/schedule" class="btn btn-secondary">取消编辑</a>
                </c:if>
            </div>
        </form>
    </div> <%-- End of form-section --%>

    <%-- 日程列表区域 (保持不变) --%>
    <h4>我的日程列表</h4>
    <c:choose>
        <c:when test="${not empty schedules}">
            <div class="list-group">
                <c:forEach var="sch" items="${schedules}">
                    <div class="list-group-item list-group-item-action flex-column align-items-start schedule-item">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">
                                <c:out value="${sch.title}"/>
                                <span class="badge bg-${sch.type == '学习' ? 'info' : 'success'} ms-2">${sch.type}</span>
                            </h5>
                            <small class="schedule-meta">
                                ID: ${sch.scheduleId}
                            </small>
                        </div>
                        <p class="mb-1 schedule-meta">
                            <fmt:formatDate value="${sch.startTime}" pattern="yyyy-MM-dd HH:mm"/>
                            至
                            <fmt:formatDate value="${sch.endTime}" pattern="yyyy-MM-dd HH:mm"/>
                            <c:if test="${not empty sch.remindTime}">
                                | 提醒: <fmt:formatDate value="${sch.remindTime}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:if>
                            <c:if test="${not empty sch.repeatRule}">
                                | 重复: <c:out value="${sch.repeatRule}"/>
                            </c:if>
                        </p>
                        <c:if test="${not empty sch.description}">
                            <p class="mb-1"><c:out value="${sch.description}"/></p>
                        </c:if>
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/schedule?action=edit&id=${sch.scheduleId}" class="btn btn-sm btn-outline-secondary me-2">编辑</a>
                            <form action="${pageContext.request.contextPath}/schedule" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="scheduleId" value="${sch.scheduleId}">
                                <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('确认删除日程: ${sch.title} ?');">删除</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info" role="alert">
                您还没有添加任何日程安排。
            </div>
        </c:otherwise>
    </c:choose>

    <div class="mt-4 text-center">
        <a href="dashboard.jsp" class="btn btn-secondary">返回仪表板</a>
    </div>

</div> <%-- End of .container --%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>