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
<jsp:useBean id="compJBean" 
             scope="page"
             class="com.liang.manage.jbean.CompJBean">
</jsp:useBean>
<jsp:useBean id="userInfoUJBean" 
             scope="page"
             class="com.liang.ujb.UserInfoUJBean">
</jsp:useBean>
<%
	productJBean.initialize(request);
	compJBean.initialize(request);
	userInfoUJBean.initialize(request);
	
	String action = productJBean.getString("ACTION");

	String compNameReq = request.getParameter("compName");
	compNameReq = compNameReq == null || compNameReq.trim().equals("") ? "" : HTMLHelper.toHTMLString(compNameReq, HTMLHelper.ES_DEF$INPUT);
	productJBean.setPageRows(16);//设定每页显示多少行	
	httpRequest.putParameter(ValidatorDataStructure.ORDERBY, "addtime DESC");
	
	Element dataInfosEL = productJBean.getProductList();
	int pageRowTotal = productJBean.getPageTotalRows(dataInfosEL); 
	int pageOffset = productJBean.getPageOffset();
	int pageRowSize = productJBean.getPageRows();
	int pageTotal = productJBean.getPages(pageRowTotal);
	
	/* 初始化排序对象 */
	TableOrder tableOrder = new TableOrder(request);
	HashMap reqInfos = RequestHelper.parseToHashMap(httpRequest); 
	tableOrder.addField(reqInfos);
	tableOrder.setDefaultOrderField("addtime", tableOrder.DESC);
	
	List dataInfos = dataInfosEL.getChildren();
	
	String compIdReq = compJBean.getString("compId");
	String productNameReq = request.getParameter("productName");
	productNameReq = productNameReq == null ? "" : HTMLHelper.toHTMLString(productNameReq.trim(), HTMLHelper.ES_DEF$INPUT);
	String addtime1 = productJBean.getString("addtime1");
	String addtime2 = productJBean.getString("addtime2");
	
	Element compsEL = compJBean.getComps();
	String compSelector = new Selector("compId", compsEL, "选择公司", "compCname", "compId", compIdReq, "").toString();
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
		base.action="productList.jsp";
		base.submit();
	}

	/* 分页，转到其它页 */
	function goOtherPage( pageOffset ){	
		if(pageOffset>0 && pageOffset<=<%=pageTotal%> && pageOffset!=<%=pageOffset%>){
			var url = "<%=tableOrder.getFormatedURL("productList.jsp", new String[]{"PAGE_OFFSET"})%>" + "&PAGE_OFFSET=" + pageOffset + "&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
			window.location = url;
		}
	}
	
	function goItem(productId, productName){
		window.location = "itemList.jsp?productId=" + productId + "&productName=" + encodeURI(productName);
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
	<form name="base" method="post">
		<table border="0" width="100%" cellspacing="0" class="searchTable" >
		  <tr>
			<td align="center" width="20%" >
			生产厂家：<%=compSelector %>
			</td>
			<td align="center" width="20%">
			产品名称：<input type="text" name="productName" value="<%=productNameReq %>" class="inputType1" />
			</td>
			<td align="center" width="25%">
			添加时间：<input type="text" name="addtime1" value="<%=addtime1 %>" readonly class="inputTime" onClick="WdatePicker()" /> 至
			 <input type="text" name="addtime2" value="<%=addtime2 %>" readonly class="inputTime" onClick="WdatePicker()" />
			</td>
			<td align="center">
			<input type="button" onclick="query()" name="Submit" value="查 询" />
			</td>
		  </tr>
		</table>
	</form>
	<table border="0" width="100%" cellspacing="0" class="mytable" >
	  <tr class="listSubTitle">
		<td width="15%" align="left">产品名称</td>
		<td width="15%" align="center">厂家</td>
		<td width="15%" align="center">品牌</td>
		<td width="20%" align="left">产地</td>
		<td width="10%" align="center">有效期</td>
		<td width="10%" align="center">创建时间</td>
		<td align="center">操作</td>
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
			String productId = XMLHelper.getAttributeString(dataInfoEL, "productId");
			String compCname = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compCname"));
			String productName = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "productName"));
			String productAds = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "productAds"));
			String productAddr = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "productAddr"));
			String contactPhone = XMLHelper.getAttributeString(dataInfoEL, "contactPhone");
			String compAddr = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compAddr"));
			String productPeriod = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "productPeriod"), "yy-MM-dd HH:mm");
			String addtime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "addtime"), "yy-MM-dd HH:mm");
			String status = XMLHelper.getAttributeString(dataInfoEL, "status");
			if(status.equals("0")){
				color = "style=\"color:red\"";
			}else{
				color = "";
			}
	%>
	  <tr <%=cssClass %> <%=color %>>
		<td align="left"><%=productName %></td>
		<td align="center"><%=compCname %></td>
		<td align="center"><%=productAds %></td>
		<td align="left"><%=productAddr %></td>
		<td align="center"><%=productPeriod %></td>
		<td align="center"><%=addtime %></td>
		<td align="center">
			<a href="#a" onclick="goItem('<%=productId%>', '<%=productName%>')">管理</a> |  
			<a href="editProduct.jsp?productId=<%=productId%>">修改</a> |  
			<a href="addProduct.jsp">添加产品</a>
		</td>
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
