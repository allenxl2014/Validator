<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%@page import="org.fto.jthink.j2ee.web.util.HTMLHelper"%>
<%@include file="/common/resource.jsp" %>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page import="org.fto.jthink.util.XMLHelper" %>
<jsp:useBean id="productMgrJBean" 
             scope="page"
             class="com.liang.manage.jbean.ProductMgrJBean">
</jsp:useBean>
<%
	productMgrJBean.initialize(request);
	
	String action = productMgrJBean.getString("ACTION");
	if(action.equals("save")){
		productMgrJBean.addOuter();
%>
		<script>window.location="compList.jsp";</script>
<%		
		return;
	}
	
	Element productsEL = productMgrJBean.getProductList();
	String productSelector = new Selector("productId", productsEL, "请选择", "productName", "productId", "", "").toString();
	
	Element distsEL = productMgrJBean.getDistList();
	List distList = distsEL.getChildren();
	String distSelector = new Selector("distId", distsEL, "请选择", "distName", "distId", "", " onchange=\"chgCity(this)\"").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=title %></title>
<link href="../style/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/Submitor.js"></script>
<script language="javascript">
	var cityArr = new Array();
	<%
	for(int i=0; i<distList.size(); i++){
		Element distEL = (Element)distList.get(i);
		out.println("cityArr["+XMLHelper.getAttributeString(distEL, "distId")+"] = new Array('" 
			+ XMLHelper.getAttributeString(distEL, "cityId") 
			+ "', '" + XMLHelper.getAttributeString(distEL, "cityName") + "')");
	}
	%>
	
	function chgCity(obj){
		var selectedValue = obj.options[obj.selectedIndex].value;
		if(selectedValue != ""){
			document.getElementById("cityId").value = cityArr[selectedValue][0];
			document.getElementById("cityName").value = cityArr[selectedValue][1];
			document.getElementById("cityNameTd").innerHTML = cityArr[selectedValue][1];
		}
	}
	
	function saveOuter(){
		if(window.confirm("确定要将该批产品出库吗？")){
			base.action = "?ACTION=save";
			base.submit();
		}
	}
	
</script>
</head>

<body>
<!--------  Top Begin ------->
	<jsp:include page="/top.jsp" flush="false">
		<jsp:param name="current" value="product" />
		<jsp:param name="title" value="产品出库" />
	</jsp:include>
<!--------  Top End ------->

<!--------  Left Begin ------->
	<jsp:include page="/left.jsp" flush="false">
		<jsp:param name="current" value="product" />
	</jsp:include>
<!--------  Left End ------->

<!--------  Main End ------->
<div style="background:url(../images/main_bg.gif) bottom repeat-x #e7eef8; margin-left:190px; min-height:700px;">
	
	<div id="dataDiv" style="width:99%; margin-left:5px; margin-right:5px;">
	<form name="base" method="post">
		<input type="hidden" id="cityId" name="cityId" />
		<input type="hidden" id="cityName" name="cityName" />
		<table border="0" width="100%" cellspacing="0" class="searchTable" >
			  <tr>
				<td align="center" width="60%"></td>
				<td align="center"></td>
			  </tr>
	  </table>
	<table border="0" width="100%" cellspacing="0" class="mytable" >
	  <tr>
		<td width="15%" align="right">产品型号：</td>
		<td width="85%" align="left"><%=productSelector %></td>
	  </tr>
	  <tr class="even">
	    <td align="right">外标序号：</td>
	    <td align="left"><input type="text" name="startNo" class="infoText" />
			-
		  <input type="text" name="endNo" class="infoText" /></td>
	  </tr>
	  <tr>
	    <td align="right">合同编号：</td>
	    <td align="left"><input type="text" name="contractNo" class="infoText" /></td>
      </tr>
	  <tr class="even">
	    <td align="right">合同单位：</td>
	    <td align="left"><%=distSelector %></td>
	  </tr>
	  <tr>
	    <td align="right">销售地区：</td>
	    <td align="left" id="cityNameTd"></td>
	  </tr>
	  <tr class="even">
	    <td align="right">运输号：</td>
	    <td align="left"><input type="text" name="transportNo" class="infoText" /></td>
      </tr>
	  <tr class="even">
	    <td align="right">&nbsp;</td>
	    <td align="left"><input type="button" value="保 存" onClick="saveOuter()" />&nbsp;&nbsp; <input type="button" value="取 消" onClick="window.history.back()" /></td>
      </tr>
	</table>
	</form>

	</div>
</div>
<!--------  Main End ------->
</body>
</html>
