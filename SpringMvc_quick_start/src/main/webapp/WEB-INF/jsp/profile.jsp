<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.campus.assistant.model.User" %>

<%-- 登录检查 --%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    // 从 request 获取最新的用户信息用于显示
    User profileUser = (User) request.getAttribute("profileUser");
    // 如果 profileUser 为 null (例如加载出错)，可以尝试使用 session 中的，或者显示错误
    if (profileUser == null) {
        profileUser = loggedInUser; // 降级使用 session 中的（可能不是最新的）
    }
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人中心 - 校园小助手</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .profile-section { margin-bottom: 2rem; }
        .profile-section h4 { border-bottom: 1px solid #dee2e6; padding-bottom: 0.5rem; margin-bottom: 1rem; }
        label { font-weight: 500; }
        .form-control[readonly] { background-color: #e9ecef; opacity: 1; }
    </style>
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-4">
    <h2 class="mb-4">个人中心</h2>

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

    <%-- 检查 profileUser 是否存在 --%>
    <c:if test="${profileUser == null}">
        <div class="alert alert-warning" role="alert">
            无法加载个人信息。
        </div>
    </c:if>
    <c:if test="${profileUser != null}">

        <%-- 显示个人信息区域 --%>
        <div class="profile-section card">
            <div class="card-header"><h4>基本信息</h4></div>
            <div class="card-body">
                <dl class="row">
                    <dt class="col-sm-3">用户名:</dt>
                    <dd class="col-sm-9"><c:out value="${profileUser.username}"/></dd>

                    <dt class="col-sm-3">学号/工号:</dt>
                    <dd class="col-sm-9"><c:out value="${not empty profileUser.studentId ? profileUser.studentId : '未设置'}"/></dd>

                    <dt class="col-sm-3">院系:</dt>
                    <dd class="col-sm-9"><c:out value="${not empty profileUser.department ? profileUser.department : '未设置'}"/></dd>

                    <dt class="col-sm-3">角色:</dt>
                    <dd class="col-sm-9"><c:out value="${profileUser.role}"/></dd>

                    <dt class="col-sm-3">注册时间:</dt>
                    <dd class="col-sm-9"><fmt:formatDate value="${profileUser.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></dd>
                </dl>
            </div>
        </div>

        <%-- 修改个人信息表单 --%>
        <div class="profile-section card">
            <div class="card-header"><h4>修改联系信息</h4></div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/profile" method="post">
                    <input type="hidden" name="action" value="updateProfile">
                    <div class="mb-3 row">
                        <label for="email" class="col-sm-3 col-form-label">邮箱:</label>
                        <div class="col-sm-9">
                            <input type="email" class="form-control" id="email" name="email" value="${profileUser.email}" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="phone" class="col-sm-3 col-form-label">电话:</label>
                        <div class="col-sm-9">
                            <input type="tel" class="form-control" id="phone" name="phone" value="${profileUser.phone}" placeholder="请输入手机号码">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-9 offset-sm-3">
                            <button type="submit" class="btn btn-primary">更新信息</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <%-- 修改密码表单 (可选) --%>
        <div class="profile-section card">
            <div class="card-header"><h4>修改密码</h4></div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/profile" method="post">
                    <input type="hidden" name="action" value="changePassword">
                    <div class="mb-3 row">
                        <label for="currentPassword" class="col-sm-3 col-form-label">当前密码:</label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="newPassword" class="col-sm-3 col-form-label">新密码:</label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="newPassword" name="newPassword" required minlength="6">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="confirmPassword" class="col-sm-3 col-form-label">确认新密码:</label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required minlength="6">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-9 offset-sm-3">
                            <button type="submit" class="btn btn-warning">修改密码</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

    </c:if> <%-- End of c:if test profileUser != null --%>

    <div class="mt-4 text-center">
        <a href="dashboard.jsp" class="btn btn-secondary">返回仪表板</a>
    </div>

</div> <%-- End of .container --%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>