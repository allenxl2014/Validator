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
	String outTime1Req = analysisJBean.getString("outTime1");
	String outTime2Req = analysisJBean.getString("outTime2");

	analysisJBean.setPageRows(16);//设定每页显示多少行	
	httpRequest.putParameter(ValidatorDataStructure.ORDERBY, "outTime DESC");
	
	Element dataInfosEL = analysisJBean.getOuterList();
	int pageRowTotal = analysisJBean.getPageTotalRows(dataInfosEL); 
	int pageOffset = analysisJBean.getPageOffset();
	int pageRowSize = analysisJBean.getPageRows();
	int pageTotal = analysisJBean.getPages(pageRowTotal);
	
	/* 初始化排序对象 */
	TableOrder tableOrder = new TableOrder(request);
	HashMap reqInfos = RequestHelper.parseToHashMap(httpRequest); 
	tableOrder.addField(reqInfos);
	tableOrder.setDefaultOrderField("outTime", tableOrder.DESC);
	
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
			var url = "<%=tableOrder.getFormatedURL("deliveryCount.jsp", new String[]{"PAGE_OFFSET"})%>" + "&PAGE_OFFSET=" + pageOffset + "&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
			window.location = url;
		}
	}
</script>
</head>

<body>
<!--------  Top Begin ------->
	<jsp:include page="/top.jsp" flush="false">
		<jsp:param name="current" value="monitor" />
		<jsp:param name="title" value="产品仓储出库统计报表（年  月   日）" />
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
	    <td colspan="7">
	    	<form id="base" name="base" method="post" action="deliveryCount.jsp">
				出库时间：
				<input type="text" name="outTime1" id="outTime1" value="<%=outTime1Req %>" class="searchText" onClick="WdatePicker()" readonly /> 到 
				<input type="text" name="outTime2" id="outTime2" value="<%=outTime2Req %>" class="searchText" onClick="WdatePicker()" readonly />
				<input type="submit" name="Submit" value="统计" />
			</form>
	    </td>
      </tr>
	  <tr class="listTitle">
	    <td colspan="2" align="center">合同号</td>
		<td width="20%" align="center">合同单位</td>
		<td width="15%" align="center">产品型号</td>
		<td width="15%" align="center">外标序号</td>
		<td width="15%" align="center">销售地区</td>
		<td width="15%" align="center">运输号</td>
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
	  	String shipmentId = XMLHelper.getAttributeString(dataInfoEL, "shipmentId");
	  	String contractNo = XMLHelper.getAttributeString(dataInfoEL, "contractNo");
	  	String startNo = XMLHelper.getAttributeString(dataInfoEL, "startNo");
	  	String endNo = XMLHelper.getAttributeString(dataInfoEL, "endNo");
	  	String cityName = XMLHelper.getAttributeString(dataInfoEL, "cityName");
	  	String outTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "outTime"), "yyyy-MM-dd HH:mm");
	  	String productName = XMLHelper.getAttributeString(dataInfoEL, "productName");
	  	String distName = XMLHelper.getAttributeString(dataInfoEL, "distName");
	  	String transportNo = XMLHelper.getAttributeString(dataInfoEL, "transportNo");
	  %>
	  <tr <%=className %>>
	    <td width="4%" align="center"><%=i+1 %></td>
		<td align="center"><%=contractNo %></td>
		<td align="center"><%=distName %></td>
		<td align="center"><%=productName %></td>
		<td align="center"><%=startNo + " - " + endNo %></td>
	    <td align="center"><%=cityName %></td>
	    <td align="center"><%=transportNo %></td>
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
