<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%
String siteRoot = request.getContextPath(); 
String title = request.getParameter("title");
title = title==null ? "" : title;
String current = request.getParameter("current");
current = current==null ? "" : current;

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=title %></title>
<link href="<%=siteRoot%>/css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="<%=siteRoot%>/js/jquery.js"></script>
<script type="text/javascript">
$(function(){	
	//顶部导航切换
	$(".nav li a").click(function(){
		$(".nav li a.selected").removeClass("selected")
		$(this).addClass("selected");
	})	
})	
</script>


</head>

<body style="background:url(<%=siteRoot%>/images/topbg.gif) repeat-x;">

    <div class="topleft">
    <a href="main.html" target="_parent"><img src="<%=siteRoot%>/images/logo.png" title="系统首页" /></a>
    </div>
        
    <ul class="nav">
    <li><a href="<%=siteRoot%>/index.html" target="mainFrame" class="selected"><img src="<%=siteRoot%>/images/home.png" title="首页" /><h2>首页</h2></a></li>
    <li><a href="<%=siteRoot%>/productList.jsp" target="mainFrame"><img src="<%=siteRoot%>/images/packing.png" title="产品管理" /><h2>产品管理</h2></a></li>
    <li><a href="<%=siteRoot%>/distributorList.jsp"  target="mainFrame"><img src="<%=siteRoot%>/images/binary-tree.png" title="经销商管理" /><h2>经销商管理</h2></a></li>
    <li><a href="<%=siteRoot%>/tools.html"  target="mainFrame"><img src="<%=siteRoot%>/images/autoship.png" title="物流管理" /><h2>物流管理</h2></a></li>
    <li><a href="<%=siteRoot%>/computer.html" target="mainFrame"><img src="<%=siteRoot%>/images/column_chart.png" title="统计分析" /><h2>统计分析</h2></a></li>
    <li><a href="<%=siteRoot%>/tab.html"  target="mainFrame"><img src="<%=siteRoot%>/images/Package-Accept.png" title="产品溯源" /><h2>产品溯源</h2></a></li>
    </ul>
            
    <div class="topright">    
    <ul>
    <li><span><img src="<%=siteRoot%>/images/help.png" title="帮助"  class="helpimg"/></span><a href="#">帮助</a></li>
    <li><a href="#">关于</a></li>
    <li><a href="<%=siteRoot%>/login.jsp" target="_parent">退出</a></li>
    </ul>
     
    <div class="user">
    <span>admin</span>
    <i>消息</i>
    <b>5</b>
    </div>    
    
    </div>

</body>
</html>
