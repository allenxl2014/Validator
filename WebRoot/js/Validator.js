/*----------------------------------------------------------------
(validator.js) 客户端数据验证方法(JavaScript/Jscript)
Wen jian
modified by abnerchai
加入注解，格式化代码，修改了一些bug,增加一些方法


-----------------------------------------------------------------*/
//----------------------------------------------------------------

//正则表达式中的一些特殊符号，匹配这些符号是请用\
var SpecialChar = "\\$()*+.[]?^{}|";

//----------------------------------------------------------------
//JavaScript正则表达式对象


var _RegExp = new RegExp();

/*
设置/返回 正则表达式模式开关,枚举类型[i|g|gi]，可以组合使用


g (全局搜索所有存在的 pattern)
i (忽略事件) 
m (多行搜索) 
*/
var _RegExp_switch = "g";

/*-------------------------------------------------------------------------
将一字符串转换为正则表达式模式字符串(即在属正则表达式的特殊字符前加上“\”字符)
调用名：toRegExpPattern(pStr)
形参：


	pStr			= 被转换的字符串表达式
返回：


	正则表达式模式字符串
说明:

注意:
	pStr 不能为 null 且必须定义，但可以为空字符即""
--------------------------------------------------------------------------*/
function toRegExpPattern(pStr)
{
		var patternStr = pStr;
		var specialCharS = "\\$^*+?.()|{}-[]";
		for(var l=0;l<specialCharS.length;l++)
		{
			var specialChar = specialCharS.charAt(l);
			var patternStrA = patternStr.split(specialChar);
			patternStr = patternStrA[0];
			for(var i=1;i<patternStrA.length;i++)
					patternStr += "\\" + specialChar + patternStrA[i];
		}
		return patternStr;
}
/*--------------------------------------------------------------------------
正则表达式模式检测


调用名：regExpTest(pStr, pPattern)
形参：


	pStr			= 被检测的字符串表达式
	pPattern	= 模式
返回：


	如果在pStr中存在模式pPattern 返回 true
							 否则 返回 false
说明:

--------------------------------------------------------------------------*/
function regExpTest(pStr, pPattern)
{
	_RegExp.compile(pPattern,_RegExp_switch);
	return _RegExp.test(pStr);
}

/*--------------------------------------------------------------------------
对字符串strA和strB进行比较
调用名：fnStrComp(pStrA,pStrB)
形参：


	strA,strB = 字符串表达式
返回：


	如果strA 不等于 strB 返回 -1
	如果strA 等于 strB	 返回	0
	如果strA 包涵 strB	 返回	1
	如果strB 包涵 strA	 返回	2

注意：


	strA,strB 不能为 null 且必须定义，但可以为空字符即""
-------------------------------------------------------------------------*/
function fnStrComp(pStrA,pStrB)
{
	if(regExpTest(pStrA,"^"+toRegExpPattern(pStrB)+"$")) return 0;
	else if(regExpTest(pStrA,toRegExpPattern(pStrB))) return 1;
	else if(regExpTest(pStrB,toRegExpPattern(pStrA))) return 2;
	else{
		return -1;
	}
}

/*-------------------------------------------------------------------------
检查strA中是否包涵strB中的一个或多个字符
调用名：fnStrComprise(pStrA,pStrB)
形参：


	strA,strB = 字符串表达式
返回：


	是 返回 true
	否 返回 false
说明：


	此函数用于检查一些特殊字符非常有用，只须将特殊字符放到strB里即可


注意：


	strA,strB 不能为 null 且必须定义，但可以为空字符即""
-------------------------------------------------------------------------*/
function fnStrComprise(pStrA,pStrB)
{
	if(pStrB=="") return true;
	else if(regExpTest(pStrA,"["+toRegExpPattern(pStrB)+"]")){	
		return true;
	}else{
		return false;
	}
}

/*-------------------------------------------------------------------------
检查strA中是否包涵strB中的全部字符
调用名：fnStrReside(pStrA,pStrB)
形参：


	StrA,strB = 字符串表达式
返回：


	是 返回 true
	否 返回 false
说明：


	
注意：


	strA,strB 不能为 null 且必须定义，但可以为空字符即""
-------------------------------------------------------------------------*/
function fnStrReside(pStrA,pStrB)
{
	var i = 0;
	while(i<pStrB.length)
	{
		var isHave = fnStrComp(pStrA,pStrB.charAt(i));
		if ( isHave == -1 || isHave == 2){		
			return false;
		}
		i = i+1;
	}
	return true;
}
/*-------------------------------------------------------------------------
判断对象Value值是否为空值


调用名：isNull(pObject)
形参：


	pObject = 表单元素(INPUT,SELECT,TEXTAREA)
返回：


	是空 返回 true
	否则 返回 false
说明：


	调用时请指定完整的对象名如：document.base.xxx，而不只是xxx
-------------------------------------------------------------------------*/
function isNull(pObject)
{
	switch((pObject.tagName).toUpperCase())
	{
		case "INPUT":
		case "TEXTAREA":
				if(pObject.value.length==0){
					return true;
				}
				break;	
		case "SELECT":
				if(pObject.options.length>0 && pObject.selectedIndex>=0){
					if(pObject.options[pObject.selectedIndex].value.length==0)return true;
					//如果是多选的，且当前没有一个被选择，怎么办？
				}
				else{
					return true;
				}
	}
	return false;
}

/*------------------------------------------------------------------------
判断指定对象值的对象是否存在
调用名：isExist(pObject, value)
形参：


	pObject = 表单元素对象(INPUT,SELECT,TEXTAREA)
	value	 = 该对象的值


返回：


	存在对应值的对象 返回 true
	否则						返回 false
说明：


	调用时请指定完整的对象名如：document.base.xxx，而不只是xxx
-------------------------------------------------------------------------*/
function isExist(pObject, value)
{
	switch((pObject.tagName).toUpperCase())
	{
		case "INPUT":
		case "TEXTAREA":
				if(pObject.value==value){
					return true;
				}
				break;	
		case "SELECT":
				for(var i=0; i<pObject.options.length; i++){
					if(pObject.options[i].value==value)return true;
				}
	}
	return false;
}

/*------------------------------------------------------------------------
验证日期格式，正确格式：YYYY-MM-DD,日期范围:1900-01-01 至 3000-12-31
调用名：vDate(pDate)
形参：


	pDate = 日期(10)
返回：


	正确 返回 true
	否则 返回 false
说明：供外部调用使用
-------------------------------------------------------------------------*/
function vDate(pDate)
{
	//return vDateTime(pDate);
	return _vDate(pDate);
}
/*-------------------------------------------------------------------------
验证日期时间格式，日期范围:1900-01-01 00;00:00 至 3000-12-31 23:59:59
								 正确格式包括：


													YYYY-MM-DD
													YYYY-MM-DD HH,
													YYYY-MM-DD HH:MM,
													YYYY-MM-DD HH:MM:SS
调用名：vDateTime(pDateTime)
形参：


	pDateTime = 长日期(16)
返回：


	正确 返回 true
	否则 返回 false
-------------------------------------------------------------------------*/
function vDateTime(pDateTime)
{
	if(!regExpTest(pDateTime,'^[ ]*[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}[ ]*$') && !regExpTest(pDateTime,'^[ ]*[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}[ ]*$') && !regExpTest(pDateTime,'^[ ]*[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}[ ]*$')	&& !regExpTest(pDateTime,'^[ ]*[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}[ ]*$'))return false; 	//Error datetime format
	var tDate = _trim(pDateTime).split(" ");
	var sDate = tDate[0];
	var sTime = tDate[1];
	if(!_vDate(sDate))return false; //Error date 
	if(tDate.length==2){
		var tTime = sTime.split(":");
		if(parseInt(tTime[0],10)<0 || parseInt(tTime[0],10)>23)return false;	//Error hour
		if(parseInt(tTime[1],10)<0 || parseInt(tTime[1],10)>59)return false;	//Error minute
		if(parseInt(tTime[2],10)<0 || parseInt(tTime[2],10)>59)return false;	//Error second
	}
	return true;
}

/*-------------------------------------------------------------------------
验证日期时间格式，日期范围:1900-01-01 00;00:00 至 3000-12-31 23:59:59
								 正确格式包括：

													YYYY-MM-DD HH:MM,
													YYYY-MM-DD HH:MM:SS
调用名：vDateTime2(pDateTime)
形参：


	pDateTime = 长日期(16)
返回：


	正确 返回 true
	否则 返回 false
-------------------------------------------------------------------------*/
function vDateTime2(pDateTime)
{
	if(!regExpTest(pDateTime,'^[ ]*[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}[ ]*$')	&& !regExpTest(pDateTime,'^[ ]*[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}[ ]*$'))return false; 	//Error datetime format
	var tDate = _trim(pDateTime).split(" ");
	var sDate = tDate[0];
	var sTime = tDate[1];
	if(!_vDate(sDate))return false; //Error date 
	if(tDate.length==2){
		var tTime = sTime.split(":");
		if(parseInt(tTime[0],10)<0 || parseInt(tTime[0],10)>23)return false;	//Error hour
		if(parseInt(tTime[1],10)<0 || parseInt(tTime[1],10)>59)return false;	//Error minute
		if(parseInt(tTime[2],10)<0 || parseInt(tTime[2],10)>59)return false;	//Error second
	}
	return true;
}

/*-------------------------------------------------------------------------
验证整数值


调用名：vInteger(pNum,[pIntegerDigit])
形参：


	pNum = 数值


	pIntegerDigit = 整数位数(可选)，这里的整数位数是指整数宽度从0到给定的宽度
返回：


	正确 返回 true
	否则 返回 false
-------------------------------------------------------------------------*/
function vInteger(pNum,pIntegerDigit)
{
	if((typeof pIntegerDigit).toLowerCase() == 'undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[0-9]{0,}[ ]*$') || _trim(pNum).length==0){
			return false; 	//Error Integer format
		}
	}else{
	
		if(!regExpTest(pNum,'^[ ]*[0-9]{0,'+ pIntegerDigit +'}[ ]*$') || _trim(pNum).length==0){
			return false; 	//Error Integer format
		}
		
	}
	return true;
}
/*-----------------------------------------------------------------------
验证有符号整数值


调用名：vSInteger(pNum,[pIntegerDigit])
形参：


	pNum = 数值


	pIntegerDigit = 整数位数(可选) 这里的整数位数是指整数宽度从0到给定的宽度
返回：


	正确 返回 true
	否则 返回 false
-------------------------------------------------------------------------*/

function vSInteger(pNum,pIntegerDigit)
{
	if((typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[\\-\\+]?[0-9]{0,}[ ]*$') || _trim(pNum).length==0)return false; 	//Error Integer format
	}
	else
	{
		if(!regExpTest(pNum,'^[ ]*[\\-\\+]?[0-9]{0,'+ pIntegerDigit +'}[ ]*$') || _trim(pNum).length==0)return false; 	//Error Integer format
	}
	return true;
}
/*-------------------------------------------------------------------------
验证浮点数值


调用名：vFloat(pNum,[pDecimalDigits],[pIntegerDigit])
形参：


	pNum = 数值


	pDecimalDigits = 小数位数(可选)
	pIntegerDigit = 整数位数(可选),如果给出，必须给出小数位数

isNull
返回：


	正确 返回 true
	否则 返回 false
-------------------------------------------------------------------------*/
function vFloat(pNum,pDecimalDigits,pIntegerDigit)
{
	if( _trim(pNum).length==0&&pNum.length!=0){
		return false;
	}
	if((typeof pDecimalDigits).toLowerCase() =='undefined' && (typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[0-9]{0,}(\\.[0-9]{1,})?[ ]*$') || _trim(pNum).length==0	|| _trim(pNum)==".")return false; 	//Error float format
	}
	else if((typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*([0-9]{0,})(\\.[0-9]{0,'+ pDecimalDigits +'})?[ ]*$') || _trim(pNum).length==0 || _trim(pNum)=="." )return false; 	//Error float format
	}
	else
	{
		if(!regExpTest(pNum,'^[ ]*([0-9]{0,' + pIntegerDigit + '})(\\.[0-9]{0,'+ pDecimalDigits +'})?[ ]*$') || _trim(pNum).length==0 || _trim(pNum)==".")return false; 	//Error float format
	}
	return true;
}

/*-------------------------------------------------------------------------
验证有符号浮点数值


调用名：vSFloat(pNum,[pDecimalDigits],[pIntegerDigit])
形参：


	pNum = 数值


	pDecimalDigits = 小数位数(可选)
	pIntegerDigit = 整数位数(可选),如果给出，必须给出小数位数


返回：


	正确 返回 true
	否则 返回 false	
-------------------------------------------------------------------------*/
function vSFloat(pNum,pDecimalDigits,pIntegerDigit)
{
	if((typeof pDecimalDigits).toLowerCase() =='undefined' && (typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[\\-\\+]?[0-9]{1,}(\\.[0-9]{1,})?[ ]*$') || _trim(pNum).length==0	|| _trim(pNum)==".")return false; 	//Error float format
	}
	else if((typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[\\-\\+]?([0-9]{1,})(\\.[0-9]{0,'+ pDecimalDigits +'})?[ ]*$') || _trim(pNum).length==0	|| _trim(pNum)==".")return false; 	//Error float format
	}
	else
	{
		if(!regExpTest(pNum,'^[ ]*[\\-\\+]?([0-9]{1,' + pIntegerDigit + '})(\\.[0-9]{0,'+ pDecimalDigits +'})?[ ]*$') || _trim(pNum).length==0	|| _trim(pNum)==".")return false; 	//Error float format
	}
	return true;	
}
/*------------------------------------------------------------------------

验证格式化浮点数值


调用名：vNumber(pNum,[pDecimalDigits],[pIntegerDigit])
形参：


	pNum = 数值, 如：pNum = 100,000,001.23435 , 20,001，用","三位一分隔的数值


	pDecimalDigits = 小数位数(可选)
	pIntegerDigit = 整数位数(可选),如果给出，必须给出小数位数


返回：


	正确 返回 true
	否则 返回 false
-------------------------------------------------------------------------*/
function vNumber(pNum,pDecimalDigits,pIntegerDigit)
{
	if(vFloat(pNum,pDecimalDigits,pIntegerDigit)){
		return true;
	}
	
	if((typeof pDecimalDigits).toLowerCase() =='undefined' && (typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[0-9]{1,3}(\\,[0-9]{3})*(\\.[0-9]{0,})?[ ]*$') || _trim(pNum).length==0	|| _trim(pNum)==".")return false; 	//Error float format
	}
	else if((typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[0-9]{1,3}(\\,[0-9]{3})*(\\.[0-9]{0,'+ pDecimalDigits +'})?[ ]*$') || _trim(pNum).length==0 || _trim(pNum)=="." )return false; 	//Error float format
	}
	else
	{
		var num = pNum.split(".");
		num	= num[0].split(",");
		num = num.join("");
		if(num.length>pIntegerDigit)return false;
		if(!regExpTest(pNum,'^[ ]*[0-9]{1,3}(\\,[0-9]{3})*(\\.[0-9]{0,'+ pDecimalDigits +'})?[ ]*$') || _trim(pNum).length==0 || _trim(pNum)==".")return false; 	//Error float format
	}
	return true;
}
/*-------------------------------------------------------------------------

验证有符号格式化浮点数值


调用名：vSNumber(pNum,[pDecimalDigits],[pIntegerDigit])
形参：


	pNum = 数值, pNum = -100,000,001.23435, +20,001,345.0022
	pDecimalDigits = 小数位数(可选)
	pIntegerDigit = 整数位数(可选),如果给出，必须给出小数位数


返回：


	正确 返回 true
	否则 返回 false	
-------------------------------------------------------------------------*/
function vSNumber(pNum,pDecimalDigits,pIntegerDigit)
{
	if(vSFloat(pNum,pDecimalDigits,pIntegerDigit)){
		return true;
	}

	if((typeof pDecimalDigits).toLowerCase() =='undefined' && (typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[\\-\\+]?[0-9]{1,3}(\\,[0-9]{3})*(\\.[0-9]{0,})?[ ]*$') || _trim(pNum).length==0	|| _trim(pNum)==".")return false; 	//Error float format
	}
	else if((typeof pIntegerDigit).toLowerCase() =='undefined')
	{
		if(!regExpTest(pNum,'^[ ]*[\\-\\+]?[0-9]{1,3}(\\,[0-9]{3})*(\\.[0-9]{0,'+ pDecimalDigits +'})?[ ]*$') || _trim(pNum).length==0 || _trim(pNum)=="." )return false; 	//Error float format
	}
	else
	{
		var num = pNum.split(".");
		num	= num[0].split(",");
		num = num.join("");
		if(num.length>pIntegerDigit)return false;
		if(!regExpTest(pNum,'^[ ]*[\\-\\+]?[0-9]{1,3}(\\,[0-9]{3})*(\\.[0-9]{0,'+ pDecimalDigits +'})?[ ]*$') || _trim(pNum).length==0 || _trim(pNum)==".")return false; 	//Error float format
	}
	return true;
}

/*-------------------------------------------------------------------------
验证电子邮件格式
vEmail(pStrEmail)
输入：


	pStrEmail = 电子邮件地址，如:towsvf@163.com,towsvf@263.com
返回：


	正确：true
	否则：false
-------------------------------------------------------------------------*/
function vEmail(pStrEmail)
{
	if(!regExpTest(pStrEmail,"^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$")) return false;									
	return true;
}
/*-------------------------------------------------------------------------
验证NotificationEmail格式
vNotificationEmail(pStrNotificationEmail)
输入：


	pStrNotificationEmail = Notification邮件地址，如:U_123,MEMBER@P00001,Adiminstrator,Owner@F000000001,Viewer@A000000001,QA@A000000001
返回：


	正确：true
	否则：false	
---------------------------------------------------------------------------*/	
function vNotificationEmail(pStrNotificationEmail)
{
	var tEmailList = pStrNotificationEmail.split(",");
	for(var i=0;i<tEmailList.length;i++){
		var oneEmail = tEmailList[i];
		if(fnStrComp(oneEmail,"@")==1){
			if(!regExpTest(oneEmail,"^[oO]{1}[wW]{1}[nN]{1}[eE]{1}[rR]{1}@[PFTA]{1}[0-9]{9}$|^[qQ]{1}[aA]{1}@[PFTA]{1}[0-9]{9}$|^[mM]{1}[eE]{1}[mM]{1}[bB]{1}[eE]{1}[rR]{1}@[PFTA]{1}[0-9]{9}$|^[vV]{1}[iI]{1}[eE]{1}[wW]{1}[eE]{1}[rR]{1}@[PFTA]{1}[0-9]{9}$") || oneEmail.length==0)return false;
		}
		else{
			if(!fnStrReside(UserID,oneEmail) || oneEmail.length==0)return false;
		}
	}
	return true;
	
}

/*-------------------------------------------------------------------------
验证URL地址格式
	vURL(pStrURL)
输入：


	pStrURL = URL地址 如http://www.china.com;http://www.china.com?action=xx&info=xx;ftp://ftp.china.com;https://secure.china.com等等
返回：


	正确：true
	否则：false
-------------------------------------------------------------------------*/
function vURL(pStrURL)
{
	if(!regExpTest(pStrURL,"^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$")) return false;									
	return true;
}
/*-------------------------------------------------------------------------
验证是否全是由26个大小写英文字符组成的字符串
	vEnglishChar(pStr)
输入：


	pStr = 被验证的字符串


返回：


	正确：true
	否则：false
-------------------------------------------------------------------------*/
function vEnglishChar(pStr)
{
	if(!regExpTest(pStr,"^[A-Za-z]+$")) return false;									
	return true;
}
/*-------------------------------------------------------------------------
验证是否全是由数字和26个英文字母组成的字符串


	vEnglishAndNumberChar(pStr)
输入：


	pStr = 被验证的字符串


返回：


	正确：true
	否则：false
-------------------------------------------------------------------------*/
function vEnglishAndNumberChar(pStr)
{
	if(!regExpTest(pStr,"^[A-Za-z0-9]+$")) return false;									
	return true;
}
/*-------------------------------------------------------------------------
验证是否全是由数字、26个英文字母或者下划线组成的字符串
	vEnglishAndNumberCharS(pStr)
输入：


	pStr = 被验证的字符串


返回：


	正确：true
	否则：false
-------------------------------------------------------------------------*/
function vEnglishAndNumberCharS(pStr)
{
	if(!regExpTest(pStr,"^\\w+$")) return false;									
	return true;
}

/*-------------------------------------------------------------------------
vInputWidth 验证字节宽度

输入：


	inputObj = form对象
	maxlen =最大允许输入宽度


	errMessage = 错误信息（可以不给指定参数）
返回：


	返回True（没有超出最大宽度），否则返回False
-------------------------------------------------------------------------*/

function vInputWidth(inputObj, maxlen, errMessage){
	if(getBytesLength(getValue(inputObj)>maxlen)){
		if(errMessage){
			alert(errMessage);
		}
		return false;
	}
	return true;
}


/*-------------------------------------------------------------------------
验证日期格式，正确格式：YYYY-MM-DD,日期范围:1900-01-01 至 3000-12-31
调用名：_vDate(pDate)
形参：


	pDate = 日期(10)
返回：


	正确 返回 true
	否则 返回 false
说明：此方法供内部使用


-------------------------------------------------------------------------*/
function _vDate(pDate)
{
	var sDate;
	var y=0;
	var m=1;
	var d=2;
	if(!regExpTest(pDate,'^[ ]*[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}[ ]*$'))return false; 	//Error date format
	sDate = _trim(pDate).split("-");
	if(parseInt(sDate[y],10)<1900 || parseInt(sDate[y],10)>3000)return false;				//Error year
	if(parseInt(sDate[m],10)<1 || parseInt(sDate[m],10)>12)return false;					//Error month
	if(parseInt(sDate[d],10)<1)return false		//Error day							
	if (parseInt(sDate[m],10)==2)
	{
		if (((parseInt(sDate[y],10)%4==0)&&(parseInt(sDate[y],10)%100 != 0))||(parseInt(sDate[y],10)%400==0))
		{
			if (parseInt(sDate[d],10) > 29) return false;	//Error day	
			 }
		else
		{
					if (parseInt(sDate[d],10) > 28)return false;	//Error day	
			}
		}
		if((parseInt(sDate[m],10)==4)||(parseInt(sDate[m],10)==6)||(parseInt(sDate[m],10)==9)||(parseInt(sDate[m],10)==11))
		{
		if (parseInt(sDate[d],10) > 30)return false;	//Error day	
		}

		 if ((parseInt(sDate[m],10)==1)||(parseInt(sDate[m],10)==3)||(parseInt(sDate[m],10)==5)||(parseInt(sDate[m],10)==7)||(parseInt(sDate[m],10)==8)||(parseInt(sDate[m],10)==10)||(parseInt(sDate[m],10)==12))
		 {
		if (parseInt(sDate[d],10) > 31)return false;	//Error day	
		 }
	return true;
}


/*-------------------------------------------------------------------------
格式化日期，将YYYY-MM-DD HH:MM:SS,或 YYYY-MM-DD HH:MM 或 YYYY-MM-DD格式的日期


格式化成JavaScript支持的日期形式即：MM/DD/YYYY HH:MM:SS格式

调用名：_formatDate(pDate)
形参：


	pDate 格式：YYYY-MM-DD HH:MM:SS,或 YYYY-MM-DD HH:MM 或 YYYY-MM-DD
返回：


	返回格式化后的日期


说明：供内部调用

-------------------------------------------------------------------------*/
function _formatDate(pDate) //YYYY-MM-DD -> MM/DD/YYYY
{
	var y=0,m=1,d=2;
	var tDate = _trim(pDate).split(" ");
	var sDate = tDate[0];
	var sTime = tDate[1];
	var tDate =sDate.split("-");
	if(_trim(pDate).length<=10) return tDate[m]+"/"+tDate[d]+"/"+tDate[y];
	if(_trim(pDate).length<=19) return tDate[m]+"/"+tDate[d]+"/"+tDate[y]+" "+sTime;
	return null;
}

/*-------------------------------------------------------------------------
_trim（）字符串，去掉字串两端的空格



	_trim(pString)
	
输入：


	pString = 要被去掉两端空格的字符串
返回：


	返回处理后的字符串


-------------------------------------------------------------------------*/
function _trim(pString)
{
	if(pString==null || pString==""){
		return pString;
	}
	var tString = pString;
	for(var i=0;i<tString.length;i++)
	{
		if(tString.substr(i,1)!=" ")break;
	}
	tString = tString.substr(i);
	for(var i=tString.length-1;i>0;i--)
	{
		if(tString.substr(i,1)!=" ")break;
	}
	tString = tString.substr(0,i+1);
	return tString;
}

/*-------------------------------------------------------------------------
验证手机号码  格式：13408553494、013408553494

返回：

	正确返回true, 错误返回false

-------------------------------------------------------------------------*/

function vMobile(pString){
	if(pString.length!=11 && pString.length!=12){
		return false;
	}
	var myreg = /^([0]?1(?:3|4|5|8)[\d]{9})$/;
	if(!myreg.test(pString)){
		return false;
	}
	return true;
}

/*-------------------------------------------------------------------------
验证座机  格式：02887055599、87055599

返回：

	正确返回true, 错误返回false

-------------------------------------------------------------------------*/
function vTelePhone(pString){
	var p1 = /(^[0-9]{7,8}$)|(^[0-9]{3,4}[0-9]{7,8}$)/
    if (p1.test(pString)){
       return true;
    }else{
       return false;
    }
}

/*-------------------------------------------------------------------------
简单验证境外电话  限制7-20

返回：


	正确返回true, 错误返回false

-------------------------------------------------------------------------*/
function vForeignPhone(pString){
	var myreg = /^\d{7,20}$/;
	if(!myreg.test(pString)){
		return false;
	}
	return true;
}

String.prototype.replaceAll  = function(s1,s2){    
	return this.replace(new RegExp(s1,"gm"),s2);    
}
