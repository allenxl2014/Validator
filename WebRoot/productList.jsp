<%@page import="org.fto.jthink.j2ee.web.util.HTMLHelper"%>
<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="/common/error.jsp" %>
<%@include file="../common/resource.jsp" %>
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
<%
	productJBean.initialize(request);
	userInfoUJBean.initialize(request);
	
	String action = productJBean.getString("ACTION");

	productJBean.setPageRows(10);//设定每页显示多少行	
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
	
	String productNameReq = request.getParameter("productName");
	productNameReq = productNameReq == null ? "" : HTMLHelper.toHTMLString(productNameReq.trim(), HTMLHelper.ES_DEF$INPUT);
	String addtime1 = productJBean.getString("addtime1");
	String addtime2 = productJBean.getString("addtime2");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=title %></title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/select-ui.min.js"></script>
<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
<script language="javascript">
$(function(){	
	//导航切换
	$(".imglist li").click(function(){
		$(".imglist li.selected").removeClass("selected")
		$(this).addClass("selected");
	})	
})	
</script>
<script type="text/javascript">
$(document).ready(function(){
  $(".click").click(function(){
  $(".tip").fadeIn(200);
  });
  
  $(".tiptop a").click(function(){
  $(".tip").fadeOut(200);
});

  $(".sure").click(function(){
  $(".tip").fadeOut(100);
});

  $(".cancel").click(function(){
  $(".tip").fadeOut(100);
});

});
$(document).ready(function(e) {
    $(".select1").uedSelect({
		width : 345			  
	});
	$(".select2").uedSelect({
		width : 100  
	});
	$(".select3").uedSelect({
		width : 100
	});
});



function query(){
	base.action="productList.jsp";
	base.submit();
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
    </ul>
    </div>
    
    <div class="rightinfo">
    
    <div class="tools">
    
		<form name="base" method="post">
        <ul class="checkbar">
            <li>
            	<label>创建时间：</label>
                <span class="selectstyle"><input type="text" name="addtime1" id="addtime1" class="dfinput" value="<%=addtime1 %>" readonly onClick="WdatePicker()" style="width:100px;"/></span>
                -
                <span class="selectstyle"><input type="text" name="addtime2" id="addtime2" class="dfinput" value="<%=addtime2 %>" readonly onClick="WdatePicker()" style="width:100px;"/></span>
            </li>
            <li><label>产品名称：</label>
           	  <span><input type="text" name="productName" id="productName" class="dfinput" value="<%=productNameReq %>" style="width:200px;"/></span>
            </li>
            
            <li><button onclick="query()" class="btn_sch">搜索</button></li>
        </ul>
    	</form>
    </div>
    
    
    <table class="imgtable">
    
    <thead>
    <tr>
    <th width="50"></th>
    <th>产品名称</th>
    <th>厂家</th>
    <th>品牌</th>
    <th>产地</th>
    <th>有效期</th>
    <th>操作</th>
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
			String productId = XMLHelper.getAttributeString(dataInfoEL, "productId");
			String productName = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "productName"));
			String productAds = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "productAds"));
			String productAddr = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "productAddr"));
			String productCreator = XMLHelper.getAttributeString(dataInfoEL, "productCreator");
			String productPeriod = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "productPeriod"), "yy-MM-dd HH:mm");
			String addtime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(dataInfoEL, "addtime"), "yy-MM-dd HH:mm");
	%>
	  <tr>
	    <td><%=i+1 %></td>
	    <td style="padding:10px 0;"><a href="itemList.jsp?productId=<%=productId %>&productName=<%=productName%>"><%=productName %></a><p>创建时间：<%=addtime %></p></td>
	    <td><%=productCreator %></td>
	    <td><%=productAds %></td>
	    <td><%=productAddr %></td>
	    <td><%=productPeriod %></td>
	    <td><a href="itemList.jsp?productId=<%=productId %>&productName=<%=productName %>" class="tablelink">查看</a></td>
	  </tr>
	<%
	  }else{
	%>
	  <tr>
		<td>&nbsp;<p>&nbsp;</p></td>
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
   
    <div class="pagin">
    	<div class="message">共<i class="blue">1256</i>条记录，当前显示第&nbsp;<i class="blue">2&nbsp;</i>页</div>
        <ul class="paginList">
        <li class="paginItem"><a href="javascript:;"><span class="pagepre"></span></a></li>
        <li class="paginItem"><a href="javascript:;">1</a></li>
        <li class="paginItem current"><a href="javascript:;">2</a></li>
        <li class="paginItem"><a href="javascript:;">3</a></li>
        <li class="paginItem"><a href="javascript:;">4</a></li>
        <li class="paginItem"><a href="javascript:;">5</a></li>
        <li class="paginItem more"><a href="javascript:;">...</a></li>
        <li class="paginItem"><a href="javascript:;">10</a></li>
        <li class="paginItem"><a href="javascript:;"><span class="pagenxt"></span></a></li>
        </ul>
    </div>
    
    
    <div class="tip">
    	<div class="tiptop"><span>提示信息</span><a></a></div>
        
      <div class="tipinfo">
        <span><img src="images/ticon.png" /></span>
        <div class="tipright">
        <p>是否确认对信息的修改 ？</p>
        <cite>如果是请点击确定按钮 ，否则请点取消。</cite>
        </div>
        </div>
        
        <div class="tipbtn">
        <input name="" type="button"  class="sure" value="确定" />&nbsp;
        <input name="" type="button"  class="cancel" value="取消" />
        </div>
    
    </div>
    
    
    
    
    </div>
    
    <div class="tip">
    	<div class="tiptop"><span>提示信息</span><a></a></div>
        
      <div class="tipinfo">
        <span><img src="images/ticon.png" /></span>
        <div class="tipright">
        <p>是否确认对信息的修改 ？</p>
        <cite>如果是请点击确定按钮 ，否则请点取消。</cite>
        </div>
        </div>
        
        <div class="tipbtn">
        <input name="" type="button"  class="sure" value="确定" />&nbsp;
        <input name="" type="button"  class="cancel" value="取消" />
        </div>
    
    </div>
	</div>
</div>    
<script type="text/javascript">
	$('.imgtable tbody tr:odd').addClass('odd');
	</script>

 
</body>

</html>
