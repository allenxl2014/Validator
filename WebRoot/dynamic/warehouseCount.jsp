<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
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
<title>五粮液PCMS数字认证防伪系统</title>
<link href="../style/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	.btn{font-size:14px; font-family:"黑体", "宋体", System, "Times New Roman"}
</style>
<script language="javascript">
</script>
</head>

<body>
<!--------  Top Begin ------->
	<jsp:include page="/top.jsp" flush="false">
		<jsp:param name="current" value="monitor" />
		<jsp:param name="title" value="产品仓储入库统计报表（年  月   日）" />
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
			<td align="center" ><input name="button2" class="btn" type="button" onclick="window.location='innerCount.jsp';" value="产品销售终端入库验收统计" />
               <input type="button" class="btn" onclick="window.location='deliveryCount.jsp';" value="产品仓库出库统计" />
			  <input name="button" class="btn" type="button" onclick="window.location='stockCount.jsp';" value="产品终端销售库存明细统计" />
		      <input name="button3" class="btn" type="button" onclick="window.location='performance1.jsp';" value="市场监管考核表1" />
		        <input name="button4" class="btn" type="button" onclick="window.location='performance2.jsp';" value="市场监管考核统计表2" /> 
            <input type="button" class="btn" onclick="window.location='salesCount.jsp';" value="产品销售动态市场管理统计" />
            <input type="button" class="btn" onclick="window.location='marketCount.jsp';" value="产品终端销售统计" /></td>
		  </tr>
		</table>
	</form>
	<table border="0" width="100%" cellspacing="0" class="countTable" >
	  <tr class="listTitle">
	    <td colspan="2" align="center">产品型号</td>
		<td width="15%" align="center">箱数量</td>
		<td width="15%" align="center">外标起始号</td>
		<td width="15%" align="center">外标结束号</td>
		<td width="15%" align="center">沉于标数量</td>
		<td width="15%" align="center">沉于标号</td>
	  </tr>
	  <tr>
	    <td width="4%" align="center">1</td>
		<td align="center">52</td>
		<td align="center">500</td>
		<td align="center">1</td>
		<td align="center">500</td>
	    <td align="center">&nbsp;</td>
	    <td align="center">&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td align="center">2</td>
		<td align="center">52</td>
		<td align="center">300</td>
		<td align="center">1</td>
		<td align="center">300</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
	  <tr>
	    <td align="center">3</td>
		<td align="center">52</td>
		<td align="center">600</td>
		<td align="center">1</td>
		<td align="center">600</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
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
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
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
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
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
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
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
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
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
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
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
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
      </tr>
	  <tr class="even">
	    <td>&nbsp;</td>
		<td>&nbsp;</td>
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
