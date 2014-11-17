<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%@page import="org.fto.jthink.resource.*" %>
<%@page import="org.fto.jthink.j2ee.web.HttpRequest" %>
<%@page import="org.fto.jthink.util.DateTimeHelper"%>
<%@page import="org.jdom.Element"%>
<%@page import="org.fto.jthink.util.XMLHelper"%>
<%@page import="org.fto.jthink.util.StringHelper"%>
<%@page import="org.fto.jthink.util.NumberHelper"%>
<%@page import="com.liang.util.*" %>
<%
	String siteRoot = request.getContextPath();
	String isLoginPage = (String)request.getAttribute("IS_LOGIN_PAGE");
	if(isLoginPage==null){
		/* check session timeout */
		new SessionTimeoutChecker(request);
	}	
	/* Get HttpRequest and  ResourceManager */
	ResourceManager resManager = (ResourceManager)request.getAttribute(ResourceManager.class.getName());
	HttpRequest httpRequest = (HttpRequest) resManager.getResource(HttpRequest.class.getName());
	
	int listLines = 10;
	String title="五粮液PCMS数字认证防伪系统";
%>