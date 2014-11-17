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
	String provinceIdReq = analysisJBean.getString("provinceId");
	String cityIdReq = analysisJBean.getString("cityId");
	String distIdReq = analysisJBean.getString("distId");
	String validateTime1Req = analysisJBean.getString("validateTime1");
	String validateTime2Req = analysisJBean.getString("validateTime2");
	String action = analysisJBean.getString("ACTION");
	if(action.equals("getCity")){
		out.print(new Selector("cityId", analysisJBean.getCityList(provinceIdReq), "全部", "cityName", "cityId", "", "id=\"cityId\" onchange=\"chgCity(this)\" class=\"searchSelect\"").toString() + 
			"$$" + new Selector("distId", analysisJBean.getDistList(provinceIdReq, cityIdReq), "全部", "distName", "distId", "", "id=\"distId\" class=\"searchSelect\"").toString());
		return;
	}else if(action.equals("getDist")){
		out.print(new Selector("distId", analysisJBean.getDistList(provinceIdReq, cityIdReq), "全部", "distName", "distId", "", "id=\"distId\" class=\"searchSelect\"").toString());
		return;
	}
	
	List dataInfos = analysisJBean.getSellCount();
	
	Element provinceEL = analysisJBean.getProvinceList();
	Element cityEL = analysisJBean.getCityList(provinceIdReq);
	Element distEL = analysisJBean.getDistList(provinceIdReq, cityIdReq);
	
	String provinceSelect = new Selector("provinceId", provinceEL, "全部", "provinceName", "provinceId", provinceIdReq, "id=\"provinceId\" onchange=\"chgProvince(this)\" class=\"searchSelect\"").toString();
	String citySelect = new Selector("cityId", cityEL, "全部", "cityName", "cityId", cityIdReq, "id=\"cityId\" onchange=\"chgCity(this)\" class=\"searchSelect\"").toString();
	String distSelect = new Selector("distId", distEL, "全部", "distName", "distId", distIdReq, "id=\"distId\" class=\"searchSelect\"").toString();
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
	
	function chgProvince(obj){
		if($(obj).val() != ""){
			$.ajax({
				url:"marketCount.jsp?ACTION=getCity&provinceId=" + $(obj).val(),
			    type: "POST",
			    success:function(retStr){
			    	var retStrs = $.trim(retStr).split("$$");
			    	$("#citySpan").html(retStrs[0]);
			    	$("#distSpan").html(retStrs[1]);
				}
			 });
		}
	}
	
	function chgCity(obj){
		if($(obj).val() != ""){
			$.ajax({
				url:"marketCount.jsp?ACTION=getDist&provinceId=" + $("#provinceId").val() + "&cityId=" + $(obj).val(),
			    type: "POST",
			    success:function(retStr){
			    	$("#distSpan").html($.trim(retStr));
				}
			 });
		}
	}
</script>
</head>

<body>
<!--------  Top Begin ------->
	<jsp:include page="/top.jsp" flush="false">
		<jsp:param name="current" value="monitor" />
		<jsp:param name="title" value="产品销售动态市场管理统计表（年  月  日）" />
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
	    <td colspan="10">
		<form id="base" name="base" method="post" action="marketCount.jsp">
			     省份：
			        <%=provinceSelect %>
			     城市：
			        <span id="citySpan"><%=citySelect %></span>
			        销售单位：
			        <span id="distSpan"><%=distSelect %></span>
				销售时间：
				<input type="text" name="validateTime1" id="validateTime1" value="<%=validateTime1Req %>" class="searchText" onClick="WdatePicker()" readonly /> 到 
				<input type="text" name="validateTime2" id="validateTime2" value="<%=validateTime2Req %>" class="searchText" onClick="WdatePicker()" readonly />
		<input type="submit" name="Submit" value="统计" />
		</form>
		</td>
      </tr>
	  <tr class="listTitle">
	    <td colspan="2" rowspan="2" align="center" class="marginListTitle">销售地区</td>
	    <td width="15%" rowspan="2" align="center" class="marginListTitle">产品类型</td>
	    <td width="13%" rowspan="2" align="center" class="marginListTitle">销售商名称</td>
	    <td colspan="2" align="center">销售数量</td>
	    <td colspan="2" align="center">销售地异常数量</td>
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
	  	String cityName = XMLHelper.getAttributeString(dataInfoEL, "cityName");
	  	String productName = XMLHelper.getAttributeString(dataInfoEL, "productName");
	  	String distName = XMLHelper.getAttributeString(dataInfoEL, "distName");
	  	String realCountP = XMLHelper.getAttributeString(dataInfoEL, "realCountP");
	  	String realCountX = XMLHelper.getAttributeString(dataInfoEL, "realCountX");
	  	String cityCountP = XMLHelper.getAttributeString(dataInfoEL, "cityCountP");
	  	String cityCountX = XMLHelper.getAttributeString(dataInfoEL, "cityCountP");
	  %>
	  <tr <%=className %>>
	    <td width="4%" align="center"><%=i+1 %></td>
		<td align="center"><%=cityName %></td>
		<td align="center"><%=productName %></td>
		<td align="center"><%=distName %></td>
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
	    <td>&nbsp;</td>
      </tr>
      <%}} %>
	</table>

	</div>
</div>
<!--------  Main End ------->
</body>
</html>
