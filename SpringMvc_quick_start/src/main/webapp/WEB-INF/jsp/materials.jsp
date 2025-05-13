<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.campus.assistant.model.User" %>

<%-- 1. 登录检查 --%>
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
    <title>资料共享 - 校园小助手</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-section { background-color: #f8f9fa; padding: 1.5rem; border-radius: 5px; margin-bottom: 2rem; }
        th, td { vertical-align: middle; }
        .file-icon { /* 可以添加根据文件类型显示不同图标的样式 */ }
    </style>
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-4">
    <h2 class="mb-4">资料共享</h2>

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

    <%-- 上传新资料表单 --%>
    <div class="form-section">
        <h4>上传新资料</h4>
        <form action="${pageContext.request.contextPath}/materials" method="post" enctype="multipart/form-data">
            <div class="row g-3">
                <div class="col-md-6">
                    <label for="courseId" class="form-label">所属课程 <span class="text-danger">*</span></label>
                    <select id="courseId" name="courseId" class="form-select" required>
                        <option value="" selected disabled>-- 请选择课程 --</option>
                        <%-- ★★★ 修正这里的 items 属性 ★★★ --%>
                        <c:forEach var="course" items="${courses}">
                            <option value="${course.courseId}">${course.courseName}</option>
                        </c:forEach>
                        <%-- (可选) 添加一个检查，如果 courses 为空 --%>
                        <c:if test="${empty courses}">
                            <option value="" disabled>暂无课程可选</option>
                        </c:if>
                    </select>
                </div>
                <div class="col-md-6">
                    <label for="file" class="form-label">选择文件 <span class="text-danger">*</span></label>
                    <input class="form-control" type="file" id="file" name="file" required>
                </div>
                <div class="col-12">
                    <label for="description" class="form-label">文件描述 (可选)</label>
                    <textarea class="form-control" id="description" name="description" rows="2" placeholder="简要说明文件内容..."></textarea>
                </div>
            </div>
            <button type="submit" class="btn btn-primary mt-3">上传资料</button>
        </form>
    </div> <%-- End of form-section --%>

    <%-- 资料列表 --%>
    <h4>共享资料列表</h4>
    <div class="table-responsive">
        <table class="table table-striped table-hover table-bordered">
            <thead class="table-light">
            <tr>
                <th>课程</th>
                <th>文件名</th>
                <th>描述</th>
                <th>类型</th>
                <th>大小</th>
                <th>上传者</th>
                <th>上传时间</th>
                <th>下载次数</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty materials}">
                    <c:forEach var="mat" items="${materials}">
                        <tr>
                            <td><c:out value="${mat.courseName}"/></td>
                            <td title="${mat.fileName}"><c:out value="${mat.fileName}"/></td> <%-- 显示原始文件名 --%>
                            <td><c:out value="${mat.description}"/></td>
                            <td><span class="badge bg-secondary">${mat.fileType}</span></td>
                            <td>${mat.fileSize} KB</td>
                            <td><c:out value="${mat.uploaderName}"/></td>
                            <td><fmt:formatDate value="${mat.uploadTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                            <td>${mat.downloadCount}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/materials?action=download&id=${mat.materialId}" class="btn btn-sm btn-success">下载</a>
                                    <%-- (可选) 删除按钮，需要权限判断和后端逻辑 --%>
                                <!--
                                <c:if test="${loggedInUser.userId == mat.uploaderId}"> <%-- 只有上传者可以删除 --%>
                                    <form action="..." method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="materialId" value="${mat.materialId}">
                                        <button type="submit" class="btn btn-sm btn-danger ms-1" onclick="return confirm('确认删除?')">删除</button>
                                    </form>
                                </c:if>
                                -->
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="9" class="text-center text-muted">当前没有共享的资料。</td>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>