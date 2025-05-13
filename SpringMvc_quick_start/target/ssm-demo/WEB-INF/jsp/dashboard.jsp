<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- 引入 JSTL core 标签库 (如果需要显示动态信息) --%>

<%-- 1. 登录状态检查 (非常重要!) --%>
<%
    // 检查 session 中是否有用户信息
    // 假设登录成功后，你在 Servlet 中执行了: session.setAttribute("user", userObject);
    if (session.getAttribute("user") == null) {
        // 如果未登录，重定向到登录页面
        response.sendRedirect("login.jsp");
        return; // 必须加 return，防止继续执行页面代码导致错误
    }
    // 如果已登录，可以获取用户信息 (可选，如果 header.jsp 中已处理)
    // com.campus.assistant.model.User loggedInUser = (com.campus.assistant.model.User) session.getAttribute("user");
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园小助手 - 仪表板</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* 可以添加一些自定义样式 */
        .feature-card {
            margin-bottom: 1.5rem; /* 卡片间距 */
            transition: transform .2s; /* 添加悬停效果 */
        }
        .feature-card:hover {
            transform: scale(1.03); /* 悬停时放大一点 */
        }
        .card-body a.btn {
            width: 80%; /* 让按钮宽度统一 */
        }
    </style>
</head>
<body>

<%-- 2. 包含通用的导航栏 --%>
<jsp:include page="header.jsp" />

<%-- 3. 主体内容区域 - 功能导航 --%>
<div class="container mt-5">
    <h1 class="mb-4 text-center">欢迎使用校园小助手</h1>
    <p class="lead text-center mb-5">请选择您需要使用的功能：</p>

    <%-- 使用 Bootstrap 网格系统排列功能卡片 --%>
    <div class="row justify-content-center">

        <%-- 功能卡片 1: 课程管理 --%>
        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">课程管理</h5>
                    <p class="card-text text-muted">查看、添加或删除您的课程表。</p>
                    <%-- ★★★ 修改这里的 href ★★★ --%>
                    <a href="${pageContext.request.contextPath}/courses" class="btn btn-outline-primary mt-2">进入课程管理</a>
                    <%-- 使用 ${pageContext.request.contextPath} 获取项目根路径，更健壮 --%>
                </div>
            </div>
        </div>

            <%-- 功能卡片 2: 作业提交 --%>
            <div class="col-lg-4 col-md-6">
                <div class="card feature-card text-center shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">作业提交</h5>
                        <p class="card-text text-muted">上传作业文件，查看截止日期和状态。</p>
                        <%-- ★★★ 修改这里的 href ★★★ --%>
                        <a href="${pageContext.request.contextPath}/assignments" class="btn btn-outline-primary mt-2">查看我的作业</a>
                    </div>
                </div>
            </div>

            <%-- 功能卡片 3: 校园公告 --%>
            <div class="col-lg-4 col-md-6">
                <div class="card feature-card text-center shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">校园公告</h5>
                        <p class="card-text text-muted">浏览学校或院系的最新通知。</p>
                        <%-- ★★★ 修改这里的 href ★★★ --%>
                        <a href="${pageContext.request.contextPath}/announcements" class="btn btn-outline-primary mt-2">查看公告</a>
                    </div>
                </div>
            </div>

            <%-- 功能卡片 4: 个人日程 --%>
            <div class="col-lg-4 col-md-6">
                <div class="card feature-card text-center shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">个人日程</h5>
                        <p class="card-text text-muted">管理您的学习和生活日程安排。</p>
                        <%-- ★★★ 修改这里的 href ★★★ --%>
                        <a href="${pageContext.request.contextPath}/schedule" class="btn btn-outline-primary mt-2">管理我的日程</a>
                    </div>
                </div>
            </div>

            <%-- 功能卡片 5: 资料共享 --%>
            <div class="col-lg-4 col-md-6">
                <div class="card feature-card text-center shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">资料共享</h5>
                        <p class="card-text text-muted">上传或下载学习相关资料文件。</p>
                        <%-- ★★★ 修改这里的 href ★★★ --%>
                        <a href="${pageContext.request.contextPath}/materials" class="btn btn-outline-primary mt-2">进入资料共享</a>
                    </div>
                </div>
            </div>

            <%-- 个人中心卡片 --%>
            <div class="col-lg-4 col-md-6">
                <div class="card feature-card text-center shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">个人中心</h5>
                        <p class="card-text text-muted">查看或修改您的个人信息。</p>
                        <%-- ★★★ 修改这里的 href 并移除 disabled ★★★ --%>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-primary mt-2">进入个人中心</a>
                    </div>
                </div>
            </div>

    </div> <%-- End of .row --%>
</div> <%-- End of .container --%>

<%-- 4. 引入 Bootstrap JS (通常放在 body 结束前) --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<%-- 可以包含一个页脚 (footer.jsp) --%>
<%-- <jsp:include page="footer.jsp" /> --%>

</body>
</html>