<%@page language="java" contentType="text/html; charset=utf-8"  isErrorPage="true" %>
<%@page import="org.fto.jthink.exception.*"%>
<%
	String siteRoot = request.getContextPath();
	String message = exception.getMessage();
	if(exception instanceof JThinkException){
		JThinkException e1 = (JThinkException)exception;
		if(e1.getCode() != 0){
			message = "系统错误，请重新登录或联系在线客服！";
		}
	}else if(exception instanceof JThinkRuntimeException){
		JThinkRuntimeException e2 = (JThinkRuntimeException)exception;
		if(e2.getMessage().indexOf("违反了 PRIMARY KEY 约束") != -1){
			message = "网络忙，请点击 <a href='javascript:window.history.back();' style='color:blue'>返回</a> 前一页，重新保存！";
		}else if(e2.getCode() != 0){
			message = "系统错误，请重新登录或联系在线客服！";
		}
	}else if(exception instanceof NullPointerException){
		message = "系统错误，请重新登录或联系在线客服！";
	}
	exception.printStackTrace();
	request.setAttribute("IS_ERROR_PAGE", "YES");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>五粮液PCMS数字认证防伪系统</TITLE>
<link type="text/css" rel="stylesheet" href="<%=siteRoot%>/style/css.css" media="screen" />
<script type="text/javascript" src="../js/main.js"></script>
</head>
<body style="text-align:center;"> 

<div id="yy_main">
<div id='error'><%=message%></div>
</div>

</body>
</html>