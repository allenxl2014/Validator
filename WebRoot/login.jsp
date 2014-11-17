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
			response.sendRedirect("main.html");
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
<title><%=title %></title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="js/jquery.js"></script>
<script src="js/cloud.js" type="text/javascript"></script>
<SCRIPT language="JavaScript" src="js/Validator.js"></SCRIPT>
<SCRIPT language="JavaScript">
	$(function(){
	    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
		$(window).resize(function(){  
	    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
	    })  
	});  
	function login(){
		if(isNull(myform.loginuser)){
			alert("用户名不能是空！");
			myform.loginuser.focus();
			return false;
		}
		if(myform.loginpwd.value==""){
			alert("密码不能是空！");
			myform.loginpwd.focus();
			return false;
		}
		return true;
	}
	
</SCRIPT>

</head>

<body style="background-color:#1c77ac; background-image:url(images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;">

    <div id="mainBody">
      <div id="cloud1" class="cloud"></div>
      <div id="cloud2" class="cloud"></div>
    </div>  

<div class="logintop">    
    <span>欢迎登录防伪90后台管理界面平台</span>    
    <ul>

    <li><a href="#">帮助</a></li>
    <li><a href="#">关于</a></li>
    </ul>    
    </div>
    
    <div class="loginbody">
    
    <span class="systemlogo"></span> 
       
    <div class="loginbox">
	<form id="myform" name="myform" method="post" action="login.jsp?action=login" onSubmit="return login()">
	    <ul>
	    	<li><input name="loginuser" type="text" class="loginuser" value="用户名" onfocus="JavaScript:this.value=''"/></li>
	    	<li><input name="loginpwd" type="text" class="loginpwd" value="密码" onfocus="JavaScript:this.value=''"/></li>
	    	<li><input name="" type="submit" class="loginbtn" value="登录"  />
	    	<label><input name="remeberPwd" type="checkbox" value="" checked="checked" />记住密码</label><label><a href="#">忘记密码？</a></label></li>
	    </ul>
    </form>
    </div>
    </div>
</body>

</html>
