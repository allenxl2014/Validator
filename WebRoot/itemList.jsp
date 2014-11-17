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
	
	String itemUnitReq = productJBean.getString("packType");
	String itemStatusReq = productJBean.getString("itemStatus");
	String outTime1 = productJBean.getString("outTime1");
	String outTime2 = productJBean.getString("outTime2");
	String itemSerialReq = productJBean.getString("itemSerial");
	
	String statusSelector = ddInfoUJBean.buildDDSelector("itemStatus", "itemStatus", itemStatusReq, "全部", " class=\"select1\"").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.idTabs.min.js"></script>
<script type="text/javascript" src="js/select-ui.min.js"></script>
<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
<script language="javascript">
	
	function query(){
		base.action="itemList.jsp";
		base.submit();
	}
	
$(document).ready(function(e) {
    $(".select1").uedSelect({
		width : 80			  
	});
	$(".select2").uedSelect({
		width : 167  
	});
	$(".select3").uedSelect({
		width : 100
	});
});


/* 分页，转到其它页 */
function goOtherPage( pageOffset ){	
	if(pageOffset>0 && pageOffset<=<%=pageTotal%> && pageOffset!=<%=pageOffset%>){
		var url = "<%=tableOrder.getFormatedURL("itemList.jsp", new String[]{"PAGE_OFFSET"})%>" + "&PAGE_OFFSET=" + pageOffset + "&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
		window.location = url;
	}
}


</script>
</head>

<body>
<div class="tbody">
	<div class="tleft">
        <div class="lefttop"><span></span>产品信息管理</div>
        
        <dl class="leftmenu">
        <dd>
            <div class="title">
                <a href="productList.jsp"><span><img src="images/leftico02.png" /></span>基础信息</a>
            </div>  
        </dd>    
        <dd>
            <div class="title">
                <a href="库存查询.html"><span><img src="images/leftico06.png" /></span>产品查询</a>
            </div>  
        </dd>
        </dl>
    </div>
    <div class="tmain">
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">产品信息管理</a></li>
    <li><a href="#">基础信息</a></li>
    <li><a href="#">单品详情</a></li>
    </ul>
    </div>
    
    <div class="formbody">
    <div class="formtitle"><span>52度五粮液</span></div>
    
    <div id="usual1" class="usual"> 
   
  	<div class="tabson">
    
    
    <div class="tools">
    	<form name="base" method="post">
    	<input type="hidden" name="productId" value="<%=productId%>" />
        <ul class="checkbar">
            <li><label>包装单位：</label>
                <span class="selectstyle">
				  <select name="packType" class="select1">
				  	<option value="1" <%if(itemUnitReq.equals("1")) out.print("selected"); %>>箱</option>
				  	<option value="0" <%if(itemUnitReq.equals("0")) out.print("selected"); %>>瓶</option>
			      </select>
                </span>
            </li>
            <li><label>状态：</label>
                <span class="selectstyle">
                <%=statusSelector %></span>
            </li>
            <li>
            	<label>出库日期：</label>
                <span class="selectstyle">
                <input type="text" name="outTime1" value="<%=outTime1 %>" readonly="readonly" class="dfinput" style="width:100px;" onclick="WdatePicker()" />
				至<input type="text" name="outTime2" value="<%=outTime2 %>" readonly="readonly" class="dfinput" style="width:100px;" onclick="WdatePicker()" /></span>
            </li>
            <li><label>产品序号：</label>
           	  <span><input type="text" name="itemSerial" class="dfinput" value="<%=itemSerialReq %>"  style="width:140px;"/></span>
            </li>
            
            <li><button onclick="query()" class="btn_sch">搜索</button></li>
        </ul>
    	</form>
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
            <th></th>
            <th>产品名称<i class="sort"><img src="images/px.gif" /></i></th>
            <th>包装单位</th>
            <th>产品序号</th>
            <th>生产日期</th>
            <th>出库日期</th>
            <th>销售激活</th>
            <th>状态</th>
            <th>销售时间</td>
        </tr>
        </thead>
        <tbody>
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
			String itemSerial = XMLHelper.getAttributeString(dataInfoEL, "itemSerial");
			String itemUnit = productJBean.getString("packType");
			String unit = "";
			if(itemUnit.equals("0")){
				unit = "瓶";
			}else{
				unit = "箱";
			}
			String itemStatus = ddInfoUJBean.getMSGValue("itemStatus", XMLHelper.getAttributeString(dataInfoEL, "itemStatus"));
			String itemStatusDD = ddInfoUJBean.getMSGValue("itemStatus", itemStatus);
			String canSale = XMLHelper.getAttributeString(dataInfoEL, "canSale");
			String canSaleShow = "";
			if(canSale.equals("1")){
				canSaleShow = "是";
			}else{
				canSaleShow = "否";
			}
			String outTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "outTime"), "yy-MM-dd HH:mm");
			String createTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "createTime"), "yy-MM-dd HH:mm");
			String validateTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "validateTime"), "yy-MM-dd HH:mm");
			if(itemStatus.equals("3")){
				color = "style=\"color:red\"";
			}else{
				color = "";
			}
	%>
	  <tr>
         <td><%=i+1 %></td>
         <td><%=productName %></td>
         <td><%=unit %></td>
         <td><%=itemSerial %></td>
         <td><%=createTime %></td>
         <td><%=outTime %></td>
         <td><%=canSaleShow %></td>
         <td><%=itemStatusDD %></td>
         <td><%=validateTime %></td>
	  </tr>
	<%
	  }else{
	%>
	  <tr>
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
        </tbody>
    </table>
    
   
  
    
    </div>  
       
	</div> 
 
	<script type="text/javascript"> 
      $("#usual1 ul").idTabs(); 
    </script>
    
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>
    
    </div>

	</div>
</div>
</body>

</html>
