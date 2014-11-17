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
	String validateTime1Req = analysisJBean.getString("validateTime1");
	String validateTime2Req = analysisJBean.getString("validateTime2");

	analysisJBean.setPageRows(16);//设定每页显示多少行	
	httpRequest.putParameter(ValidatorDataStructure.ORDERBY, "validateTime DESC");
	
	Element dataInfosEL = analysisJBean.getSalesList();
	int pageRowTotal = analysisJBean.getPageTotalRows(dataInfosEL); 
	int pageOffset = analysisJBean.getPageOffset();
	int pageRowSize = analysisJBean.getPageRows();
	int pageTotal = analysisJBean.getPages(pageRowTotal);
	
	/* 初始化排序对象 */
	TableOrder tableOrder = new TableOrder(request);
	HashMap reqInfos = RequestHelper.parseToHashMap(httpRequest); 
	tableOrder.addField(reqInfos);
	tableOrder.setDefaultOrderField("validateTime", tableOrder.DESC);
	
	List dataInfos = dataInfosEL.getChildren();
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

	/* 分页，转到其它页 */
	function goOtherPage( pageOffset ){	
		if(pageOffset>0 && pageOffset<=<%=pageTotal%> && pageOffset!=<%=pageOffset%>){
			var url = "<%=tableOrder.getFormatedURL("salesCount.jsp", new String[]{"PAGE_OFFSET"})%>" + "&PAGE_OFFSET=" + pageOffset + "&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
			window.location = url;
		}
	}
</script>
</head>

<body>
<!--------  Top Begin ------->
	<jsp:include page="/top.jsp" flush="false">
		<jsp:param name="current" value="monitor" />
		<jsp:param name="title" value="产品终端销售统计报表（年  月  日）" />
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
			<form id="base" name="base" method="post" action="salesCount.jsp">
				销售时间：
				<input type="text" name="validateTime1" id="validateTime1" value="<%=validateTime1Req %>" class="searchText" onClick="WdatePicker()" readonly /> 到 
				<input type="text" name="validateTime2" id="validateTime2" value="<%=validateTime2Req %>" class="searchText" onClick="WdatePicker()" readonly />
				<input type="submit" name="Submit" value="统计" />
			</form>

		</td>
      </tr>
	  <tr class="listTitle">
	    <td colspan="2" rowspan="2" align="center" class="marginListTitle">产品型号</td>
		<td width="15%" rowspan="2" align="center" class="marginListTitle">外标序号</td>
		<td width="15%" rowspan="2" align="center" class="marginListTitle">内标序号</td>
		<td width="15%" rowspan="2" align="center" class="marginListTitle">销售时间</td>
		<td colspan="2" align="center">产品销售地异常</td>
	    <td colspan="2" align="center">真伪鉴定</td>
      </tr>
	  <tr class="listTitle">
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
	  	String productName = XMLHelper.getAttributeString(dataInfoEL, "productName");
	  	String productSerial = XMLHelper.getAttributeString(dataInfoEL, "productSerial");
	  	String productNo = XMLHelper.getAttributeString(dataInfoEL, "productNo");
	  	String itemUnit = XMLHelper.getAttributeString(dataInfoEL, "itemUnit");
	  	if(itemUnit.equals("1")){
	  		itemUnit = "箱 ";
	  	}else{
	  		itemUnit = "瓶";
	  	}
	  	String itemStatus = XMLHelper.getAttributeString(dataInfoEL, "itemStatus");
	  	String isValidate = XMLHelper.getAttributeString(dataInfoEL, "isValidate");
	  	String validateTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "validateTime"), "yyyy-MM-dd HH:mm");
	  %>
	  <tr <%=className %>>
	    <td width="4%" align="center"><%=i+1 %></td>
		<td align="center"><%=productName %></td>
		<td align="center"><%=productSerial %></td>
		<td align="center"><%=productNo %></td>
		<td align="center"><%=validateTime %></td>
	    <td align="center"></td>
	    <td align="center">&nbsp;</td>
        <td align="center"></td>
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
	
     <!--分页开始 -->
    <div class="splitPage">
			<jsp:include page="/common/pagination.jsp" flush="false">
				<jsp:param name="PAGE_ROW_TOTAL" value="<%=String.valueOf(pageRowTotal)%>" />
				<jsp:param name="PAGE_OFFSET" value="<%=String.valueOf(pageOffset)%>" />
				<jsp:param name="PAGE_ROW_SIZE" value="<%=String.valueOf(pageRowSize)%>" />
				<jsp:param name="ROW_TOTAL" value="<%=dataInfos.size()%>" />
			</jsp:include>
            <div class="clear"></div>
      </div>
      <!--分页 结束 -->

	</div>
</div>
<!--------  Main End ------->
</body>
</html>
