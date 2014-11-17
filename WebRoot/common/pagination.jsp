<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/popError.jsp" %>
<%
	/* 总行数 */
	int _pageRowTotal = request.getParameter("PAGE_ROW_TOTAL")==null?0:Integer.parseInt(request.getParameter("PAGE_ROW_TOTAL")); 
	/* 页偏移 */
	int _pageOffset = request.getParameter("PAGE_OFFSET")==null?1:Integer.parseInt(request.getParameter("PAGE_OFFSET")); 
	/* 每页行数 */
	int _pageRowSize = request.getParameter("PAGE_ROW_SIZE")==null||request.getParameter("PAGE_ROW_SIZE").equals("0")?20:Integer.parseInt(request.getParameter("PAGE_ROW_SIZE")); 
	/* 总页数 */
	int _pageTotalNum = _pageRowTotal / _pageRowSize	+ ((_pageRowTotal % _pageRowSize) > 0 ? 1 : 0);
	
	String _position = request.getParameter("POSITION");
	_position = _position==null?"left":_position;
%>

<%//if(_pageTotalNum>1){%>
<table height="20" border="0" cellpadding="0" cellspacing="0" align="right" style="margin-right:20px;">
	<tr>
		<td align="<%=_position%>" style="height:40px; line-height:10px">
			共&nbsp;<font color="#FF3300"><strong><%=_pageTotalNum%></strong></font>&nbsp;页 第&nbsp;<font color="#FF3300"><strong><%=_pageOffset%></strong></font>&nbsp;页 
			<a onClick="goOtherPage(1)" class="page link_black" href="#a">首页</a> 	
			<a onClick="goOtherPage(<%=_pageOffset-1%>)" class="page link_black" href="#a">上页</a>
			<a onClick="goOtherPage(<%=_pageOffset+1%>)" class="page link_black" href="#a">下页</a>
			<a onClick="goOtherPage(<%=_pageTotalNum%>)" class="page link_black" href="#a">末页</a> 
			&nbsp;&nbsp;转到 
			<select name="jump" onChange="goOtherPage(this.options[this.selectedIndex].value)">
				<option value=""></option>
				<%for(int p=1;p<=_pageTotalNum;p++){%>
				<option value="<%=p%>">-<%=p%>-</option>
			<%}%>
			</select> 页

		</td>
	</tr>
</table>
<%//}%>	
