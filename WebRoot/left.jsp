<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%String siteRoot = request.getContextPath(); %>
<div style="width:190px; background:url(<%=siteRoot%>/images/left_bottom_bg.gif) bottom fixed repeat-x #E15D00; float:left; min-height:725px;">
	<div style="margin:20px auto; width:170px;">
		<div style="background:url(<%=siteRoot%>/images/left_main_top.gif) no-repeat; height:4px; width:170px;"></div>
		<div style="background:url(<%=siteRoot%>/images/left_main_middle.gif) repeat-y;">
			<div style="height:10px;"></div>
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(1)"><img src="<%=siteRoot%>/images/product.png" width="110" height="85" border="0" /></a>
				<span style="margin:10px auto; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(1)">产品信息管理</a></span>
			</div>
			<!-- 
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(2)"><img src="<%=siteRoot%>/images/print.png" width="110" height="85" border="0" /></a>
				<span style="margin:10px auto; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(2)">二维码打印</a></span>
			</div>
			 -->
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(3)"><img src="<%=siteRoot%>/images/sysLog.png" width="110" height="85" border="0" /></a>
				<span style="margin:10px auto; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(3)">系统动态监控</a></span>
			</div>
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(4)"><img src="<%=siteRoot%>/images/states.png" width="110" height="85" border="0" /></a>
				<span style="margin:10px auto; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(4)">统计分析报表</a></span>
			</div>
			<div style="text-align:center;">
				<a href="javascript:void(0)" onclick="goPage(5)"><img src="<%=siteRoot%>/images/backup.png" width="110" height="85" border="0" /></a>
				<span style="margin:auto; margin-top: 10px; display:block;"><a href="javascript:void(0)" class="menu" onclick="goPage(5)">系统数据库备份</a></span>
			</div>
			<div style="height:10px;"></div>
		</div>
		<div style="background:url(<%=siteRoot%>/images/left_main_bottom.gif) no-repeat; height:4px; width:170px;"></div>
	</div>
	<div style="height:30px;"></div>
</div>
<script language="javascript">
	function goPage(type)
	{
		if(type == 1)
		{
			window.location = "<%=siteRoot%>/productInfo/productList.jsp";
		}
		else if(type == 3)
		{
			window.location = "<%=siteRoot%>/monitor/monitor.jsp";
		}
		else if(type == 4)
		{
			window.location = "<%=siteRoot%>/dynamic/innerCount.jsp";
		}
		else
		{
			alert("稍后开放！");
			return;
		}
	}
</script>
