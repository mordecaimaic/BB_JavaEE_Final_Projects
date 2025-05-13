<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 引入 JSTL 标签库，如果导航栏中使用了 JSTL/EL (例如显示用户名) --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 获取当前请求的 URI，用于判断哪个导航链接应该高亮显示 (active) --%>
<% String currentURI = request.getRequestURI(); %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <%-- Logo 或 站点名称，链接到仪表板 --%>
    <a class="navbar-brand" href="dashboard.jsp">校园小助手</a>

    <%-- 移动设备上的折叠按钮 --%>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <%-- 导航链接 --%>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <%-- 如果当前页面是 dashboard.jsp，则添加 'active' 类 --%>
          <a class="nav-link <%= currentURI.endsWith("dashboard.jsp") ? "active" : "" %>" aria-current="page" href="dashboard.jsp">首页</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("courses.jsp") ? "active" : "" %>" href="courses.jsp">课程管理</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("assignments.jsp") ? "active" : "" %>" href="assignments.jsp">作业提交</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("announcements.jsp") ? "active" : "" %>" href="announcements.jsp">校园公告</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("schedule.jsp") ? "active" : "" %>" href="schedule.jsp">个人日程</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("materials.jsp") ? "active" : "" %>" href="materials.jsp">资料共享</a>
        </li>
      </ul>

      <%-- 用户信息和退出登录按钮 (放在导航栏右侧) --%>
      <ul class="navbar-nav">
        <%-- 检查 session 中是否有用户信息 (假设登录后设置了 sessionScope.user) --%>
        <c:if test="${not empty sessionScope.user}">
          <li class="nav-item">
              <%-- 使用 EL 表达式显示用户名, 假设 User 对象有 getUsername() 方法 --%>
            <span class="navbar-text me-3">
                            欢迎, ${sessionScope.user.username}!
                        </span>
          </li>
        </c:if>
        <li class="nav-item">
          <%-- 退出登录按钮，链接到处理登出的 Servlet (例如 logoutServlet) --%>
          <a class="btn btn-outline-light" href="logoutServlet">退出登录</a>
        </li>
      </ul>
    </div>
  </div>
</nav>