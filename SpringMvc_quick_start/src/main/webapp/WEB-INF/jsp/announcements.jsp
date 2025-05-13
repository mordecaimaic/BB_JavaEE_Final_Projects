<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- 用于日期格式化 --%>
<%@ page import="com.campus.assistant.model.User" %>

<%-- 1. 登录状态检查 (Servlet 已做，这里可省略或保留) --%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园公告 - 校园小助手</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .announcement-card {
            margin-bottom: 1.5rem;
            border-left: 5px solid #0d6efd; /* 左侧加条颜色线 */
        }
        .announcement-card.urgent {
            border-left-color: #dc3545; /* 紧急公告用红色线 */
        }
        .announcement-meta {
            font-size: 0.85rem;
            color: #6c757d; /* 元数据用灰色 */
        }
        .announcement-content {
            white-space: pre-wrap; /* 保留公告内容中的换行和空格 */
            margin-top: 0.5rem;
        }
    </style>
</head>
<body>

<%-- 2. 包含通用的导航栏 --%>
<jsp:include page="header.jsp" />

<%-- 3. 主体内容区域 --%>
<div class="container mt-4">
    <h2 class="mb-4">校园公告</h2>

    <%-- 显示错误消息 --%>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
                ${errorMessage}
        </div>
    </c:if>

    <%-- 公告列表 --%>
    <c:choose>
        <c:when test="${not empty announcements}">
            <c:forEach var="ann" items="${announcements}">
                <div class="card announcement-card ${ann.urgent ? 'urgent' : ''}">
                    <div class="card-body">
                        <h5 class="card-title">
                            <c:out value="${ann.title}" />
                            <c:if test="${ann.urgent}">
                                <span class="badge bg-danger ms-2">紧急</span>
                            </c:if>
                        </h5>
                        <div class="announcement-meta">
                            <span>发布者: <c:out value="${ann.publisherName}" /></span> |
                            <span>发布时间: <fmt:formatDate value="${ann.publishTime}" pattern="yyyy-MM-dd HH:mm" /></span> |
                            <span>范围: <c:out value="${ann.scope}" /></span>
                            <c:if test="${ann.scope == '院系' && not empty ann.department}">
                                (面向: <c:out value="${ann.department}" />)
                            </c:if>
                        </div>
                        <div class="announcement-content">
                            <c:out value="${ann.content}" escapeXml="false" /> <%-- escapeXml="false" 如果内容包含HTML需谨慎 --%>
                                <%-- 如果内容确定是纯文本，建议保持默认的 escapeXml="true" (或不写) 以防止 XSS --%>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info" role="alert">
                当前没有与您相关的公告。
            </div>
        </c:otherwise>
    </c:choose>

    <div class="mt-4 text-center">
        <a href="dashboard.jsp" class="btn btn-secondary">返回仪表板</a>
    </div>

</div> <%-- End of .container --%>

<%-- 引入 Bootstrap JS --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>