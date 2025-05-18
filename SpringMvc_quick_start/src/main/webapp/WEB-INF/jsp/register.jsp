<%@ page contentType="text/html;charset=UTF-8" %> <%-- 移除了 language="java" --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- 引入 JSTL 以便后续可能显示消息 --%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园小助手 - 注册</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            /* --- 背景图片设置 --- */
            background-image: url('${pageContext.request.contextPath}/static/images/register_background.png');
            background-color: #1c2541; /* 深色备用背景，与星空或科技感图片搭配 */
            background-size: cover;       /* 关键：确保图片覆盖整个区域，不留白 */
            background-position: center center;  /* 图片居中显示和裁剪 */
            background-repeat: no-repeat; /* 图片不重复平铺 */

            /* 尝试添加 image-rendering 属性以提高缩放图片的清晰度 */
            image-rendering: crisp-edges; /* 保留标准属性，移除 -webkit-optimize-contrast */
            /* --- 背景图片设置结束 --- */
        }
        .register-container {
            max-width: 450px; /* 可以比登录框稍宽一点，因为注册表单字段可能更多 */
            width: 90%; /* 在小屏幕上确保不会超出太多 */
            padding: 2rem 2.5rem; /* 调整内边距 */
            background-color: rgba(10, 25, 47, 0.85); /* 与登录页风格统一的深蓝半透明背景 */
            border-radius: 10px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.5); /* 更明显的阴影 */
            border: 1px solid rgba(100, 255, 218, 0.1); /* 主题色边框，可选 */
            backdrop-filter: blur(3px); /* 可选：毛玻璃效果 */
            -webkit-backdrop-filter: blur(3px);
        }
        .register-container h2 {
            color: #e6f1ff;
            font-weight: 400;
            letter-spacing: 0.5px;
            margin-bottom: 1.8rem; /* 调整标题下的间距 */
        }
        .register-container label {
            color: #a8b2d1;
            font-weight: 500; /* 标签文字稍粗一点 */
            margin-bottom: 0.3rem; /* 标签和输入框之间的间距 */
        }
        .register-container .form-control {
            background-color: rgba(20, 35, 59, 0.75);
            border: 1px solid rgba(100, 255, 218, 0.35);
            color: #e6f1ff;
            border-radius: 5px;
            padding: 0.6rem 0.8rem; /* 输入框内边距 */
        }
        .register-container .form-control:focus {
            background-color: rgba(20, 35, 59, 0.9);
            border-color: #64ffda;
            color: #e6f1ff;
            box-shadow: 0 0 0 0.2rem rgba(100, 255, 218, 0.25);
        }
        .register-container .form-control::placeholder {
            color: #8892b0;
            opacity: 0.6;
        }
        .register-container .btn-success { /* 自定义注册按钮样式 */
            background-color: #64ffda;
            border-color: #64ffda;
            color: #0a192f;
            font-weight: 500;
            padding: 0.6rem 0; /* 按钮上下内边距 */
            transition: background-color 0.2s ease-in-out, border-color 0.2s ease-in-out;
        }
        .register-container .btn-success:hover {
            background-color: #52d8bc;
            border-color: #52d8bc;
        }
        .register-container .mt-3 a {
            color: #64ffda;
            text-decoration: none;
        }
        .register-container .mt-3 a:hover {
            color: #affce9;
            text-decoration: underline;
        }
        /* 用于显示注册成功或失败消息的样式 (如果后续添加) */
        .alert-custom {
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: #e6f1ff;
        }
        .alert-custom.alert-danger-custom {
            background-color: rgba(220, 53, 69, 0.3); /* 半透明红色 */
            border-color: rgba(220, 53, 69, 0.5);
        }
        .alert-custom.alert-success-custom {
            background-color: rgba(25, 135, 84, 0.3); /* 半透明绿色 */
            border-color: rgba(25, 135, 84, 0.5);
        }

    </style>
</head>
<body>
<div class="register-container">
    <h2 class="text-center mb-4">用户注册</h2>

    <%-- 用于显示注册相关的消息 (如果您的注册Controller会设置这些属性) --%>
    <c:if test="${not empty requestScope.registrationError}">
        <div class="alert alert-custom alert-danger-custom" role="alert">
                ${requestScope.registrationError}
        </div>
    </c:if>
    <c:if test="${not empty requestScope.registrationSuccess}">
        <div class="alert alert-custom alert-success-custom" role="alert">
                ${requestScope.registrationSuccess}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="mb-3">
            <label for="username" class="form-label">用户名:</label>
            <input type="text" class="form-control" id="username" name="username" placeholder="创建您的用户名" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">密码:</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="设置您的密码 (至少6位)" required minlength="6">
        </div>
        <div class="mb-3">
            <label for="confirmPassword" class="form-label">确认密码:</label>
            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="再次输入密码" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">邮箱:</label>
            <input type="email" class="form-control" id="email" name="email" placeholder="请输入您的邮箱地址 (可选)">
        </div>
        <button type="submit" class="btn btn-success w-100 mt-2">立即注册</button>
        <div class="mt-3 text-center">
            <a href="${pageContext.request.contextPath}/login">已有账号？前往登录</a>
        </div>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>