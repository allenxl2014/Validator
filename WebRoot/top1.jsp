<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%
String siteRoot = request.getContextPath(); 
String title = request.getParameter("title");
title = title==null ? "" : title;
String current = request.getParameter("current");
current = current==null ? "" : current;


%>
<table width="100%" height="66" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="333" style="background:url(<%=siteRoot%>/images/topMiddle.gif)"><img src="<%=siteRoot%>/images/topLeft.gif" width="333" height="66" /></td>
    <td style="background:url(<%=siteRoot%>/images/topMiddle.gif)">&nbsp;</td>
    <td width="523" style="background:url(<%=siteRoot%>/images/topMiddle.gif)"><img src="<%=siteRoot%>/images/topRight.gif" width="523" height="66" /></td>
  </tr>
</table>
<div style="width:100%; background:url(<%=siteRoot%>/images/left_bottom_bg.gif) bottom fixed repeat-x #E15D00;">
	<div style="background:url(<%=siteRoot%>/images/left_top_bg.gif); width:190px; height:30px; text-align:center; float:left; vertical-align:bottom;">
		 <span style="text-align:left; color:#FFFFFF; margin:auto; width:110px; height:30px; line-height:30px; background:url(<%=siteRoot%>/images/user.png) left no-repeat; padding-left:30px; display:block">欢迎您，谢亮</span>
	</div>
	<div style="background:url(<%=siteRoot%>/images/main_top_bg.gif) repeat-x; height:30px; margin-left:190px;">
		<span style="text-align:left; margin-left:20px; width:30%; height:30px; line-height:30px; background:url(<%=siteRoot%>/images/main_back.png) left no-repeat; padding-left:20px;">你现在位置 －> <%=title %></span>
		<span id="topMenu" style="text-align:right; margin-right:20px; float:right; height:30px; line-height:30px; display:inline-block;">
		<%if(current.equals("product")){ %>
		<a href="compList.jsp">公司信息</a> / <a href="productList.jsp">产品信息</a> / <a href="addOuter.jsp">产品出库</a>
			 / <a href="addInner.jsp">经销入库</a>
		<%}else{ %>
		
		<%} %>
		</span>
	</div>
	
</div>
