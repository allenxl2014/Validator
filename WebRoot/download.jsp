<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String ua = request.getHeader("User-Agent");
String para = request.getQueryString();
String url = "http://www.fw90.com/v.apk";
if(para.length() == 52){
	url = "http://www.fw90.com/" + para.substring(24, 29) + ".apk";
}
if (ua != null) {
	if (ua.indexOf("iPhone") >-1 || ua.indexOf("iPad") >-1 || ua.indexOf("iPod") >-1) {
		url = "http://supermacer.webatu.com/index.html";
	}
	if(ua.indexOf("MicroMessenger") == -1 && ua.indexOf("micromessenger") == -1){
		response.sendRedirect(url);
	}
}
%>
<!DOCTYPE HTML>
<html lang="zh-hans">
<head>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<title>FW90防伪认证系统下载</title>
<meta name="Keywords" content="FW90防伪认证系统下载"/>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0,user-scalable=0"/>
<meta name="format-detection" content="telephone=yes"/>
<link rel="stylesheet" href="wstyle/themes/default/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="wstyle/jquery.mobile-wap.css" />
<script src="wjs/jquery-1.9.1.js"></script>
<script src="wjs/jquery.mobile-1.4.0.min.js"></script>

<script type="text/javascript">

	function is_weixin(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
//			alert(1);
			return true;
	 	} else {
//			alert(2);
			return false;
		}
	}
	
	$(document).ready(function(){
//		is_weixin();
	});
</script>
</head>

<body>
<img src="images/tips.png" width="100%" />
</body>
</html>
