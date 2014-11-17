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
	
	String compId = compJBean.getString("compId");
	String action = compJBean.getString("ACTION");
	if(action.equals("save")){
		compJBean.editComp(compId);
%>
		<script type="text/javascript">window.location = "distributorList.jsp";</script>
<%
		return;
	}
	
	Element dataInfoEL = compJBean.getCompInfo(compId);
	String provinceId = XMLHelper.getAttributeString(dataInfoEL, "provinceId");
	String cityId = XMLHelper.getAttributeString(dataInfoEL, "cityId");
	
	Element provinceEL = userInfoUJBean.getProvinceList();
	Element cityEL = userInfoUJBean.getCityList(provinceId);
	
	String provinceSelect = new Selector("provinceId", provinceEL, "全部", "provinceName", "provinceId", provinceId, "id=\"provinceId\" onchange=\"chgProvince(this)\" class=\"select2\"").toString();
	String citySelect = new Selector("cityId", cityEL, "全部", "cityName", "cityId", cityId, "id=\"cityId\" class=\"select2\"").toString();
	String compName = compJBean.getRealAttr(dataInfoEL, "compName");
	String compCname = compJBean.getRealAttr(dataInfoEL, "compCname");
	String compContact = compJBean.getRealAttr(dataInfoEL, "compContact");
	String contactPhone = compJBean.getRealAttr(dataInfoEL, "contactPhone");
	String compAddr = compJBean.getRealAttr(dataInfoEL, "compAddr");
	String compTele = compJBean.getRealAttr(dataInfoEL, "compTele");
	String compFax = compJBean.getRealAttr(dataInfoEL, "compFax");
	String compEmail = compJBean.getRealAttr(dataInfoEL, "compEmail");
	String compDesc = compJBean.getRealAttr(dataInfoEL, "compDesc");
	String status = compJBean.getRealAttr(dataInfoEL, "status");
	String isCreator = compJBean.getRealAttr(dataInfoEL, "isCreator");
	String isFather = compJBean.getRealAttr(dataInfoEL, "isFather");
	String isSeller = compJBean.getRealAttr(dataInfoEL, "isSeller");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=title %></title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.idTabs.min.js"></script>
<script type="text/javascript" src="js/select-ui.min.js"></script>
<script type="text/javascript" src="editor/kindeditor.js"></script>

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
			width : 150  
		});
		$(".select3").uedSelect({
			width : 100
		});
	});
	
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
	
	function save(){
		base.action="editDistributor.jsp?ACTION=save&compId=<%=compId%>";
		base.submit();
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
    <li><a href="经销商管理.html">经销商管理</a></li>
    <li><a href="#">添加经销商</a></li>
    </ul>
    </div>
    
    <div class="formbody">
    <div id="usual1" class="usual">
    <div class="itab">
  	<ul> 
    <li><a href="#tab1" class="selected">基本信息</a></li> 
  	</ul>
    </div> 
    <div id="tab1" class="tabson">
    <form name="base" method="post">
    <ul class="forminfo">
    <li><label>所属省份</label><span id="citySpan" class="selectstyle"><%=provinceSelect %></span></li>
    <li><label>所属城市</label><span id="citySpan" class="selectstyle"><%=citySelect %></span></li>
    <li><label>是否可出库</label><span style="display:inline-block; padding-top:10px;">
    	<input name="isSeller" type="radio" value="1" <%if(isSeller.equals("1")) out.print("checked"); %> />&nbsp;是&nbsp;&nbsp;&nbsp;
    	<input name="isSeller" type="radio" value="0" <%if(isSeller.equals("0")) out.print("checked"); %> />&nbsp;否</span></li>
    <li><label>是否可销售</label><span style="display:inline-block; padding-top:10px;">
    	<input name="isFather" type="radio" value="1" <%if(isFather.equals("1")) out.print("checked"); %> />&nbsp;是&nbsp;&nbsp;&nbsp;
    	<input name="isFather" type="radio" value="0" <%if(isFather.equals("0")) out.print("checked"); %> />&nbsp;否</span></li>
    <li><label>经销商名称</label><input name="compName" type="text" class="dfinput" value="<%=compName %>" /><i>不能超过30个字符</i></li>
    <li><label>公司简称</label><input name="compCname" type="text" class="dfinput" value="<%=compCname %>" /><i>不能超过30个字符</i></li>
    <li><label>经销商地址</label><input name="compAddr" type="text" class="dfinput" value="<%=compAddr %>" /><i>不能超过30个字符</i></li>
    <li><label>公司电话</label><input name="compTele" type="text" class="dfinput" style="width:180px" value="<%=compTele %>" /></li>
    <li><label>公司传真</label><input name="compFax" type="text" class="dfinput" style="width:180px" value="<%=compFax %>" /></li>
    <li><label>联系人</label><input name="compContact" type="text" class="dfinput" style="width:180px" value="<%=compContact %>" /></li>
    <li><label>联系电话</label><input name="contactPhone" type="text" class="dfinput"  style="width:180px" value="<%=contactPhone %>" /></li>
    <li><label>邮件地址</label><input name="compEmail" type="text" class="dfinput" style="width:180px" value="<%=compEmail %>" /></li>
    <li><label>公司简介</label><textarea name="compDesc" cols="" rows="" class="textinput"><%=compDesc %></textarea></li>
    <li><label>&nbsp;</label><input type="button" onclick="save()" class="btn" value="确认保存"/></li>
    </ul>
    </form>
    </div>
  
    </div>
	</div>
	</div>
	<script type="text/javascript"> 
      $("#usual1 ul").idTabs(); 
    </script>
    
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>
    
</div>  
</body>

</html>
