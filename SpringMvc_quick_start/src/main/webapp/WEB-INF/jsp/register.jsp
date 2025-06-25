<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
            background-image: url('${pageContext.request.contextPath}/static/images/register_background.png');
            background-color: #1c2541; /* 深色备用背景，与星空或科技感图片搭配 */
            background-size: cover;       /* 关键：确保图片覆盖整个区域，不留白 */
            background-position: center center;  /* 图片居中显示和裁剪 */
            background-repeat: no-repeat; /* 图片不重复平铺 */
            image-rendering: crisp-edges;
        }
        .register-container {
            max-width: 450px;
            width: 90%;
            padding: 2rem 2.5rem;
            background-color: rgba(10, 25, 47, 0.85);
            border-radius: 10px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.5);
            border: 1px solid rgba(100, 255, 218, 0.1);
            backdrop-filter: blur(3px);
            -webkit-backdrop-filter: blur(3px);
        }
        .register-container h2 {
            color: #e6f1ff;
            font-weight: 400;
            letter-spacing: 0.5px;
            margin-bottom: 1.8rem;
        }
        .register-container label {
            color: #a8b2d1;
            font-weight: 500;
            margin-bottom: 0.3rem;
        }
        .register-container .form-control {
            background-color: rgba(20, 35, 59, 0.75);
            border: 1px solid rgba(100, 255, 218, 0.35);
            color: #e6f1ff;
            border-radius: 5px;
            padding: 0.6rem 0.8rem;
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
        .register-container .btn-success {
            background-color: #64ffda;
            border-color: #64ffda;
            color: #0a192f;
            font-weight: 500;
            padding: 0.6rem 0;
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
        .alert-custom {
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: #e6f1ff;
        }
        .alert-custom.alert-danger-custom {
            background-color: rgba(220, 53, 69, 0.3);
            border-color: rgba(220, 53, 69, 0.5);
        }
        .alert-custom.alert-success-custom {
            background-color: rgba(25, 135, 84, 0.3);
            border-color: rgba(25, 135, 84, 0.5);
        }
    </style>
</head>
<body>
<div class="register-container">
    <h2 class="text-center mb-4">用户注册</h2>

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

        <div class="mb-3">
            <label class="form-label d-block">选择您的角色:</label>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="role" id="roleStudentRegister" value="student" checked>
                <label class="form-check-label" for="roleStudentRegister">
                    学生
                </label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="role" id="roleAdminRegister" value="admin">
                <label class="form-check-label" for="roleAdminRegister">
                    管理员
                </label>
            </div>
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