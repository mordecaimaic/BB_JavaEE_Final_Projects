<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% String currentURI = request.getRequestURI(); %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="dashboard">校园小助手</a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("dashboard") ? "active" : "" %>" aria-current="page" href="dashboard">首页</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("courses") ? "active" : "" %>" href="courses">课程管理</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("assignments") ? "active" : "" %>" href="assignments">作业提交</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("announcements") ? "active" : "" %>" href="announcements">校园公告</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("schedule") ? "active" : "" %>" href="schedule">个人日程</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= currentURI.endsWith("materials") ? "active" : "" %>" href="materials">资料共享</a>
        </li>
      </ul>

      <ul class="navbar-nav">
        <c:if test="${not empty sessionScope.user}">
          <li class="nav-item">
            <span class="navbar-text me-3">
              <%-- ========== 修改部分开始 ========== --%>
              <c:choose>
                <%-- 条件1: 如果用户角色是 'admin' --%>
                <c:when test="${sessionScope.user.role == 'admin'}">
                  欢迎, ${sessionScope.user.username} 老师！
                </c:when>

                <%-- 条件2: 如果用户角色是 'student' --%>
                <c:when test="${sessionScope.user.role == 'student'}">
                  欢迎, ${sessionScope.user.username} 同学！
                </c:when>

                <%-- 其他情况 (例如角色信息缺失), 显示默认欢迎语 --%>
                <c:otherwise>
                  欢迎, ${sessionScope.user.username}!
                </c:otherwise>
              </c:choose>
              <%-- ========== 修改部分结束 ========== --%>
            </span>
          </li>
        </c:if>
        <li class="nav-item">
          <a class="btn btn-outline-light" href="logoutServlet">退出登录</a>
        </li>
      </ul>
    </div>
  </div>
</nav>