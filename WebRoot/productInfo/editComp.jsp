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
<%
	compJBean.initialize(request);
	
	String compId = compJBean.getString("compId");
	String action = compJBean.getString("ACTION");
	if(action.equals("save")){
		compJBean.editComp(compId);
%>
		<script>window.location="compList.jsp";</script>
<%		
		return;
	}
	Element dataInfoEL = compJBean.getCompInfo(compId);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=title %></title>
<link href="../style/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
	function saveComp(){
		base.action = "?ACTION=save&compId=<%=compId%>";
		base.submit();
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
				<td align="center" width="60%"></td>
				<td align="center"></td>
			  </tr>
			</table>
	<table border="0" width="100%" cellspacing="0" class="mytable" >
	  <tr>
		<td width="15%" align="right">公司名称：</td>
		<td width="85%" align="left"><input type="text" name="compName" value="<%=compJBean.getRealAttr(dataInfoEL, "compName") %>" class="infoText" /></td>
	  </tr>
	  <tr class="even">
	    <td align="right">公司简称：</td>
	    <td align="left"><input type="text" name="compCname" value="<%=compJBean.getRealAttr(dataInfoEL, "compCname") %>" class="infoText" /></td>
      </tr>
	  <tr>
	    <td align="right">联系人：</td>
	    <td align="left"><input type="text" name="compContact" value="<%=compJBean.getRealAttr(dataInfoEL, "compContact") %>" class="infoText" /></td>
      </tr>
	  <tr class="even">
	    <td align="right">联系电话：</td>
	    <td align="left"><input type="text" name="contactPhone" value="<%=compJBean.getRealAttr(dataInfoEL, "contactPhone") %>" class="infoText" /></td>
      </tr>
	  <tr>
	    <td align="right">座机电话：</td>
	    <td align="left"><input type="text" name="compTele" value="<%=compJBean.getRealAttr(dataInfoEL, "compTele") %>" class="infoText" /></td>
      </tr>
	  <tr class="even">
	    <td align="right">传真号码：</td>
	    <td align="left"><input type="text" name="compFax" value="<%=compJBean.getRealAttr(dataInfoEL, "compFax") %>" class="infoText" /></td>
      </tr>
	  <tr>
	    <td align="right">公司地址：</td>
	    <td align="left"><input type="text" name="compAddr" value="<%=compJBean.getRealAttr(dataInfoEL, "compAddr") %>" class="infoText" /></td>
      </tr>
	  <tr class="even">
	    <td align="right">电子邮件：</td>
	    <td align="left"><input type="text" name="compEmail" value="<%=compJBean.getRealAttr(dataInfoEL, "compEmail") %>" class="infoText" /></td>
      </tr>
	  <tr>
	    <td align="right">公司说明：</td>
	    <td align="left"><textarea name="textarea" class="infoArea"><%=compJBean.getRealAttr(dataInfoEL, "compDesc") %></textarea></td>
      </tr>
	  <tr class="even">
	    <td align="right">&nbsp;</td>
	    <td align="left"><input type="button" value="保 存" onclick="saveComp()" />&nbsp;&nbsp; <input type="button" value="取 消" onclick="window.history.back()" /></td>
      </tr>
	</table>
	</form>

	</div>
</div>
<!--------  Main End ------->
</body>
</html>
