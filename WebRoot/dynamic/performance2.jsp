<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%@include file="/common/resource.jsp" %>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page import="org.fto.jthink.util.XMLHelper" %>
<jsp:useBean id="analysisJBean" 
             scope="page"
             class="com.liang.manage.jbean.AnalysisJBean">
</jsp:useBean>
<%
	analysisJBean.initialize(request);
	String detectTime1Req = analysisJBean.getString("detectTime1");
	String detectTime2Req = analysisJBean.getString("detectTime2");

	List dataInfos = analysisJBean.getPerformanceCount();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>五粮液PCMS数字认证防伪系统</title>
<link href="../style/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	.btn{font-size:14px; font-family:"黑体", "宋体", System, "Times New Roman"}
</style>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script language="javascript">
</script>
</head>

<body>
<!--------  Top Begin ------->
	<jsp:include page="/top.jsp" flush="false">
		<jsp:param name="current" value="monitor" />
		<jsp:param name="title" value="市场监管考核统计表" />
	</jsp:include>
<!--------  Top End ------->

<!--------  Left Begin ------->
	<jsp:include page="/left.jsp" flush="false">
		<jsp:param name="current" value="dynamic" />
	</jsp:include>
<!--------  Left End ------->

<!--------  Main End ------->
<div style="background:url(../images/main_bg.gif) bottom repeat-x #e7eef8; margin-left:190px; min-height:720px;">
	
	<div id="dataDiv" style="width:99%; margin-top:5px; margin-left:5px; margin-right:5px;">
	<form name="base" method="post">
		<table border="0" width="100%" cellspacing="0" class="searchTable" >
		  <tr>
			<td align="center" >
			<input type="button" class="btn" onclick="window.location='deliveryCount.jsp';" value="产品仓库出库统计" />
			<input name="button2" class="btn" type="button" onclick="window.location='innerCount.jsp';" value="产品销售终端入库验收统计" />
			  <input name="button" class="btn" type="button" onclick="window.location='stockCount.jsp';" value="产品终端销售库存明细统计" />
			  <input type="button" class="btn" onclick="window.location='salesCount.jsp';" value="产品销售动态市场管理统计" />
            <input type="button" class="btn" onclick="window.location='marketCount.jsp';" value="产品终端销售统计" />
		      <input name="button3" class="btn" type="button" onclick="window.location='performance1.jsp';" value="市场监管考核表" />
		        <input name="button4" class="btn" type="button" onclick="window.location='performance2.jsp';" value="市场监管考核统计表" /> 
            </td>
		  </tr>
		</table>
	</form>
	<table border="0" width="100%" cellspacing="0" class="countTable" >
	  <tr class="listTitle">
	    <td colspan="9">
			<form id="base" name="base" method="post" action="performance2.jsp">
			督察时间：
			<input type="text" name="detectTime1" id="detectTime1" value="<%=detectTime1Req %>" class="searchText" onClick="WdatePicker()" readonly /> 到 
			<input type="text" name="detectTime2" id="detectTime2" value="<%=detectTime2Req %>" class="searchText" onClick="WdatePicker()" readonly />
			<input type="submit" name="Submit" value="统计" />
			</form>
		</td>
      </tr>
	  <tr class="listTitle">
	    <td colspan="2" rowspan="2" align="center" class="marginListTitle">姓名</td>
	    <td width="10%" rowspan="2" align="center" class="marginListTitle">地区</td>
	    <td colspan="2" align="center">扫描数量</td>
	    <td colspan="2" align="center">销售地异常量</td>
	    <td colspan="2" align="center">仿冒数量</td>
      </tr>
	  <tr class="listTitle">
	    <td width="10%" align="center">箱</td>
		<td width="10%" align="center">瓶</td>
	    <td width="10%" align="center">箱</td>
        <td width="10%" align="center">瓶</td>
        <td width="10%" align="center">箱</td>
        <td width="10%" align="center">瓶</td>
      </tr>

	  <%
	  String className = "";
	  for(int i=0; i<dataInfos.size() || i < 16; i++){
	  	if(i % 2 == 0){
	  		className = " class=\"even\"";
	  	}else{
	  		className = "";
	  	}
	  	if(i<dataInfos.size()){
	  	Element dataInfoEL = (Element)dataInfos.get(i);
		String vlPhone = XMLHelper.getAttributeString(dataInfoEL, "vlPhone");
		String descript = XMLHelper.getAttributeString(dataInfoEL, "descript");
		String vlCityId = XMLHelper.getAttributeString(dataInfoEL, "vlCityId");
		String cityName = XMLHelper.getAttributeString(dataInfoEL, "cityName");
	  	String realCountP = XMLHelper.getAttributeString(dataInfoEL, "realCountP");
	  	String realCountX = XMLHelper.getAttributeString(dataInfoEL, "realCountX");
	  	String cityCountP = XMLHelper.getAttributeString(dataInfoEL, "cityCountP");
	  	String cityCountX = XMLHelper.getAttributeString(dataInfoEL, "cityCountP");
	  %>
	  <tr <%=className %>>
	    <td width="4%" align="center"><%=i+1 %></td>
		<td align="center"><%=descript %></td>
		<td align="center"><%=cityName %></td>
		<td align="center"><%=realCountX %></td>
		<td align="center"><%=realCountP %></td>
	    <td align="center"><%=cityCountX %></td>
        <td align="center"><%=cityCountP %></td>
        <td align="center">&nbsp;</td>
        <td align="center">&nbsp;</td>
      </tr>
      <%}else{ %>
	  <tr <%=className %>>
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
      <%}} %>
	</table>
	
	</div>
</div>
<!--------  Main End ------->
</body>
</html>
