<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园小助手 - 仪表板</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-image: url('${pageContext.request.contextPath}/static/images/dashboard_background.png');
            background-color: #f4f7f6;
            background-size: cover;
            background-position: center center;
            background-repeat: no-repeat;
            background-attachment: fixed;
            min-height: 100vh;
            /* 确保这个值与 header.jsp 中固定导航栏的实际高度一致 */
            /* 如果导航栏不是固定的，或者您在 header.jsp 中用其他方式处理了占位，可以调整或移除此项 */
            padding-top: 56px; /* 假设导航栏高度为 56px */
        }

        /* 确保 header.jsp 中的导航栏（如果它是 fixed-top）具有足够高的 z-index */
        /* Bootstrap 的 fixed-top navbar 通常已经处理了 z-index (默认为 1030) */
        /* 如果您自定义了 header.jsp 且没有使用 Bootstrap 的 fixed-top，可能需要手动设置： */
        /*
        .your-custom-fixed-header-class {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1030; // 或者一个比页面其他内容都高的值
            // 确保它有背景色，例如：
            // background-color: #343a40; // 示例深色
        }
        */

        .feature-card {
            margin-bottom: 1.5rem;
            transition: transform .2s;
            background-color: rgba(255, 255, 255, 0.9);
            border: none;
        }
        .feature-card:hover {
            transform: scale(1.03);
            box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
        }
        .card-body a.btn {
            width: 80%;
        }
        .container.mt-5 {
            /* 确保这里的 mt-5 不会因为 body 的 padding-top 而产生双重间距问题 */
            /* 如果 body 有 padding-top，这里的 mt-5 依然会生效，计算是从 body 的 padding 内部开始的 */
            /* 您可能希望内容区域与导航栏底部保持一定距离 */
            background-color: rgba(255, 255, 255, 0.8);
            padding: 2rem;
            border-radius: 0.5rem;
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<%-- 2. 包含通用的导航栏 --%>
<%-- 确保 header.jsp 中的导航栏HTML使用了 Bootstrap 的 fixed-top 类 (例如 <nav class="navbar ... fixed-top">) --%>
<jsp:include page="header.jsp" />

<%-- 3. 主体内容区域 - 功能导航 --%>
<div class="container mt-5">
    <h1 class="mb-4 text-center">欢迎使用校园小助手</h1>
    <p class="lead text-center mb-5">请选择您需要使用的功能：</p>

    <div class="row justify-content-center">
        <%-- 功能卡片 ... (省略以减少篇幅) ... --%>
        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">课程管理</h5>
                    <p class="card-text text-muted">查看、添加或删除您的课程表。</p>
                    <a href="${pageContext.request.contextPath}/courses" class="btn btn-outline-primary mt-2">进入课程管理</a>
                </div>
            </div>
        </div>
        <%-- 其他卡片 --%>
        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">作业提交</h5>
                    <p class="card-text text-muted">上传作业文件，查看截止日期和状态。</p>
                    <a href="${pageContext.request.contextPath}/assignments" class="btn btn-outline-primary mt-2">查看我的作业</a>
                </div>
            </div>
        </div>

        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">校园公告</h5>
                    <p class="card-text text-muted">浏览学校或院系的最新通知。</p>
                    <a href="${pageContext.request.contextPath}/announcements" class="btn btn-outline-primary mt-2">查看公告</a>
                </div>
            </div>
        </div>

        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">个人日程</h5>
                    <p class="card-text text-muted">管理您的学习和生活日程安排。</p>
                    <a href="${pageContext.request.contextPath}/schedule" class="btn btn-outline-primary mt-2">管理我的日程</a>
                </div>
            </div>
        </div>

        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">资料共享</h5>
                    <p class="card-text text-muted">上传或下载学习相关资料文件。</p>
                    <a href="${pageContext.request.contextPath}/materials" class="btn btn-outline-primary mt-2">进入资料共享</a>
                </div>
            </div>
        </div>

        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">个人中心</h5>
                    <p class="card-text text-muted">查看或修改您的个人信息。</p>
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-primary mt-2">进入个人中心</a>
                </div>
            </div>
        </div>

        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">生活费用缴纳</h5>
                    <p class="card-text text-muted">缴纳住宿费、水电费等校园生活相关费用。</p>
                    <a href="${pageContext.request.contextPath}/hello" class="btn btn-outline-primary mt-2">前往缴费</a>
                </div>
            </div>
        </div>

        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">校园活动</h5>
                    <p class="card-text text-muted">查看近期校园活动、讲座和展览信息。</p>
                    <a href="${pageContext.request.contextPath}/hello" class="btn btn-outline-primary mt-2">查看活动</a>
                </div>
            </div>
        </div>

        <div class="col-lg-4 col-md-6">
            <div class="card feature-card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">学生组织</h5>
                    <p class="card-text text-muted">浏览学生社团和组织，寻找您的兴趣爱好。</p>
                    <a href="${pageContext.request.contextPath}/hello" class="btn btn-outline-primary mt-2">探索组织</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>