<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%@page import="org.fto.jthink.j2ee.web.util.HTMLHelper"%>
<%@include file="/common/resource.jsp" %>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page import="org.fto.jthink.util.XMLHelper" %>
<jsp:useBean id="productJBean" 
             scope="page"
             class="com.liang.manage.jbean.ProductJBean">
</jsp:useBean>
<%
	productJBean.initialize(request);
	
	String productId = productJBean.getString("productId");
	String action = productJBean.getString("ACTION");
	if(action.equals("save")){
		productJBean.editProduct(productId);
%>
		<script>window.location="productList.jsp";</script>
<%		
		return;
	}
	
	Element productEL = productJBean.getProductInfo(productId);
	String productPhoto = XMLHelper.getAttributeString(productEL, "productPhoto");
	String productTag = XMLHelper.getAttributeString(productEL, "productTag");
	String productName = productEL.getAttributeValue("productName");
	productName = productName == null || productName.equals("") ? "" : HTMLHelper.toHTMLString(productName, HTMLHelper.ES_DEF$INPUT);
	String productAds = productEL.getAttributeValue("productAds");
	productAds = productAds == null || productAds.equals("") ? "" : HTMLHelper.toHTMLString(productAds, HTMLHelper.ES_DEF$INPUT);
	String productAddr = productEL.getAttributeValue("productAddr");
	productAddr = productAddr == null || productAddr.equals("") ? "" : HTMLHelper.toHTMLString(productAddr, HTMLHelper.ES_DEF$INPUT);
	String productCreator = productEL.getAttributeValue("productCreator");
	productCreator = productCreator == null || productCreator.equals("") ? "" : HTMLHelper.toHTMLString(productCreator, HTMLHelper.ES_DEF$INPUT);
	String productDesc = productEL.getAttributeValue("productDesc");
	productDesc = productDesc == null || productDesc.equals("") ? "" : HTMLHelper.toHTMLString(productDesc, HTMLHelper.ES_DEF$INPUT);
	String productTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(productEL, "productTime"), "yyyy-MM-dd");
	String productPeriod = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(productEL, "productPeriod"), "yyyy-MM-dd");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=title %></title>
<link href="../style/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script language="javascript">
	function saveProduct(){
		base.action = "?ACTION=save&productId=<%=productId%>";
		base.submit();
	}
	
</script>
</head>

<body>
<!--------  Top Begin ------->
	<jsp:include page="/top.jsp" flush="false">
		<jsp:param name="current" value="product" />
		<jsp:param name="title" value="产品信息管理" />
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
	<form method="post" name="base">
		<table border="0" width="100%" cellspacing="0" class="searchTable" >
			  <tr>
				<td align="center" width="60%"></td>
				<td align="center"></td>
			  </tr>
	  </table>
	<table border="0" width="100%" cellspacing="0" class="mytable" >
	  <tr>
		<td width="15%" align="right">产品名称：</td>
		<td width="85%" align="left"><input type="text" name="productName" class="infoText" value="<%=productName %>" /></td>
	  </tr>
	  <tr>
	    <td align="right">产品品牌：</td>
	    <td align="left"><input type="text" name="productAds" class="infoText" value="<%=productAds %>" /></td>
      </tr>
	  <tr class="even">
	    <td align="right">产品产地：</td>
	    <td align="left"><input type="text" name="productAddr" class="infoText" value="<%=productAddr%>" /></td>
      </tr>
	  <tr>
	    <td align="right">生产厂家：</td>
	    <td align="left"><input type="text" name="productCreator" class="infoText" value="<%=productCreator%>" /></td>
      </tr>
	  <tr class="even">
	    <td align="right">生产时间：</td>
	    <td align="left"><input type="text" name="productTime" class="infoText" value="<%=productTime%>" readonly onClick="WdatePicker()" /></td>
      </tr>
	  <tr>
	    <td align="right">有效期：</td>
	    <td align="left"><input type="text" name="productPeriod" value="<%=productPeriod%>" readonly onClick="WdatePicker()" class="infoText" /></td>
	    </tr>
	  <tr class="even">
	    <td align="right">产品单位：</td>
	    <td align="left"><select name="productTag" class="infoSelect">
	    	<option value="X" <%if(productTag.equals("X")) out.print("selected"); %> >箱</option>
	    	<option value="P" <%if(productTag.equals("P")) out.print("selected"); %> >瓶</option>
	      </select></td>
	    </tr>
	  <tr>
	    <td align="right">产品图片：</td>
	    <td align="left"><input type="file" name="productPhoto" /></td>
      </tr>
	  <tr class="even">
	    <td align="right">产品说明：</td>
	    <td align="left"><textarea name="productDesc" class="infoArea"><%=productDesc%></textarea></td>
      </tr>
	  <tr>
	    <td align="right">&nbsp;</td>
	    <td align="left"><input type="button" value="保 存" onClick="saveProduct()" />&nbsp;&nbsp; <input type="button" value="取 消" onClick="window.history.back()" /></td>
      </tr>
	</table>
	</form>

	</div>
</div>
<!--------  Main End ------->
</body>
</html>
