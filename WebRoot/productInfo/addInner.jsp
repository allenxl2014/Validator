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
		productMgrJBean.addInner();
%>
		<script>window.location="compList.jsp";</script>
<%		
		return;
	}
	
	Element contractsEL = productMgrJBean.getContractList();
	String contractSelector = new Selector("shipmentId", contractsEL, "请选择", "contractNo", "shipmentId", "", " onchange=\"chgContract(this)\"").toString();
	
	List contacts = contractsEL.getChildren();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=title %></title>
<link href="../style/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/Submitor.js"></script>
<script language="javascript">
	var contractArr = new Array();
	<%
	for(int i=0; i<contacts.size(); i++){
		Element contractEL = (Element)contacts.get(i);
		out.println("contractArr["+XMLHelper.getAttributeString(contractEL, "shipmentId")+"] = new Array('" 
			+ "', '" + XMLHelper.getAttributeString(contractEL, "distName") 
			+ "', '" + XMLHelper.getAttributeString(contractEL, "productName") 
			+ "', '" + XMLHelper.getAttributeString(contractEL, "startNo") 
			+ "', '" + XMLHelper.getAttributeString(contractEL, "endNo") 
			+ "', '" + XMLHelper.getAttributeString(contractEL, "transportNo") 
			+ "', '" + XMLHelper.getAttributeString(contractEL, "outTime") 
			+ "', '" + XMLHelper.getAttributeString(contractEL, "userName")
			+ "', '" + XMLHelper.getAttributeString(contractEL, "productId") + "')");
	}
	%>

	function chgContract(obj){
		var selectedValue = obj.options[obj.selectedIndex].value;
		if(selectedValue != ""){
			document.getElementById("distName").innerHTML = contractArr[selectedValue][1];
			document.getElementById("product").innerHTML = contractArr[selectedValue][2];
			document.getElementById("productSerial").innerHTML = contractArr[selectedValue][3] + " - " + contractArr[selectedValue][4];
			document.getElementById("startNo").value = contractArr[selectedValue][3];
			document.getElementById("endNo").value = contractArr[selectedValue][4];
			document.getElementById("transportNo").innerHTML = contractArr[selectedValue][5];
			document.getElementById("outerTime").innerHTML = contractArr[selectedValue][6];
			document.getElementById("outerUser").innerHTML = contractArr[selectedValue][7];
			document.getElementById("productId").value = contractArr[selectedValue][8];
		}
	}
	
	function saveInner(){
		if(window.confirm("确定要将该批产品入库吗？")){
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
		<jsp:param name="title" value="经销入库" />
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
		<input type="hidden" id="productId" name="productId" />
		<input type="hidden" id="startNo" name="startNo" />
		<input type="hidden" id="endNo" name="endNo" />
		<table border="0" width="100%" cellspacing="0" class="searchTable" >
			  <tr>
				<td align="center" width="60%"></td>
				<td align="center"></td>
			  </tr>
	  </table>
	<table border="0" width="100%" cellspacing="0" class="mytable" >
	  <tr>
	    <td width="15%" align="right">合同编号：</td>
	    <td width="85%" align="left"><%=contractSelector %></td>
      </tr>
	  <tr class="even">
	    <td align="right">产品型号：</td>
	    <td align="left" id="product">&nbsp;</td>
	    </tr>
	  <tr>
	    <td align="right">外标序号：</td>
	    <td align="left" id="productSerial">&nbsp;</td>
	    </tr>
	  <tr class="even">
	    <td align="right">合同单位：</td>
	    <td align="left" id="distName">&nbsp;</td>
	  </tr>
	  <tr>
	    <td align="right">运输号：</td>
	    <td align="left" id="transportNo">&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td align="right">出库人：</td>
	    <td align="left" id="outerUser">&nbsp;</td>
      </tr>
	  <tr>
	    <td align="right">出库时间：</td>
	    <td align="left" id="outerTime">&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td align="right">&nbsp;</td>
	    <td align="left"><input type="button" value="入 库" onClick="saveInner()" />&nbsp;&nbsp; <input type="button" value="取 消" onClick="window.history.back()" /></td>
      </tr>
	</table>
	</form>

	</div>
</div>
<!--------  Main End ------->
</body>
</html>
