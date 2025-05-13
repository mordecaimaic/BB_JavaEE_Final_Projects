<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 检查session中是否有用户信息
    if (session.getAttribute("user") != null) {
        // 如果已登录，重定向到仪表板
        response.sendRedirect("dashboard");
    } else {
        // 如果未登录，重定向到登录页面
        response.sendRedirect("login");
    }
%>
<%-- 此页面只做重定向，不需要显示任何HTML内容 --%>