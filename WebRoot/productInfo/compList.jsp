<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%@page import="org.fto.jthink.j2ee.web.util.HTMLHelper"%>
<%@include file="/common/resource.jsp" %>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page import="org.fto.jthink.util.XMLHelper" %>
<jsp:useBean id="compJBean" 
             scope="page"
             class="com.liang.manage.jbean.CompJBean">
</jsp:useBean>
<jsp:useBean id="userInfoUJBean" 
             scope="page"
             class="com.liang.ujb.UserInfoUJBean">
</jsp:useBean>
<%
	compJBean.initialize(request);
	userInfoUJBean.initialize(request);
	
	String action = compJBean.getString("ACTION");
	String compIdReq = compJBean.getString("compId");
	if(action.equals("writeOff")){
		compJBean.writeOff(compIdReq);
%>
		<script>window.location="compList.jsp";</script>
<%		
		return;
	}

	String compNameReq = request.getParameter("compName");
	compNameReq = compNameReq == null || compNameReq.trim().equals("") ? "" : HTMLHelper.toHTMLString(compNameReq, HTMLHelper.ES_DEF$INPUT);
	compJBean.setPageRows(16);//设定每页显示多少行	
	httpRequest.putParameter(ValidatorDataStructure.ORDERBY, "createTime DESC");
	
	Element dataInfosEL = compJBean.getCompList();
	int pageRowTotal = compJBean.getPageTotalRows(dataInfosEL); 
	int pageOffset = compJBean.getPageOffset();
	int pageRowSize = compJBean.getPageRows();
	int pageTotal = compJBean.getPages(pageRowTotal);
	
	/* 初始化排序对象 */
	TableOrder tableOrder = new TableOrder(request);
	HashMap reqInfos = RequestHelper.parseToHashMap(httpRequest); 
	tableOrder.addField(reqInfos);
	tableOrder.setDefaultOrderField("createTime", tableOrder.DESC);
	
	List dataInfos = dataInfosEL.getChildren();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=title %></title>
<link href="../style/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
	
	function writeOff(compId){
		if(confirm("确定要注销该公司信息吗？")){
			window.location = "compList.jsp?ACTION=writeOff&compId=" + compId;
		}
	}
	
	function query(){
		base.action="compList.jsp";
		base.submit();
	}


	/* 分页，转到其它页 */
	function goOtherPage( pageOffset ){	
		if(pageOffset>0 && pageOffset<=<%=pageTotal%> && pageOffset!=<%=pageOffset%>){
			var url = "<%=tableOrder.getFormatedURL("compList.jsp", new String[]{"PAGE_OFFSET"})%>" + "&PAGE_OFFSET=" + pageOffset + "&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
			window.location = url;
		}
	}

</script>
</head>

<body>
<!--------  Top Begin ------->
	<jsp:include page="/top.jsp" flush="false">
		<jsp:param name="current" value="product" />
		<jsp:param name="title" value="公司信息管理" />
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
			<td align="center" width="60%" >
			公司名称：<input type="text" name="compName" class="inputType1" style="width:500px" value="<%=compNameReq %>" />
			</td>
			<td align="center">
			<input type="button" onClick="query()" name="Submit" value="查 询" />
			</td>
		  </tr>
		</table>
	</form>
	<table border="0" width="100%" cellspacing="0" class="mytable" >
	  <tr class="listSubTitle">
		<td width="12%" align="left">公司简称</td>
		<td width="10%" align="center">公司电话</td>
		<td width="15%" align="left">公司邮箱</td>
		<td width="18%" align="left">公司地址</td>
		<td width="6%" align="center">联系人</td>
		<td width="6%" align="center">联系电话</td>
		<td width="6%" align="center">创建人</td>
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
			String compId = XMLHelper.getAttributeString(dataInfoEL, "compId");
			String compName = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compName"));
			String compCname = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compCname"));
			String compTele = XMLHelper.getAttributeString(dataInfoEL, "compTele");
			String compEmail = XMLHelper.getAttributeString(dataInfoEL, "compEmail");
			String compContact = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compContact"));
			String contactPhone = XMLHelper.getAttributeString(dataInfoEL, "contactPhone");
			String compAddr = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compAddr"));
			String createUid = userInfoUJBean.getUserName(XMLHelper.getAttributeString(dataInfoEL, "createUid"));
			String createTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "createTime"), "yy-MM-dd HH:mm");
			String status = XMLHelper.getAttributeString(dataInfoEL, "status");
			if(status.equals("0")){
				color = "style=\"color:red\"";
			}else{
				color = "";
			}
	%>
	  <tr <%=cssClass %> <%=color %>>
		<td align="left"><%=compCname %></td>
		<td align="center"><%=compTele %></td>
		<td align="left"><%=compEmail %></td>
		<td align="left"><%=compAddr %></td>
		<td align="center"><%=compContact %></td>
		<td align="center"><%=contactPhone %></td>
		<td align="center"><%=createUid %></td>
		<td align="center"><%=createTime %></td>
		<td align="center">
			<a href="editComp.jsp?compId=<%=compId%>">修改</a> |  
			<a href="javascript:void(0)" onClick="writeOff(<%=compId%>)">注销</a> | 
			<a href="addComp.jsp">添加公司</a>
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
