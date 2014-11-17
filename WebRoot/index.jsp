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
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>

</head>


<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    </ul>
    </div>
    
    <div class="mainindex">
    
    
    <div class="welinfo">
    <span><img src="images/sun.png" alt="天气" /></span>
    <b>Admin早上好，欢迎使用信息管理系统</b>
    <a href="#">帐号设置</a>
    </div>
    
    <div class="welinfo">
    <span><img src="images/time.png" alt="时间" /></span>
    <i>您上次登录的时间：2013-10-09 15:22</i> 
    </div>
    
    <div class="xline"></div>
    
    <ul class="iconlist">
    


    <li><img src="images/i05.png" /><p><a href="#">数据统计</a></p></li>
    <li><img src="images/diagram_64.png" /><p><a href="#">系统动态监控</a></p></li>
    <li><img src="images/shopping-unlock-64.png" /><p><a href="#">销售激活</a></p></li>
    <li><img src="images/search_64.png" /><p><a href="#">产品查询</a></p></li> 
            
    </ul>
    
    <!--<div class="ibox"><a class="ibtn"><img src="images/iadd.png" />添加新的快捷功能</a></div>-->
    
    <div class="box"></div>
    <div class="xline"></div>
    </div>
    
    

</body>

</html>
