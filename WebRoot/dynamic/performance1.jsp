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
	String vlPhoneReq = analysisJBean.getString("vlPhone");
	String detectTime1Req = analysisJBean.getString("detectTime1");
	String detectTime2Req = analysisJBean.getString("detectTime2");
	String action = analysisJBean.getString("ACTION");
	if(action.equals("getCity")){
		out.print(new Selector("cityId", analysisJBean.getCityList(provinceIdReq), "全部", "cityName", "cityId", "", "id=\"cityId\" class=\"searchSelect\"").toString());
		return;
//	}else if(action.equals("getDist")){
//		out.print(new Selector("distId", analysisJBean.getDistList(provinceIdReq, cityIdReq), "全部", "distName", "distId", "", "id=\"distId\" class=\"searchSelect\"").toString());
//		return;
	}

	analysisJBean.setPageRows(16);//设定每页显示多少行	
	httpRequest.putParameter(ValidatorDataStructure.ORDERBY, "detectTime DESC");
	
	Element dataInfosEL = analysisJBean.getperformanceList();
	int pageRowTotal = analysisJBean.getPageTotalRows(dataInfosEL); 
	int pageOffset = analysisJBean.getPageOffset();
	int pageRowSize = analysisJBean.getPageRows();
	int pageTotal = analysisJBean.getPages(pageRowTotal);
	
	/* 初始化排序对象 */
	TableOrder tableOrder = new TableOrder(request);
	HashMap reqInfos = RequestHelper.parseToHashMap(httpRequest); 
	tableOrder.addField(reqInfos);
	tableOrder.setDefaultOrderField("detectTime", tableOrder.DESC);
	
	List dataInfos = dataInfosEL.getChildren();
	
	Element provinceEL = analysisJBean.getProvinceList();
	Element cityEL = analysisJBean.getCityList(provinceIdReq);
	Element distEL = analysisJBean.getDetectUserList();
	
	String provinceSelect = new Selector("provinceId", provinceEL, "全部", "provinceName", "provinceId", provinceIdReq, "id=\"provinceId\" onchange=\"chgProvince(this)\" class=\"searchSelect\"").toString();
	String citySelect = new Selector("cityId", cityEL, "全部", "cityName", "cityId", cityIdReq, "id=\"cityId\" class=\"searchSelect\"").toString();
	String vlPhoneSelect = new Selector("vlPhone", distEL, "全部", "descript", "phoneNum",vlPhoneReq, "id=\"descript\" class=\"searchSelect\"").toString();
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
			var url = "<%=tableOrder.getFormatedURL("performance1.jsp", new String[]{"PAGE_OFFSET"})%>" + "&PAGE_OFFSET=" + pageOffset + "&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
			window.location = url;
		}
	}
	
	function chgProvince(obj){
		if($(obj).val() != ""){
			$.ajax({
				url:"performance1.jsp?ACTION=getCity&provinceId=" + $(obj).val(),
			    type: "POST",
			    success:function(retStr){
			    	$("#citySpan").html($.trim(retStr));
//			    	var retStrs = $.trim(retStr).split("$$");
//			    	$("#citySpan").html(retStrs[0]);
//			    	$("#distSpan").html(retStrs[1]);
				}
			 });
		}
	}
	
	function chgCity(obj){
		if($(obj).val() != ""){
			$.ajax({
				url:"performance1.jsp?ACTION=getDist&provinceId=" + $("#provinceId").val() + "&cityId=" + $(obj).val(),
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
		<jsp:param name="title" value="市场监管考核表" />
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
		<form id="base" name="base" method="post" action="performance1.jsp">
	     	省份：
	        <%=provinceSelect %>
	   		 城市：
	        <span id="citySpan"><%=citySelect %></span>
	        	监督员：
	        <span id="distSpan"><%=vlPhoneSelect %></span>
			督察时间：
		<input type="text" name="detectTime1" id="detectTime1" value="<%=detectTime1Req %>" class="searchText" onClick="WdatePicker()" readonly /> 到 
		<input type="text" name="detectTime2" id="detectTime2" value="<%=detectTime2Req %>" class="searchText" onClick="WdatePicker()" readonly />
		<input type="submit" name="Submit" value="统计" />
		</form>
	    </td>
      </tr>
	  <tr class="listTitle">
	    <td colspan="2" align="center">监督地区</td>
	    <td width="10%" align="center">督察人</td>
	    <td width="10%" align="center">督察时间</td>
	    <td width="10%" align="center">产品型号</td>
	    <td width="10%" align="center">产品标号</td>
	    <td width="15%" align="center">销售地异常</td>
	    <td width="10%" align="center">真伪鉴定</td>
	    <td width="10%" align="center">精确定位</td>
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
	  	String detectId = XMLHelper.getAttributeString(dataInfoEL, "detectId");
	  	String descript = XMLHelper.getAttributeString(dataInfoEL, "descript");
	  	String productName = XMLHelper.getAttributeString(dataInfoEL, "productName");
	  	String productSeq = XMLHelper.getAttributeString(dataInfoEL, "productSeq");
	  	String positionX = XMLHelper.getAttributeString(dataInfoEL, "positionX");
	  	String positionY = XMLHelper.getAttributeString(dataInfoEL, "positionY");
	  	String detectResult = XMLHelper.getAttributeString(dataInfoEL, "detectResult");
	  	if(detectResult.equals("1")){
	  		detectResult = "真";
	  	}else{
	  		detectResult = "伪";
	  	}
	  	String sellCity = analysisJBean.getCityName(XMLHelper.getAttributeString(dataInfoEL, "cityId"));
	  	String detectCity = analysisJBean.getCityName(XMLHelper.getAttributeString(dataInfoEL, "vlCityId"));
	  	String detectTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "detectTime"), "yyyy-MM-dd HH:mm");
	  %>
	  <tr <%=className %>>
	    <td width="4%" align="center"><%=i+1 %></td>
		<td align="center"><%=sellCity %></td>
		<td align="center"><%=descript %></td>
		<td align="center"><%=detectTime %></td>
		<td align="center"><%=productName %></td>
		<td align="center"><%=productSeq %></td>
	    <td align="center"><%=detectCity %></td>
	    <td align="center"><%=detectResult %></td>
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
