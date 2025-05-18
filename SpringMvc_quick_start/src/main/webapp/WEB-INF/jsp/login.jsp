<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园小助手 - 登录</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            background-image: url('${pageContext.request.contextPath}/static/images/login_background.png');
            background-color: #0a192f; /* 深蓝色备用背景，更贴近星空 */
            background-size: cover;       /* 保持覆盖，不留白 */
            background-repeat: no-repeat;

            /* --- 调整这里的 background-position --- */
            /* 选项 1: 默认居中 (您当前使用的) */
            background-position: center center;

            /* 选项 2: 尝试让图片顶部居中，如果星空的上半部分更重要 */
            /* background-position: center top; */

            /* 选项 3: 尝试让图片底部居中，如果星空的下半部分更重要 */
            /* background-position: center bottom; */

            /* 选项 4: 尝试具体的百分比，例如，如果图片的视觉焦点偏上 */
            /* background-position: 50% 25%; */ /* 水平居中，垂直方向从顶部25%处开始裁剪 */

            /* 选项 5: 尝试具体的百分比，例如，如果图片的视觉焦点偏左上 */
            /* background-position: 25% 25%; */

            /* --- 请取消注释上面一个选项进行测试 --- */
        }
        .login-container {
            max-width: 400px;
            padding: 2rem;
            /* 调整背景色和透明度，使其在星空背景下更和谐 */
            background-color: rgba(10, 25, 47, 0.8); /* 深蓝半透明背景 */
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.3);
            border: 1px solid rgba(255, 255, 255, 0.1); /* 可选：浅色边框 */
        }
        /* 调整文字颜色以适应深色背景 */
        .login-container h2 {
            color: #e6f1ff; /* 浅蓝色或白色系 */
        }
        .login-container label {
            color: #ccd6f6; /* 稍暗的浅蓝色 */
        }
        .login-container .form-check-label {
            color: #ccd6f6;
        }
        .login-container a {
            color: #64ffda; /* 亮一点的青色或主题色作为链接颜色 */
        }
        .login-container a:hover {
            color: #affce9;
        }
        /* 可以为输入框也稍微调整一下样式，如果默认的在深色背景下不好看 */
        .login-container .form-control {
            background-color: rgba(255, 255, 255, 0.05);
            border-color: rgba(255, 255, 255, 0.2);
            color: #e6f1ff;
        }
        .login-container .form-control:focus {
            background-color: rgba(255, 255, 255, 0.1);
            border-color: #64ffda;
            color: #e6f1ff;
            box-shadow: 0 0 0 0.25rem rgba(100, 255, 218, 0.25);
        }
        .login-container .form-control::placeholder { /* Chrome, Firefox, Opera, Safari 10.1+ */
            color: #8892b0;
            opacity: 1; /* Firefox */
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2 class="text-center mb-4">校园小助手登录</h2>

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
        <% session.removeAttribute("successMessage"); %>
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

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="mb-3">
            <label for="username" class="form-label">用户名:</label>
            <input type="text" class="form-control" id="username" name="username" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">密码:</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" value="true" id="rememberMe" name="rememberMe">
            <label class="form-check-label" for="rememberMe">
                记住我 (下次自动登录)
            </label>
        </div>
        <button type="submit" class="btn btn-primary w-100">登录</button>
        <div class="mt-3 text-center">
            <a href="${pageContext.request.contextPath}/register">没有账号？去注册</a>
        </div>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>