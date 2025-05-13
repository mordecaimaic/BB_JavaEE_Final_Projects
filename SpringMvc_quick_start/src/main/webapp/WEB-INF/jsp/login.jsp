<!-- 登录-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- 引入 JSTL 以便显示消息 --%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园小助手 - 登录</title>
    <!-- 引入 Bootstrap CSS (使用CDN) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        .login-container {
            max-width: 400px;
            padding: 2rem;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2 class="text-center mb-4">校园小助手登录</h2>

    <%-- 显示来自 Servlet 的错误或成功消息 --%>
    <c:if test="${not empty requestScope.loginError}">
        <div class="alert alert-danger" role="alert">
                ${requestScope.loginError}
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("successMessage"); // 显示后移除 Flash Message %>
    </c:if>
    <c:if test="${not empty param.message && param.message == 'logout_success'}">
        <div class="alert alert-success" role="alert">
            您已成功退出登录。
        </div>
    </c:if>
    <c:if test="${not empty param.error && param.error == 'token_mismatch'}">
        <div class="alert alert-warning" role="alert">
            检测到您的账号可能在其他地方登录，请重新登录。为安全起见，已清除自动登录设置。
        </div>
    </c:if>
    <c:if test="${not empty param.error && param.error == 'user_deleted'}">
        <div class="alert alert-danger" role="alert">
            与自动登录关联的用户账号不存在，请重新登录或注册。
        </div>
    </c:if>


    <!-- 登录表单 -->
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="mb-3">
            <label for="username" class="form-label">用户名:</label>
            <input type="text" class="form-control" id="username" name="username" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">密码:</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>

        <%-- 添加 “记住我” 复选框 --%>
        <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" value="true" id="rememberMe" name="rememberMe">
            <label class="form-check-label" for="rememberMe">
                记住我 (下次自动登录) <%-- 或者 "记住我 (持续登录 2 天)" --%>
            </label>
        </div>

        <button type="submit" class="btn btn-primary w-100">登录</button>
        <div class="mt-3 text-center">
            <a href="register">没有账号？去注册</a>
        </div>
    </form>
</div>

<!-- 引入 Bootstrap JS (可选，如果需要用到JS组件) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>