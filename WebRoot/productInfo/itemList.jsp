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
<jsp:useBean id="userInfoUJBean" 
             scope="page"
             class="com.liang.ujb.UserInfoUJBean">
</jsp:useBean>
<jsp:useBean id="ddInfoUJBean" 
             scope="page"
             class="com.liang.ujb.DDInfoUJBean">
</jsp:useBean>
<%
	productJBean.initialize(request);
	userInfoUJBean.initialize(request);
	ddInfoUJBean.initialize(request);
	
	String action = productJBean.getString("ACTION");
	String productId = productJBean.getString("productId");
	String productName = productJBean.getString("productName");
	productName = productName == null || productName.trim().equals("") ? "" : HTMLHelper.toHTMLString(productName, HTMLHelper.ES_DEF$INPUT);
	productJBean.setPageRows(16);//设定每页显示多少行	
	httpRequest.putParameter(ValidatorDataStructure.ORDERBY, "itemId DESC");
	
	Element dataInfosEL = productJBean.getItemList();
	int pageRowTotal = productJBean.getPageTotalRows(dataInfosEL); 
	int pageOffset = productJBean.getPageOffset();
	int pageRowSize = productJBean.getPageRows();
	int pageTotal = productJBean.getPages(pageRowTotal);
	
	/* 初始化排序对象 */
	TableOrder tableOrder = new TableOrder(request);
	HashMap reqInfos = RequestHelper.parseToHashMap(httpRequest); 
	tableOrder.addField(reqInfos);
	tableOrder.setDefaultOrderField("itemId", tableOrder.DESC);
	
	List dataInfos = dataInfosEL.getChildren();
	
	String itemUnitReq = productJBean.getString("itemUnit");
	String itemStatusReq = productJBean.getString("itemStatus");
	String contractNoReq = productJBean.getString("contractNo");
	String outTime1 = productJBean.getString("outTime1");
	String outTime2 = productJBean.getString("outTime2");
	String inTime1 = productJBean.getString("inTime1");
	String inTime2 = productJBean.getString("inTime2");
	
	String statusSelector = ddInfoUJBean.buildDDSelector("itemStatus", "itemStatus", itemStatusReq, "全部", "").toString();
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
	
	function query(){
		base.action="itemList.jsp";
		base.submit();
	}

	/* 分页，转到其它页 */
	function goOtherPage( pageOffset ){	
		if(pageOffset>0 && pageOffset<=<%=pageTotal%> && pageOffset!=<%=pageOffset%>){
			var url = "<%=tableOrder.getFormatedURL("itemList.jsp", new String[]{"PAGE_OFFSET"})%>" + "&PAGE_OFFSET=" + pageOffset + "&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
			window.location = url;
		}
	}

</script>
</head>

<body>&nbsp; 
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
	<form name="base" method="post">
		<input type="hidden" id="productId" name="productId" value="<%=productId %>" /> 
		<input type="hidden" id="productName" name="productName" value="<%=productName %>" /> 
		<table border="0" width="100%" cellspacing="0" class="searchTable" >
		  <tr>
			<td align="center" width="10%" >
			包装：			
			  <select name="itemUnit">
			  	<option value="" >全部</option>
			  	<option value="1" <%if(itemUnitReq.equals("1")) out.print("selected"); %>>箱</option>
			  	<option value="0" <%if(itemUnitReq.equals("0")) out.print("selected"); %>>瓶</option>
		      </select>
		    </td>
			<td width="14%" align="center">状态：<%=statusSelector %>
			</td>
			<td width="24%" align="center">出库时间：
			  <input type="text" name="outTime1" value="<%=outTime1 %>" readonly="readonly" class="inputTime" onclick="WdatePicker()" />
至
<input type="text" name="outTime2" value="<%=outTime2 %>" readonly="readonly" class="inputTime" onclick="WdatePicker()" /></td>
			<td align="center" width="27%">经销商入库时间：
			  <input type="text" name="inTime1" value="<%=inTime1 %>" readonly="readonly" class="inputTime" onclick="WdatePicker()" />
至
<input type="text" name="inTime2" value="<%=inTime2 %>" readonly="readonly" class="inputTime" onclick="WdatePicker()" /></td>
			<td align="center" width="15%">合同号：
		    <input type="text" name="contractNo" value="<%=contractNoReq %>" class="inputTime" /></td>
			<td align="center">
			<input type="button" onclick="query()" name="Submit" value="查 询" />			</td>
		  </tr>
		</table>
	</form>
	<table border="0" width="100%" cellspacing="0" class="mytable" >
	  <tr class="listSubTitle">
		<td width="15%" align="left">产品名称</td>
		<td width="5%" align="left">包装</td>
		<td width="10%" align="center">标号</td>
		<td width="15%" align="center">合同号</td>
		<td width="10%" align="left">状态</td>
		<td width="10%" align="center">出库时间</td>
		<td width="10%" align="center">经销商入库时间</td>
		<td width="10%" align="center">销售时间</td>
	  </tr>
	<%
		String cssClass = "";
		String color = "";
		for(int i=0; (i<dataInfos.size() || i < listLines); i++){
		  cssClass = "";
		  if(i % 2 == 0){
		  	cssClass = " class=\"even\"";
		  }
		  if(i < dataInfos.size()){
			Element dataInfoEL = (Element)dataInfos.get(i);
			String productSerial = XMLHelper.getAttributeString(dataInfoEL, "productSerial");
			String contractNo = XMLHelper.getAttributeString(dataInfoEL, "contractNo");
			String itemUnit = XMLHelper.getAttributeString(dataInfoEL, "itemUnit");
			String unit = "";
			if(itemUnit.equals("0")){
				unit = "瓶";
			}else{
				unit = "箱";
			}
			String itemStatus = ddInfoUJBean.getMSGValue("itemStatus", XMLHelper.getAttributeString(dataInfoEL, "itemStatus"));
			String itemStatusDD = ddInfoUJBean.getMSGValue("itemStatus", itemStatus);
			String outTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "outTime"), "yy-MM-dd HH:mm");
			String inTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "inTime"), "yy-MM-dd HH:mm");
			String validateTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "validateTime"), "yy-MM-dd HH:mm");
			if(itemStatus.equals("3")){
				color = "style=\"color:red\"";
			}else{
				color = "";
			}
	%>
	  <tr <%=cssClass %> <%=color %>>
		<td align="left"><%=productName %></td>
		<td align="left"><%=unit %></td>
		<td align="center"><%=productSerial %></td>
		<td align="center"><%=contractNo %></td>
		<td align="left"><%=itemStatusDD %></td>
		<td align="center"><%=outTime %></td>
		<td align="center"><%=inTime %></td>
		<td align="center"><%=validateTime %></td>
	  </tr>
	<%
	  }else{
	%>
	  <tr <%=cssClass %>>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	<%
	  }
	}
	%>
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
