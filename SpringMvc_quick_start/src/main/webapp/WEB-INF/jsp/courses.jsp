<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- 引入 JSTL --%>
<%@ page import="com.example.model.User" %> <%-- 假设你的User类在 com.example.model 包下 --%>

<%-- 1. 登录状态检查 --%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>课程管理 - 校园小助手</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* 可以添加一些自定义样式 */
        .table-responsive {
            margin-top: 1.5rem;
        }
        th {
            white-space: nowrap; /* 防止表头换行 */
        }
    </style>
</head>
<body>

<%-- 2. 包含通用的导航栏 --%>
<jsp:include page="header.jsp" />

<%-- 3. 主体内容区域 --%>
<div class="container mt-4">
    <h2 class="mb-3">课程管理</h2>

    <%-- 显示错误消息 (如果 Servlet 传递了错误) --%>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
                ${errorMessage}
        </div>
    </c:if>

    <%-- 分区：我的课程 --%>
    <div class="card mb-4">
        <div class="card-header">
            <h4>我的课程</h4>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover table-bordered">
                    <thead class="table-light">
                    <tr>
                        <th>课程名称</th>
                        <th>授课教师</th>
                        <th>上课地点</th>
                        <th>上课时间</th>
                        <th>学分</th>
                        <th>类型</th>
                        <th>学期</th>
                        <%-- <th>操作</th> 可选：未来添加“退课”按钮 --%>
                    </tr>
                    </thead>
                    <tbody>
                    <%-- 使用 JSTL 遍历 Servlet 传递过来的 myCourses 列表 --%>
                    <c:choose>
                        <c:when test="${not empty myCourses}">
                            <c:forEach var="course" items="${myCourses}">
                                <tr>
                                    <td><c:out value="${course.courseName}" /></td>
                                    <td><c:out value="${not empty course.teacherName ? course.teacherName : 'N/A'}" /></td>
                                    <td><c:out value="${course.classroom}" /></td>
                                    <td><c:out value="${course.schedule}" /></td>
                                    <td><c:out value="${course.credit}" /></td>
                                    <td><c:out value="${course.type}" /></td>
                                    <td><c:out value="${course.semester}" /></td>
                                        <%-- <td><button class="btn btn-sm btn-danger" disabled>退课</button></td> --%>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="text-center text-muted">您当前没有选修任何课程。</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <%-- 分区：所有可选课程 --%>
    <div class="card">
        <div class="card-header">
            <h4>可选课程列表</h4>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover table-bordered">
                    <thead class="table-light">
                    <tr>
                        <th>课程名称</th>
                        <th>授课教师</th>
                        <th>学分</th>
                        <th>类型</th>
                        <th>描述</th>
                        <%-- <th>操作</th> 可选：未来添加“选课”按钮 --%>
                    </tr>
                    </thead>
                    <tbody>
                    <%-- 遍历 availableCourses 列表 --%>
                    <c:choose>
                        <c:when test="${not empty availableCourses}">
                            <c:forEach var="course" items="${availableCourses}">
                                <tr>
                                    <td><c:out value="${course.courseName}" /></td>
                                    <td><c:out value="${not empty course.teacherName ? course.teacherName : 'N/A'}" /></td>
                                    <td><c:out value="${course.credit}" /></td>
                                    <td><c:out value="${course.type}" /></td>
                                    <td><c:out value="${course.description}" /></td>
                                        <%-- <td><button class="btn btn-sm btn-success" disabled>选课</button></td> --%>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="text-center text-muted">当前没有可供选择的课程。</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="mt-4 text-center">
        <a href="dashboard" class="btn btn-secondary">返回仪表板</a>
    </div>

</div> <%-- End of .container --%>

<%-- 引入 Bootstrap JS --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>