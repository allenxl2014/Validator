<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%
	request.setAttribute("IS_LOGIN_PAGE", "YES");
%>
<%@include file="/common/resource.jsp" %>
<jsp:useBean id="sessionJBean"
	class="com.liang.session.SessionJBean" scope="page"></jsp:useBean>

<%
	sessionJBean.initialize(request);
	
	String errorMessage = "";
	if(sessionJBean.getParameterString("action").equals("login")){
		try{
			sessionJBean.login();
			response.sendRedirect("productInfo/productList.jsp");
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = e.getMessage();
		}
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>数字CS认证防伪系统－登录</title>
<link href="style/style.css" rel="stylesheet" type="text/css" />
<SCRIPT language="JavaScript" src="js/Validator.js"></SCRIPT>
<SCRIPT language="JavaScript">
	function login(){
		if(isNull(base.loginUid)){
			alert("用户名不能是空！");
			base.loginUid.focus();
			return false;
		}
		if(base.loginPwd.value==""){
			alert("密码不能是空！");
			base.loginPwd.focus();
			return false;
		}
		return true;
	}
	
</SCRIPT>
</head>
<body onload="document.getElementById('loginUid').focus()" style="background-color:rgb(60,141,247);">
<div class="loginTitle">数字CS认证防伪系统</div>
<div id="loginDiv">
	<div class="bigDiv">
		<div class="leftDiv">用户登录：</div>
		<div class="rightDiv">
			<form id="base" name="base" method="post" action="login.jsp?action=login" onSubmit="return login()">
				用户名：<br/><input type="text" id="loginUid" name="loginUid" class="loginInput" /><br/><br/>
				密&nbsp;&nbsp;码：<br/><input type="password" id="loginPwd" name="loginPwd" class="loginInput" /><br/><br/>
				<input type="submit" value="登录"/>
				<span style="padding:0 15px;"><input type="checkbox" id="remeberPwd" name="remeberPwd"/><label for="remeberPwd">记住密码</label></span>
				忘记密码
			</form>
			<span style="color:red; display:block; height:25px; line-height:25px;"><%=errorMessage%></span>
		</div>
		<div class="middleDiv"></div>
	</div>
</div>

</body>
</html>
