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
<jsp:useBean id="ddInfoUJBean" 
             scope="page"
             class="com.liang.ujb.DDInfoUJBean">
</jsp:useBean>
<%
	compJBean.initialize(request);
	userInfoUJBean.initialize(request);
	ddInfoUJBean.initialize(request);
	
	compJBean.initialize(request);
	userInfoUJBean.initialize(request);
	
	String action = compJBean.getString("ACTION");
	String compIdReq = compJBean.getString("compId");
	String provinceIdReq = compJBean.getString("provinceId");
	String cityIdReq = compJBean.getString("cityId");
	if(action.equals("getCity")){
		out.print(new Selector("cityId", userInfoUJBean.getCityList(provinceIdReq), "全部", "cityName", "cityId", "", "id=\"cityId\" class=\"select2\"").toString());
		return;
	}else if(action.equals("detect")){
		out.print(compJBean.detectComp(compIdReq));
		return;
	}else if(action.equals("writeOff")){
		compJBean.writeOffComp(compIdReq);
	}

	String isFatherReq = compJBean.getString("isFather");
	String isSellerReq = compJBean.getString("isSeller");
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
	
	Element provinceEL = userInfoUJBean.getProvinceList();
	Element cityEL = userInfoUJBean.getCityList(provinceIdReq);
	
	String provinceSelect = new Selector("provinceId", provinceEL, "全部", "provinceName", "provinceId", provinceIdReq, "id=\"provinceId\" onchange=\"chgProvince(this)\" class=\"select2\"").toString();
	String citySelect = new Selector("cityId", cityEL, "全部", "cityName", "cityId", cityIdReq, "id=\"cityId\" class=\"select2\"").toString();
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
	
	function saveComp(){
		base.action = "?ACTION=save";
		base.submit();
	}
	
	function chgProvince(obj){
		if($(obj).val() != ""){
			$.ajax({
				url:"distributorList.jsp?ACTION=getCity&provinceId=" + $(obj).val(),
			    type: "POST",
			    success:function(retStr){
			    	var retStrs = $.trim(retStr);
			    	$("#citySpan").html(retStrs);
				}
			 });
		}
	}
	
	function writeOff(compId){
		if(confirm("确定要注销该公司信息吗？")){
			window.location = "distributorList.jsp?ACTION=writeOff&compId=" + compId;
		}
	}
	
	function query(){
		base.action="distributorList.jsp";
		base.submit();
	}
	
	function writeOff(compId){
		$.ajax({
			url:"distributorList.jsp?ACTION=detect&compId=" + compId,
			data: $("#base").serialize(),
		    type: "POST",
		    success:function(retStr){
		    	if($.trim(retStr) == "0"){
		    		if(window.confirm("确定要注销该经销商吗？")){
		    			base.action="distributorList.jsp?ACTION=writeOff&compId=" + compId;
		    			base.submit();
		    		}
		    	}else{
		    		alert("对不起，该公司有下级经销商，不可注销！");
		    	}
		    }
		});
	}

	/* 分页，转到其它页 */
	function goOtherPage( pageOffset ){	
		if(pageOffset>0 && pageOffset<=<%=pageTotal%> && pageOffset!=<%=pageOffset%>){
			var url = "<%=tableOrder.getFormatedURL("distributorList.jsp", new String[]{"PAGE_OFFSET"})%>" + "&PAGE_OFFSET=" + pageOffset + "&PAGE_ROW_TOTAL=<%=pageRowTotal%>";
			window.location = url;
		}
	}

</script>
</head>


<body>
<div class="tbody">
	<div class="tleft">
        <div class="lefttop"><span></span>下级经销商</div>
        
        <dl class="leftmenu">
        <dd>
            <div class="title">
                <a href="distributorList.jsp"><span><img src="images/leftico01.png" /></span>经销商列表</a>
            </div> 
        </dd>    
        <dd>
            <div class="title">
                <a href="addDistributor.jsp"><span><img src="images/leftico01.png" /></span>添加经销商</a>
            </div>    
        </dd>
        </dl>
    </div>
    <div class="tmain">
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">经销商管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    
    <div class="tools">
    
    	<ul class="toolbar">
        <li class="click"><a href="addDistributor.jsp"><span><img src="images/t01.png" /></span>添加经销商</a></li>
       
        </ul>
        <form name="base" method="post">
        
        <ul class="checkbar">
            <li><label>省份：</label>
                <span class="selectstyle">
                <%=provinceSelect %>
                </span>
            </li>
            <li><label>城市：</label>
                <span id="citySpan" class="selectstyle">
                <%=citySelect %>
                </span>
            </li>
            <li><label>可出库：</label>
                <span id="citySpan" class="selectstyle">
                <select name="isFather" class="select2">
                	<option value="" <%if(isFatherReq.equals("")) out.print("selected"); %>>全部</option>
                	<option value="1" <%if(isFatherReq.equals("1")) out.print("selected"); %>>是</option>
                	<option value="0" <%if(isFatherReq.equals("0")) out.print("selected"); %>>否</option>
				</select>
                </span>
            </li>
            <li><label>可销售：</label>
                <span id="citySpan" class="selectstyle">
                <select name="isSeller" class="select2">
                	<option value="" <%if(isSellerReq.equals("")) out.print("selected"); %>>全部</option>
                	<option value="1" <%if(isSellerReq.equals("1")) out.print("selected"); %>>是</option>
                	<option value="0" <%if(isSellerReq.equals("0")) out.print("selected"); %>>否</option>
				</select>
                </span>
            </li>
            <li><label>经销商名称：</label>
           	  <span><input type="text" class="dfinput" name="compName" value="<%=compNameReq %>" style="width:100px;"/></span>
            </li>
            <li><button onClick="query()" class="btn_sch">搜索</button></li>
        </ul>
    </form>
    </div>
    
    
    <table class="imgtable">
    
    <thead>
    <tr>
    <th>经销商</th>
    <th>联系人</th>
    <th>联系电话</th>
    <th>公司电话</th>
    <th>公司传真</th>
    <th>是否可出库</th>
    <th>是否可销售</th>
    <th>操作</th>
    </tr>
    </thead>
    
    <tbody>
	<%
		String color = "";
		for(int i=0; (i<dataInfos.size() || i < listLines); i++){
		  if(i < dataInfos.size()){
			Element dataInfoEL = (Element)dataInfos.get(i);
			String compId = XMLHelper.getAttributeString(dataInfoEL, "compId");
			String compName = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compName"));
			String compCname = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compCname"));
			String compTele = XMLHelper.getAttributeString(dataInfoEL, "compTele");
			String compFax = XMLHelper.getAttributeString(dataInfoEL, "compFax");
			String compContact = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compContact"));
			String contactPhone = XMLHelper.getAttributeString(dataInfoEL, "contactPhone");
			String compAddr = HTMLHelper.toHTMLString(XMLHelper.getAttributeString(dataInfoEL, "compAddr"));
			String isFather = XMLHelper.getAttributeString(dataInfoEL, "isFather");
			String isFatherShow = "";
			if(isFather.equals("1")){
				isFatherShow = "是";
			}else{
				isFatherShow = "否";
			}
			String isSeller = XMLHelper.getAttributeString(dataInfoEL, "isSeller");
			String isSellerShow = "";
			if(isSeller.equals("1")){
				isSellerShow = "是";
			}else{
				isSellerShow = "否";
			}
			String status = XMLHelper.getAttributeString(dataInfoEL, "status");
			if(status.equals("0")){
				color = "style=\"color:red\"";
			}else{
				color = "";
			}
	%>
    <tr>
    <td style="padding:10px 0;"><%=compName %></td>
    <td><%=compContact %></td>
    <td><%=contactPhone %></td>
    <td><%=compTele %></td>
    <td><%=compFax %></td>
    <td><%=isFatherShow %></td>
    <td><%=isSellerShow %></td>
    <td><a href="editDistributor.jsp?compId=<%=compId %>" class="tablelink">编辑</a>&nbsp;&nbsp;|&nbsp;&nbsp;
    <a href="javascript:writeOff(<%=compId %>)" class="tablelink">注销</a></td>
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
    
</div>    
<script type="text/javascript">
	$('.imgtable tbody tr:odd').addClass('odd');
	</script>
</body>
</html>
