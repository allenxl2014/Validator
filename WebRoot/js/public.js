/*---公用变量----*/

 function pubVar(){
	var len = 400;
 }
 
 var pubvar = new pubVar();
 var DESC_LENGTH = "400";
 var SOL_LENGTH = "800";
 var TEXT_LENGTH="100";
 var TEXT_MSG="";
 var DESC_MSG="描述的长度不能超过400！";
 var SOLUTION_MSG="解决方案的长度不能超过800！";
 var ADVICE_MSG="意见的长度不能超过800！";
 var marqueeContent=new Array();
 
 //通用提示信息常量定义
 var PROMPT_DEL = "确认执行\"删除\"操作?"
 
/*-------------------------------------------------------------------------------------------------------------
方法名称：getValue(pObject)
方法作用：获取一个HTML对象的值(INPUT TEXTAREA SELECT)
方法参数：要获取的对象



返回值：	返回该对象的值




modify by abnerchai
---------------------------------------------------------------------------------------------------------------*/
	function getValue(pObject){
		var objValue = "";
		switch((pObject.tagName).toUpperCase()){
			case "INPUT":
			case "TEXTAREA":
					objValue = pObject.value;
					break;	
			case "SELECT":
					objValue = pObject.options[pObject.selectedIndex].value;
		}
		return objValue;
	}
		
/*------------------------------------------------------------------------------------------------
方法名：
		trim():					去掉字符串左右的空格
		ltrim():				去掉字符串左边的空格
		rtrim():				去掉字符串右边的空格
		getBytesLength(str):	得到一个字符串的字节数，一个中文算两字节



		@author:彭海蛟



		
功能描述：	这三个函数与VB中的trim()、ltrim()、rtrim()相似，



			在Javascript中可直接对字符串按对象进行操作




用法举例：



			var stringTest = "	测试字符串第一行\n	测试字符串第二行\n	\n\n			";
			var result = stringTest.ltrim();//运行结果result="测试字符串第一行\n	测试字符串第二行\n	\n\n			"
			result = stringTest.rtrim();//运行结果result="	测试字符串第一行\n	测试字符串第二行"
			result = stringTest.trim();//运行结果result="测试字符串第一行\n	测试字符串第二行"
--------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------
方法名：trim()

功能描述：去掉字符串两端的空白字符




用法举例：




		var str="	待测字符串	";
		str=str.trim();//则去掉字符串两端的空白字符



----------------------------------------------------------------------------------------------------*/
	function trim()
	{
		return this.toString().ltrim().rtrim();
	}
/*---------------------------------------------------------------------------------------------------
方法名：ltrim()

功能描述：去掉字符串左端的空白字符




用法举例：



	var str="	待测字符串	";
	str=str.ltrim();//则去掉字符串左端的空白字符



------------------------------------------------------------------------------------------------------*/
	function ltrim()
	{
		return this.replace(/^\s*/gi,"");
	}

/*-----------------------------------------------------------------------------------------------------
方法名：rtrim()

功能描述：去掉字符串右端的空白字符




用法举例：




	var str="	待测字符串	";
	str=str.rtrim();//则去掉字符串右端的空白字符



-------------------------------------------------------------------------------------------------------*/
	function rtrim()
	{
		return this.replace(/\s*$/gi,"");
	}
		//赋予JavaSrcipt的String对象拥有此方法



		
	String.prototype.trim=trim;
	String.prototype.ltrim=ltrim;
	String.prototype.rtrim=rtrim;
/*-End String Function----------------------------------------------------------------------------------*/

/*-------------------------------------------------------------------------------------------------------
方法名：getBytesLength(str)

功能描述：得到一个字符串的字节数，一个中文算两字节




参数说明：



		str:待测字符串



---------------------------------------------------------------------------------------------------------*/
	function getBytesLength(str)
	{
		var re=/[\x00-\xff]/g;
		var len=str.length;
		var array=str.match(re);
		if (array==null)
		{
			array="";
		}
		return len*2 - array.length;
	}
/*-------------------------------------------------------------------------------------------------------
方法名：CHlength()

功能描述：



		获得一个中文字符串的字节数，一个中文算两字节



		
用法举例：




	var str="待测字符串";
	str=str.CHlength();//获得该字串的字节数



---------------------------------------------------------------------------------------------------------*/
	function CHlength()
	{
		return getBytesLength(this.toString());
	}
		//使String对象拥有此方法



		String.prototype.CHlength=CHlength;	
/*---end ------------------------------------------------------------------------------------------------*/	

/*-------------------------------------------------------------------------
格式化日期，将YYYY-MM-DD HH:MM:SS,或 YYYY-MM-DD HH:MM 或 YYYY-MM-DD格式的日期



格式化成JavaScript支持的日期形式即：MM/DD/YYYY HH:MM:SS格式

调用名：_formatDate(pDate)
形参：



	pDate 格式：YYYY-MM-DD HH:MM:SS,或 YYYY-MM-DD HH:MM 或 YYYY-MM-DD
返回：



	返回格式化后的日期



说明：供外部调用

-------------------------------------------------------------------------*/
	function formatDateTo(pDate) //YYYY-MM-DD -> YYYY/MM/DD
	{
	return pDate.replace('-','/');
	/*
		var y=0,m=1,d=2;
		var tDate = _trim(pDate).split(" ");
		var sDate = tDate[0];
		var sTime = tDate[1];
		var tDate =sDate.split("-");
		if(_trim(pDate).length<=10)return tDate[y]+"/" + tDate[m] +"/"+tDate[d];
		if(_trim(pDate).length<=19)return tDate[y]+"/" + tDate[m] +"/"+tDate[d]+sTime;
		return null;
		*/
	}

/*-------------------------------------------------------------------------------
方法功能：



				补全日期功能，如将2004-09-05 补全为：2004-09-05 00:00:00或2004-09-05 23:59:59
方法名：fillDateTime(pDate,flag)
参数说明：



			pDate，需要被被全的时间串，格式如：2004-09-05
			flag,被全标识：



										1为补全为：2004-09-05 00:00:00 ；



						yyyy-mm-dd -> yyyy-mm-dd 00:00:00
						yyyy-mm-dd hh -> yyyy-mm-dd hh:00:00
						yyyy-mm-dd hh:mm ->yyyy-mm-dd hh:mm:00
						yyyy-mm-dd hh:mm:ss ->yyyy-mm-dd hh:mm:ss			
										2为补全为：2004-09-05 23:59:59格式；



						yyyy-mm-dd -> yyyy-mm-dd 23:59:59
						yyyy-mm-dd hh -> yyyy-mm-dd hh:59:59
						yyyy-mm-dd hh:mm ->yyyy-mm-dd hh:mm:59
						yyyy-mm-dd hh:mm:ss ->yyyy-mm-dd hh:mm:ss							
										其它的字串自动加补到pDate后面。



										如：fillDateTime('2004-09-03',"17:30:30")将返回2004-09-03 17:30:30
返回值：
			返回补全后的时间串。



--------------------------------------------------------------------------------*/
	function fillDateTime(pDate,flag){

		
		var pDates = pDate.trim().split(" ");
		var dates = pDates[0].split("-");
		
		var year = dates[0];
		var month = dates[1];
		var day = dates[2];

		var hour="00";
		var minute="00";
		var second="00";
		
		if(pDates.length==2){
			var times = pDates[1].split(":");
			if(times.length==1){
				hour = times[0];
			}
			if(times.length==2){
				hour = times[0];
				minute = times[1];
			}
			if(times.length==3){
				hour = times[0];
				minute = times[1];
				second = times[2];
			}
		}
		
		
		if(flag=='1'){
			return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
		}
		else if(flag=='2') {
			if(pDates.length==1){
				return year+"-"+month+"-"+day+" "+"23:59:59";
			}
			var times = pDates[1].split(":");
			if(times.length==1){
				return year+"-"+month+"-"+day+" "+hour+":59:59";
			}
			if(times.length==2){
				return year+"-"+month+"-"+day+" "+hour+":"+minute+":59";
			}
			if(times.length==3){
				return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
			}
		}
		else return year+"-"+month+"-"+day + " "+flag;
	}

/*-------------------------------------------------------------------------------
方法功能：




				补全日期功能，如将2004-09-05 补全为：2004-09-05 00:00或2004-09-05 23:59
方法名：fillTimeToMinutes(pDate,flag)
参数说明：



			参考fillDateTime(pDate,flag)
返回值：
			参考fillDateTime(pDate,flag)


--------------------------------------------------------------------------------*/

	function fillTimeToMinutes(pDate,flag){

		
		var pDates = pDate.trim().split(" ");
		var dates = pDates[0].split("-");
		
		var year = dates[0];
		var month = dates[1];
		var day = dates[2];

		var hour="00";
		var minute="00";
		//var second="00";
		
		if(pDates.length==2){
			var times = pDates[1].split(":");
			if(times.length==1){
				hour = times[0];
			}
			if(times.length==2){
				hour = times[0];
				minute = times[1];
			}
			if(times.length==3){
				hour = times[0];
				minute = times[1];
				//second = times[2];
			}
		}
		if(flag=='1'){
			return year+"-"+month+"-"+day+" "+hour+":"+minute;
		}
		else if(flag=='2') {
			if(pDates.length==1){
				return year+"-"+month+"-"+day+" "+"23:59";
			}
			var times = pDates[1].split(":");
			if(times.length==1){
				return year+"-"+month+"-"+day+" "+hour+":59";
			}
			if(times.length==2){
				return year+"-"+month+"-"+day+" "+hour+":"+minute;
			}
			if(times.length==3){
				return year+"-"+month+"-"+day+" "+hour+":"+minute;
			}
		}
		else return year+"-"+month+"-"+day + " "+flag;
	}


	/*
	补全日期时间
			pDate，需要被被全的时间串，格式如：2004-09-05

			flag, 类型,值别为

					1: 如果pDate没有时分秒,将补全时间为:(00:00),只输入了小时,补全为:(小时:00),其它,不处理

					2: 如果pDate没有时分秒,将补全时间为:(23:59),只输入了小时,补全为:(小时:59),其它,不处理

					3: 如果pDate没有时分秒,将补全时间为:(23:59), 只输入了小时,补全为:(小时:00),其它,不处理

					4: 如果pDate没有时分秒,将补全时间为:(当前服务器时间), 只输入了小时,补全为:(小时:00),其它,不处理

					5: 如果pDate没有时分秒,将补全时间为:(当前服务器时间), 只输入了小时,补全为:(小时:59),其它,不处理

					其它值:直接加在日期后面
	*/
	function fillTime(pDate,flag){
		var dt;
		switch(flag){
			case 1:
				dt = fillTimeToMinutes(pDate, 1);
				break;
			case 2:
				dt = fillTimeToMinutes(pDate, 2);
				break;
			case 3:
				if(onlyHour(pDate)){
					dt = fillTimeToMinutes(pDate, 1);
				}else{
					dt = fillTimeToMinutes(pDate, 2);
				}
				break;
			case 4:
				if(getTime(pDate)==""){
					dt = pDate+" "+getServerTime();
				}else if(onlyHour(pDate)){
					dt = fillTimeToMinutes(pDate, 1);
				}else{
					dt = pDate;
				}
				break;
			case 5:
				if(getTime(pDate)==""){
					dt = pDate+" "+getServerTime();
				}else if(onlyHour(pDate)){
					dt = fillTimeToMinutes(pDate, 2);
				}else{
					dt = pDate;
				}
				break;
			default:
				dt = fillTimeToMinutes(pDate, flag);
			
		}
		return dt;
	}
	function getTime(d){
		var ds = d.split(" ");
		if(ds.length>=2){
			return ds[1];
		}
		return "";
	}
	function onlyHour(d){
		var time = getTime(d);
		if(time==""){
			return false;
		}
		var times = time.split(":");
		if(times.length>1){
			return false;
		}
		return true;
	}
	
	/**
		返回服务器时间, 时:分

	*/
	function getServerTime(){
		return getServerDateTime("HH:mm");
	}
	
	/**
	返回服务器日期时间

	参数:
		format: 返回的日期时间格式,参考DateTimeHelper中的日期格式
	*/
	function getServerDateTime(format){
		if(!format){
		format = "";
		}
		var urlSps = (window.location+"").split("/");
		var url = urlSps[0]+"/"+urlSps[1]+"/"+urlSps[2]+"/"+urlSps[3]+"/common/datetime_getter.jsp";
		var parameters = "?FORMAT="+format;
		var xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		xmlHttp.Open("POST",url+parameters, false);
		xmlHttp.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");
		xmlHttp.send(parameters);
		return xmlHttp.responseTEXT.trim();
	}

/*-------------------------------------------------------------------------------------------------------------
@方法名称：getTiptopWindow(win)
@方法作用：返回最顶层的窗口对象



@方法参数：可选，如果给出参数，将返回参数指定的窗口的最顶层窗口，如果不给参数，将返回当前窗口的最顶层窗口
@返回值：	返回最顶层窗口对象

@author:	wen
---------------------------------------------------------------------------------------------------------------*/
	function getTiptopWindow(win){
		var cwindow = null;
		if(win){
			cwindow = win;
		}else{
			cwindow = window
		}
		while(true){
			var pwindow = cwindow.parent;
			if(cwindow == pwindow){
				break;
			}
			cwindow = pwindow;
		}
		return cwindow;
	}
	
	
/*------------------------------------------------------------------------
比较日期，返回日期之间(pDate2-pDate1)的差值，以毫秒为单位
调用名：compareDate(pDate1,pDate2)
形参：



	pDate 格式：YYYY-MM-DD HH:MM:SS,或 YYYY-MM-DD HH:MM 或 YYYY-MM-DD
返回：



	返回日期之间(pDate2-pDate1)的差值，以毫秒为单位
说明：



-------------------------------------------------------------------------*/
	function compareDate(pDate1,pDate2)
	{
		return Date.parse(_formatDate(pDate2))-Date.parse(_formatDate(pDate1));
	}


/*------------------------------------------------------------------------
验证日期输入框并选择日期，返回一个日期字符串
调用名：validateDateFormat( type )
形参：



	type ：输入框名字
返回：



	返回一个日期字符串
说明：			@author:	XL
-------------------------------------------------------------------------*/
	function validateDateFormat( type ){
		var dateTime = null;
			dateTime = document.getElementById(type).value;
			if( !vDateTime( dateTime ) && dateTime.length != 0 ){
				document.getElementById(type).value = "";
				document.getElementById(type).focus();
				return;
			}else{
				selectDate(document.getElementById(type));
			}
		
	}

	
/*------------------------------------------------------------------------
将一格式化数值串转换为数值字符串(123,456,789.12345 -> 123456789.12345)
调用名： unFormatNumber(formatedNumber)
形参：



	pNum1 = 格式化数值(123,456,789.12345)
返回：



	返回 数值 (123456789.12345)

-------------------------------------------------------------------------*/
	function unFormatNumber(formatedNumber)
	{	
		var numbers = formatedNumber.split(",");
		var numbersLength = numbers.length;
		var number = "";
		for(var i=0; i<numbersLength; i++){
			number += numbers[i];
		}
		if(number.length==0)number = "0.0";
		return number;
	}
/*-------------------------------------------------------------------------
数值比较



调用名：compareNumber(pNum1,pNum2)
形参：



	pNum1 = 数值



	pNum2 = 数值 
返回：



	返回 pNum2 - pNum1 的差值




-------------------------------------------------------------------------*/
	function compareNumber(pNum1,pNum2)
	{
		return parseFloat(pNum2)-parseFloat(pNum1);
	}

/*-------------------------------------------------------------------------
光标后输入,用于SM4X2005的短语选择
by cxp

-------------------------------------------------------------------------*/
		function storeCaret (textEl) {
			if (textEl.createTextRange) 
				textEl.caretPos = document.selection.createRange().duplicate();
		}
		function insertAtCaret (textEl, obj) {
			var text= obj.options[obj.selectedIndex].text;
			
			if (textEl.createTextRange && textEl.caretPos) {
			
				var caretPos = textEl.caretPos;
				
				caretPos.text =
					caretPos.text.charAt(caretPos.text.length - 1) == ' ' ?
						text + ' ' : text;
			}
			else
				textEl.value	= text;
				
		}
/*-------------------------------------------------------------------------
对字符进行URI编码，汉字,数字，字母除外。



形参：



	str = 串



返回：



	返回 编码后的串，比如：&%~!@# 编码为 %26%25~!%40%23

-------------------------------------------------------------------------*/
	function encodeString(str){
		var strs = str.split("");
		var uristr = "";
		for(var i=0;i<strs.length;i++){
			var c = strs[i].charCodeAt(0);
			if(c>255 || c>=0x30 && c<=0x39 || c>=0x61 && c<=0x7a || c>=0x41 && c<=0x5a){
				uristr += strs[i];
			}else{
				uristr += "%" + toHexString(c);
			}
		}
		return uristr;
	}

/*-------------------------------------------------------------------------
将整数转换为16进制字符串



形参：



	i = 整数类型的值



返回：



	返回 十六进制格式的串

-------------------------------------------------------------------------*/
	function toHexString(i){
		return toUnsignedString(i, 4);
	}

/*-------------------------------------------------------------------------
将整数转换为2,8,16进制字符串



形参：



	i = 整数类型的值



	shift = 位移量，不同的位移量，(1,3，4)分别表示二进制、八进制、十六进制



返回：



	返回 2,8,16进制格式的串

-------------------------------------------------------------------------*/
		var digits = new Array('0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8' , '9' , 'a' , 'b' ,'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,	'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,	'o' , 'p' , 'q' , 'r' , 's' , 't' ,	'u' , 'v' , 'w' , 'x' , 'y' , 'z');
		function toUnsignedString(i, shift) {
		var buf = new Array();
		var charPos = 32;
		var radix = 1 << shift;
		var mask = radix - 1;
		do {
				buf[--charPos] = digits[i & mask];
				i >>>= shift;
		} while (i != 0);
		var ustr="";
		for(var j=charPos;j<32;j++){
			ustr+=buf[j];
		}
		return ustr;
		}
/*-----------------------------------------------------------

	/**
	* 返回选定的，Radio类型的Input对象值



	*/
	function getRadioObjValue(objName){
		var objValue = null;
		var obj = getRadioObj(objName)
		if(obj!=null){
			objValue = obj.value;
		}
		return objValue;
	}

/*------------------------------------------------------------*/
	/**
	* 返回选定的，Radio类型的Input对象
	*/
	function getRadioObj(objName){
		var objRadioObj = null;
		var objRadioObjs = document.getElementsByName(objName);
		for(var i=0; i<objRadioObjs.length; i++){
			if(objRadioObjs[i]. checked == true) {
				objRadioObj = objRadioObjs[i];
			}
		}
		
		return objRadioObj;
	}
/*------------------------------------------------------------*/

/*
上传文件本地检查大小



*/
function getFileSize (fileName) {
if (document.layers) {
if (navigator.javaEnabled()) {
var file = new java.io.File(fileName);
if (location.protocol.toLowerCase() != 'file:')
netscape.security.PrivilegeManager.enablePrivilege(
'UniversalFileRead' );
return file.length(); }
else return -1; }
else if (document.all) {
window.oldOnError = window.onerror;
window.onerror = function (err) {
if (err.indexOf('utomation') != -1) {
alert('上传不能够被允许，您可以通过设置浏览器的【安全级别】来实现上传');
return true;
}
else
return false; };
var fso = new ActiveXObject('Scripting.FileSystemObject');
var file = fso.GetFile(fileName);
window.onerror = window.oldOnError;
var osize=Math.round(file.Size/1000);
return osize }}
/*-----------------------------------------------------------------

检查字段的输入大小是否查过指定值



-----------------------------------------------------------------*/
 function ckvalue(formValue,sumbytes,msg){
	var _base="base";
	
	var formSTR=document.getElementById(formValue).value;
	
	if(getBytesLength(formSTR) > sumbytes){
	
		alert(""+msg+"太长（英文"+sumbytes+"字符，中文"+(sumbytes/2)+"字）！");
		
		document.getElementById(formValue).focus();
		
		return true;
		}
 
	return false;
 }
 
 function checkValuelength(formTagName,bytesLength,msg){
 	return ckvalue(formTagName,bytesLength,msg);
 }
 
 /*----------------------------------------------------------------
	清除客户和客户id
 ----------------------------------------------------------------*/
function cleanThis(){
	
	var custId=window.base.CUST_ID.value;
	var custName=window.base.CUST_ID_CN.value;
	if(custName!=""){
		
	window.base.CUST_ID.value="";
	window.base.CUST_ID_CN.value="";
	
	}


}


 /*----------------------------------------------------------------
	清除客户和客户id
 ----------------------------------------------------------------*/
function cleanClient(){
	
	var custId=window.base.CONTRACT_CLIENT.value;
	var custName=window.base.CUST_ID_CN.value;
	if(custName!=""){
		
	window.base.CONTRACT_CLIENT.value="";
	window.base.CUST_ID_CN.value="";
	
	}

 /*----------------------------------------------------------------
	格式化浮点型数据
 ----------------------------------------------------------------*/
 function formatFloat(src, pos)
{
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
}

}


 /*----------------------------------------------------------------
	构建指定表单所有字段，结果为"param1=value1&param2=value2"
 ----------------------------------------------------------------*/
function formToRequestString(form_obj){ 
	var query_string=''; 
	var and=''; 
	//alert(form_obj.length); 
	for (i=0;i<form_obj.length;i++){ 
		e=form_obj[i];
		if (e.name!=''){ 
			if (e.type=='select-one') { 
				element_value=e.options[e.selectedIndex].value; 
			}else if (e.type=='checkbox' || e.type=='radio') { 
				if (e.checked==false) { 
					break; 
				} 
				element_value=e.value; 
	
			}else { 
				element_value=e.value; 
			} 
			query_string+=and+e.name+'='+element_value.replace(/\&/g,"%26"); 
			and="&" 
		} 
	} 
	return query_string; 
} 



function CheckNum(str,checkOK){
	var checkStr=str.trim();
	var temp=str.trim();
	var ch_h="";
	for (i=0;i<checkStr.length;i++){
		ch_h=checkStr.charAt(i);
		//判断是否为数字
		if(checkOK.indexOf(ch_h)<0){
	        temp=temp.replace(ch_h,"");		
		}
		//判断第一位是否为小数点
		else if(checkStr.substring(0,1)=='.'){
			temp=temp.replace(ch_h,"");
		}
		//判断第一位是否为0的整数
		else if(checkStr.length>1){
			if(checkStr.substring(0,1)=='0' && checkStr.substring(1,2)!='.'){
				temp=temp.replace(temp,temp.substring(1,temp.length));
				break;
			}
		}
		//判断是否有多个小数点
		if(checkStr.indexOf('.')>0){
			if((checkStr.substring(checkStr.indexOf('.')+1,checkStr.length)).indexOf('.')>=0){
				temp=temp.replace(temp,temp.substring(0,temp.length-1));
				break;
			}
		}
	}
	return temp;
}
function checkPointNumber(obj){
	if(obj.value!=''){
		obj.value=CheckNum(obj.value,"0123456789.");
		obj.focus();
	}
}
function checkBaseNumber(obj){
	if(obj.value!=''){
		obj.value=CheckNum(obj.value,"0123456789");
		obj.focus();
	}
}
function checkNumChat(obj){
	if(obj.value!=''){
		obj.value=CheckNum(obj.value,"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		obj.focus();
	}
}

function urlencode(str)//url编码
{ 
 var i,temp,p,q; 
 var result=""; 

 for(i=0;i<str.length;i++)
 {   
  temp = str.charCodeAt(i); 
   if(temp>=0x4e00)
  {    
   execScript("ascCode=hex(asc(\""+str.charAt(i)+"\"))", "vbscript");  
   	var temp = ascCode.replace(/(.{ 2 })/g, "%$1");
   	temp = "%" + temp.substr(0, 2) + "%" + temp.substr(2, 2);
    result+= temp;  
   }else{    
   result+=escape(str.charAt(i));   
  }  
 } 
 return result;
 }

function urldecode( str )
{
 var i,temp; 
 var result = "";
 
 for( i=0; i<str.length; i++ )
 {  
  if( str.charAt(i) == "%" )
  {    
   if( str.charAt(++i) == "u" )
   {     
    temp = str.charAt(i++) + str.charAt(i++) + str.charAt(i++) + str.charAt(i++) + str.charAt(i);
    result += unescape("%" + temp);
   }
   else
   { 
    temp = str.charAt(i++) + str.charAt(i);
    if( eval("0x"+temp) <= 160 )
    {     
     result += unescape( "%" + temp );  
    }
    else
    {  
     temp += str.charAt(++i) + str.charAt(++i) + str.charAt(++i); 
     result += Decode_unit("%" + temp);    
    }    
   }   
  }
  else
  {
   result += str.charAt(i); 
  }
 }
 
 return result;
}

function   getIEPosX(elt)   {   return   getIEPos(elt,"Left");   }   
function   getIEPosY(elt)   {   return   getIEPos(elt,"Top");   }   
function   getIEPos(elt,which){   
	iPos   =   0;   
	while   (elt!=null){   
		iPos   +=   elt["offset"   +   which];   
		elt   =   elt.offsetParent; 
	}   
	return   iPos;   
}
	
/*----------------------------------------------------------------
	判断浏览器的版本
 ----------------------------------------------------------------*/
	function judgeIEVersion(){
		var browser=navigator.appName 
		var b_version=navigator.appVersion 
		var version=b_version.split(";"); 
		var trim_Version=version[1].replace(/[ ]/g,""); 
		if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0") { 
			return 7;
		} else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0") {
			return 6;
		} 
	}

 /*----------------------------------------------------------------
	根据屏幕高度计算iframe高度
 ----------------------------------------------------------------*/
function getFallIframeHeight(){
	var screenHeight = window.screen.height;
	var iframeHeight = screenHeight - 214;
	return iframeHeight + "px";
}
 /*----------------------------------------------------------------
	根据屏幕高度计算iframe高度
 ----------------------------------------------------------------*/
function getCheangedIframeHeight(){
	var screenHeight = window.screen.height;
	var iframeHeight = screenHeight - 214 - 375;
	return iframeHeight + "px";
}

 /*----------------------------------------------------------------
	根据屏幕高度计算iframe高度，用于子框架
 ----------------------------------------------------------------*/
function getChildIframeHeight(menuHeight){
	var screenHeight = window.screen.height;
	var iframeHeight = screenHeight - 214 - menuHeight;
	return iframeHeight + "px";
}
 /*----------------------------------------------------------------
	根据屏幕高度计算iframe高度
 ----------------------------------------------------------------*/
function getCheangedChildIframeHeight(menuHeight){
	var screenHeight = window.screen.height;
	var iframeHeight = screenHeight - 214 - 124 - menuHeight;
	return iframeHeight + "px";
}

function clearText(obj){
	obj.value = "";
}


function getDocHeight(doc)
     {
         //在IE中doc.body.scrollHeight的可信度最高
         //在Firefox中，doc.height就可以了
         var docHei = 0;
         var scrollHei;//scrollHeight
         var offsetHei;//offsetHeight，包含了边框的高度

         if (doc.height)
         {
             //Firefox支持此属性，IE不支持
             docHei = doc.height;
         }
         else if (doc.body)
         {
             //在IE中，只有body.scrollHeight是与当前页面的高度一致的，
             //其他的跳转几次后就会变的混乱，不知道是依照什么取的值！
             //似乎跟包含它的窗口的大小变化有关
             if(doc.body.offsetHeight) docHei = offsetHei = doc.body.offsetHeight;
             if(doc.body.scrollHeight) docHei = scrollHei = doc.body.scrollHeight;
         }
         else if(doc.documentElement)
         {
             if(doc.documentElement.offsetHeight) docHei = offsetHei = doc.documentElement.offsetHeight;
             if(doc.documentElement.scrollHeight) docHei = scrollHei = doc.documentElement.scrollHeight;
         }
         /*
         docHei = Math.max(scrollHei,offsetHei);//取最大的值，某些情况下可能与实际页面高度不符！
         */
         return docHei;
     }

	function doReSize(objName, fixedHgt, changableHgt){
          var iframeWin = window.frames[objName];
          var iframeEl = window.document.getElementById? window.document.getElementById(objName): document.all? document.all[objName]: null;
          if(iframeEl && iframeWin){
              var docHei = getDocHeight(getTiptopWindow(this).document) - fixedHgt - changableHgt;
              var tmp = iframeEl.style.height;
              if (docHei != tmp){
            	  iframeEl.style.height = docHei + 'px';
              }
          }else if(iframeEl){
              var docHei = getDocHeight(getTiptopWindow(this).contentDocument) - fixedHgt - changableHgt;
              if (docHei != iframeEl.style.height) iframeEl.style.height = docHei + 'px';
          }
      }
