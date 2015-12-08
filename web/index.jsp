<%--
  Created by IntelliJ IDEA.
  User: huzhicheng
  Date: 2015/12/7
  Time: 22:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.fengzheng.wechat.accesstoken.TokenThread" %>
<html>
  <head>
    <title></title>
  </head>
  <body>

  access_token为：
<%=TokenThread.accessToken.getAccessToken()%>
  </body>
</html>
