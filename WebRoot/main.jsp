<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%@include file="../common/resource.jsp" %>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page import="org.fto.jthink.util.XMLHelper" %>
<jsp:useBean id="productJBean" 
             scope="page"
             class="com.liang.manage.jbean.ProductJBean">
</jsp:useBean>
<%
	productJBean.initialize(request);

	productJBean.setPageRows(12);//设定每页显示多少行	
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>FW90防伪认证系统下载</title>
<link href="style/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
</script>
</head>

<body>
<!--------  Top Begin ------->
<table width="100%" height="66" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="333" style="background:url(images/topMiddle.gif)"><img src="images/topLeft.gif" width="333" height="66" /></td>
    <td style="background:url(images/topMiddle.gif)">&nbsp;</td>
    <td width="523" style="background:url(images/topMiddle.gif)"><img src="images/topRight.gif" width="523" height="66" /></td>
  </tr>
</table>
<div style="width:100%; background:url(images/left_bottom_bg.gif) bottom fixed repeat-x #E15D00; min-height:720px;">
		<div style="background:url(images/left_top_bg.gif); width:190px; height:30px; text-align:center; float:left; vertical-align:bottom;">
			 <span style="text-align:left; color:#FFFFFF; margin:auto; width:110px; height:30px; line-height:30px; background:url(images/user.png) left no-repeat; padding-left:30px; display:block">欢迎您，谢亮</span>
		</div>
		<div style="background:url(images/main_top_bg.gif) repeat-x; width:100%; height:30px; margin-left:190px;">
		<span style="text-align:left; margin-left:20px; height:30px; line-height:30px; background:url(images/main_back.png) left no-repeat; padding-left:20px; display:block">你现在位置 －> 产品信息管理</span>
	</div>
	
</div>
<!--------  Top End ------->

<!--------  Left Begin ------->
<div style="width:190px; background:url(images/left_bottom_bg.gif) bottom fixed repeat-x #E15D00; float:left; min-height:720px;">
	<div style="margin:20px auto; width:170px;">
		<div style="background:url(images/left_main_top.gif) no-repeat; height:4px; width:170px;"></div>
		<div style="background:url(images/left_main_middle.gif) repeat-y;">
			<div style="height:10px;"></div>
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(1)"><img src="images/product.png" width="110" height="85" border="0" /></a>
				<span style="margin:10px auto; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(1)">产品信息管理</a></span>
			</div>
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(2)"><img src="images/print.png" width="110" height="85" border="0" /></a>
				<span style="margin:10px auto; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(2)">二维码打印</a></span>
			</div>
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(3)"><img src="images/sysLog.png" width="110" height="85" border="0" /></a>
				<span style="margin:10px auto; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(3)">系统动态监控</a></span>
			</div>
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(4)"><img src="images/states.png" width="110" height="85" border="0" /></a>
				<span style="margin:10px auto; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(4)">统计分析报表</a></span>
			</div>
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(5)"><img src="images/backup.png" width="110" height="85" border="0" /></a>
				<span style="margin:auto; margin-top: 10px; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(5)">系统数据库备份</a></span>
			</div>
			<div style="height:10px;"></div>
		</div>
		<div style="background:url(images/left_main_bottom.gif) no-repeat; height:4px; width:170px;"></div>
	</div>
	<div style="height:30px;"></div>
</div>
<!--------  Left End ------->

<!--------  Main Begin ------->
<div style="background:url(images/main_bg.gif) bottom repeat-x #e7eef8; margin-left:190px; min-height:720px;">
	
	<div id="dataDiv" style="width:99%; margin-top:5px; margin-left:5px; margin-right:5px;">
	<form name="base" method="post">
		<table border="0" width="100%" cellspacing="0" class="searchTable" >
		  <tr>
			<td align="center" width="20%" >
			生产厂家：<input type="text" name="textfield" class="inputType1" />
			</td>
			<td align="center" width="20%">
			产品名称：<input type="text" name="textfield2" class="inputType1" />
			</td>
			<td align="center" width="25%">
			添加时间：<input type="text" name="textfield3" class="inputTime" onClick="WdatePicker()" /> 至
			 <input type="text" name="textfield3" class="inputTime" onClick="WdatePicker()" />
			</td>
			<td align="center">
			<input type="submit" name="Submit" value="查 询" />
			</td>
		  </tr>
		</table>
	</form>
	<table border="0" width="100%" cellspacing="0" class="mytable" >
	  <tr class="listSubTitle">
		<td width="20%" align="center">产品名称</td>
		<td width="15%" align="center">品牌</td>
		<td width="20%" align="center">产地</td>
		<td width="20%" align="center">厂家</td>
		<td align="center">操作</td>
	  </tr>
	  <tr>
		<td align="center">52°五粮液375ml</td>
		<td align="center">五粮液</td>
		<td align="center">中国四川省宜宾市</td>
		<td align="center">五粮液集团</td>
		<td align="center">
			<a href="javascript:void(0)" onclick="goPage(5)">详细</a> | 
			<a href="javascript:void(0)" onclick="goPage(5)">修改</a> |  
			<a href="javascript:void(0)" onclick="goPage(5)">添加产品</a> | 
			<a href="javascript:void(0)" onclick="goPage(5)">删除</a>
		</td>
	  </tr>
	  <tr class="even">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr class="even">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr class="even">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr class="even">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr class="even">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr class="even">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr class="even">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr class="even">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
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
