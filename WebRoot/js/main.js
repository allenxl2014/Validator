/****************************
作用:初始化



*****************************/
//试用版开关


var tryVersionFlag = false;
var tryVersionAlert = "对不起，试用版不开放此功能！";

document.title = unescape("好房通ERP%A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0行业标准引领者 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0 %A0");

//屏蔽错误
//window.onerror = function(){return true;}
addEvent(window, "load", initial)
function initial()
{
	window.focus();
	document.onkeydown=function()
	{
	  try{
		if((event.altKey) 
		|| ((event.keyCode==8) && (event.srcElement.type!="text" && event.srcElement.type!="textarea" && event.srcElement.type!="password")) 
		|| ((event.ctrlKey) && ((event.keyCode==78) || (event.keyCode==82))) 
		|| (event.keyCode==116))   
	    {   
		    event.keyCode=0;   
		    event.returnValue=false;   
	    }
	  }catch(e){}
	}
//	document.oncontextmenu = function(){
//		var e = window.event || event;
//		if(e.srcElement.type != "text" && e.srcElement.type != "textarea")	return false;
//	};



	//这个部分是主菜单的显示



	if ($("menu"))
	{
		var childs=$("menu").getElementsByTagName("dl");
		for (var i=0; i<childs.length; i++)
		{
			var parent=childs[i].parentNode;
			parent.child=childs[i];
			parent.onmouseover=function() { this.child.style.display="block"; };
			parent.onmouseout =function() { this.child.style.display="none"; };
		}
	}

	if ($("myform"))
	{
		var tagNames=new Array("input","textarea","select")
		for (var i=0; i<tagNames.length; i++)
		{
			var inputArry = $("myform").getElementsByTagName(tagNames[i]);
			for (var j=0; j < inputArry.length; j++)
			{
				var input=inputArry[j];
				if (input.getAttribute("dataType") != null && input.getAttribute("require") == null) {input.style.backgroundColor="#fddcdc"}
				input.onblur = function() {this.style.borderColor= "#999"}
				input.onfocus= function() {this.style.borderColor= "#f30"}
				if (input.tabIndex==1) {input.focus();} //第一个输入控件获得焦点



			}
		}
	}
}


/****************************
作用:对象开关显示



使用:toggle(obj1,obj2)
*****************************/
function toggle()
{
    for (var i = 0; i < arguments.length; i++) 
	{
      var obj = $(arguments[i]);
      obj.style.display = (obj.style.display != "block" ? "block" : "none");
    }
}


function selectIt(obj,fybm)
{
	var aryTr = obj.parentNode.parentNode.getElementsByTagName("tr")
	for (var i=0;i<aryTr.length;i++)
	{
		aryTr[i].style.background="white"
	}
	obj.style.background = (obj.style.background == "#bce774" ? "white" : "#bce774");
	//$("keyIndex")=""
	//parent.$("fybmitem").value=fybm;
}


function openCalendar(id) //通用日期选择函数，请参考calendar.asp中的说明
{
	var x = parseInt(screen.width - 300) / 2;
	var y = parseInt(screen.height - 280)/ 2;
	window.open("/public/calendar.asp?return=" + id, "Calendar", "left="+ x +"px, top="+ y +"px, width=320px, height=280px, dialog=yes, modal=yes, resizable=no, scrollbars=no, alwaysRaised=yes");
}




/****************************
作用:根据对象id,获得指定对象
*****************************/
function $(objId)
{
	return document.getElementById(objId)
}


/****************************
作用:根据对象id,获得指定对象的值



*****************************/
function $F(objId)
{
	return document.getElementById(objId).value
}


/****************************
作用:根据对象id,获得指定对象的值,
将可能输入的html或xml标记过滤掉后,
并做encodeURIComponent
*****************************/
function $U(objId)
{
	var item=document.getElementById(objId).value;
	item=item.replace(/<\/?[^>]+>/gi, "");
	return encodeURIComponent(item);
}


/****************************
作用:事务函数
使用:直接调用addEvent(对象, 触发事件, 执行的函数)
*****************************/
function addEvent(obj, type, fn)
{
    if (obj.attachEvent)
        obj.attachEvent('on' + type, fn);
    else
        obj.addEventListener(type, fn, false);
}


/****************************
作用:设置对象的透明度



使用:setOpacity(对象,0.92)
*****************************/
function setOpacity(obj,opacity)
{
	obj.style.filter="alpha(opacity="+ opacity*100 +")"; /* IE */
	obj.style.opacity=opacity; /* 支持opacity的浏览器*/
	obj.style.KHTMLOpacity=opacity; /* 老版Safari */
	obj.style.MozOpacity=opacity; /* 老版Mozilla */
}


/****************************
作用:表单验证
*****************************/
 Validator = {
	Require : /.+/,
	Email : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
	Phone : /(^[0-9]{7,8}$)|(^[0-9]{3,4}[0-9]{7,8})$/,
	Mobile : /^([0]?1(?:3|4|5|8)[\d]{9})$/,
	Url : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
	IdCard : "this.IsIdCard(value)",
	Currency : /^\d+(\.\d+)?$/,
	Number : /^\d+$/,
	Zip : /^[1-9]\d{5}$/,
	QQ : /^[1-9]\d{4,8}$/,
	Integer : /^[-\+]?\d+$/,
	Double : /^[-\+]?\d+(\.\d+)?$/,
	English : /^[A-Za-z]+$/,
	EnglishAndNumber : /^[A-Za-z0-9]+$/,
	Chinese :  /^[\u0391-\uFFE5]+$/,
	Time: /^([0-1]?[0-9]|2[0-3]):([0-5][0-9])$/,
	Username : /^[a-z]\w{3,}$/i,
	UnSafe : /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/,
	IsSafe : function(str){return !this.UnSafe.test(str);},
	SafeString : "this.IsSafe(value)",
	Filter : "this.DoFilter(value, getAttribute('accept'))",
	Limit : "this.limit(value.length,getAttribute('min'),  getAttribute('max'))",
	LimitB : "this.limit(this.LenB(value), getAttribute('min'), getAttribute('max'))",
	Date : "this.IsDate(value, getAttribute('min'), getAttribute('format'))",
	Repeat : "value == document.getElementsByName(getAttribute('to'))[0].value",
	Range : "getAttribute('min') < (value|0) && (value|0) < getAttribute('max')",
	Compare : "this.compare(value,getAttribute('operator'),getAttribute('to'))",
	Custom : "this.Exec(value, getAttribute('regexp'))",
	Group : "this.MustChecked(getAttribute('name'), getAttribute('min'), getAttribute('max'))",
	ErrorItem : [document.forms[0]],
	ErrorMessage : ["以下原因导致提交失败：\t\t\t\t"],
	Validate : function(theForm, mode){
		var obj = theForm || event.srcElement;
		var count = obj.elements.length;
		this.ErrorMessage.length = 1;
		this.ErrorItem.length = 1;
		this.ErrorItem[0] = obj;
		for(var i=0;i<count;i++){
			with(obj.elements[i]){
				var _dataType = getAttribute("dataType");
				if(typeof(_dataType) == "object" || typeof(this[_dataType]) == "undefined")  continue;
				this.ClearState(obj.elements[i]);
				if(getAttribute("require") == "false" && value == "") continue;
				switch(_dataType){
					case "IdCard" :
					case "Date" :
					case "Repeat" :
					case "Range" :
					case "Compare" :
					case "Custom" :
					case "Group" : 
					case "Limit" :
					case "LimitB" :
					case "SafeString" :
					case "Filter" :
						if(!eval(this[_dataType]))	{
							this.AddError(i, getAttribute("msg"));
						}
						break;
					default :
						if(!this[_dataType].test(value)){
							this.AddError(i, getAttribute("msg"));
						}
						break;
				}
			}
		}
		if(this.ErrorMessage.length > 1){
			mode = mode || 1;
			var errCount = this.ErrorItem.length;
			switch(mode){
			case 2 :
				for(var i=1;i<errCount;i++)
					this.ErrorItem[i].style.backgroundColor = "#f5f6be"
			case 1 :
				alert(this.ErrorMessage.join("\n"));
				this.ErrorItem[1].focus();
				break;
			case 3 :
				for(var i=1;i<errCount;i++){
				try{
					var span = document.createElement("SPAN");
					span.id = "__ErrorMessagePanel";
					span.style.color = "red";
					this.ErrorItem[i].parentNode.appendChild(span);
					span.innerHTML = this.ErrorMessage[i].replace(/\d+:/,"*");
					}
					catch(e){alert(e.description);}
				}
				this.ErrorItem[1].focus();
				break;
			default :
				alert(this.ErrorMessage.join("\n"));
				break;
			}
			return false;
		}
		return true;
	},
	limit : function(len,min, max){
		min = min || 0;
		max = max || Number.MAX_VALUE;
		return min <= len && len <= max;
	},
	LenB : function(str){
		return str.replace(/[^\x00-\xff]/g,"**").length;
	},
	ClearState : function(elem){
		with(elem){
			if(style.color == "red")
				style.color = "";
			var lastNode = parentNode.childNodes[parentNode.childNodes.length-1];
			if(lastNode.id == "__ErrorMessagePanel")
				parentNode.removeChild(lastNode);
		}
	},
	AddError : function(index, str){
		this.ErrorItem[this.ErrorItem.length] = this.ErrorItem[0].elements[index];
		this.ErrorMessage[this.ErrorMessage.length] = this.ErrorMessage.length + ":" + str;
	},
	Exec : function(op, reg){
		return new RegExp(reg,"g").test(op);
	},
	compare : function(op1,operator,op2){
		switch (operator) {
			case "NotEqual":
				return (op1 != op2);
			case "GreaterThan":
				return (op1 > op2);
			case "GreaterThanEqual":
				return (op1 >= op2);
			case "LessThan":
				return (op1 < op2);
			case "LessThanEqual":
				return (op1 <= op2);
			default:
				return (op1 == op2);            
		}
	},
	MustChecked : function(name, min, max){
		var groups = document.getElementsByName(name);
		var hasChecked = 0;
		min = min || 1;
		max = max || groups.length;
		for(var i=groups.length-1;i>=0;i--)
			if(groups[i].checked) hasChecked++;
		return min <= hasChecked && hasChecked <= max;
	},
	DoFilter : function(input, filter){
return new RegExp("^.+\.(?=EXT)(EXT)$".replace(/EXT/g, filter.split(/\s*,\s*/).join("|")), "gi").test(input);
	},
	IsIdCard : function(number){
		var date, Ai;
		var verify = "10x98765432";
		var Wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
		var area = ['','','','','','','','','','','','北京','天津','河北','山西','内蒙古','','','','','','辽宁','吉林','黑龙江','','','','','','','','上海','江苏','浙江','安微','福建','江西','山东','','','','河南','湖北','湖南','广东','广西','海南','','','','重庆','四川','贵州','云南','西藏','','','','','','','陕西','甘肃','青海','宁夏','新疆','','','','','','台湾','','','','','','','','','','香港','澳门','','','','','','','','','国外'];
		var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/i);
		if(re == null) return false;
		if(re[1] >= area.length || area[re[1]] == "") return false;
		if(re[2].length == 12){
			Ai = number.substr(0, 17);
			date = [re[9], re[10], re[11]].join("-");
		}
		else{
			Ai = number.substr(0, 6) + "19" + number.substr(6);
			date = ["19" + re[4], re[5], re[6]].join("-");
		}
		if(!this.IsDate(date, "ymd")) return false;
		var sum = 0;
		for(var i = 0;i<=16;i++){
			sum += Ai.charAt(i) * Wi[i];
		}
		Ai +=  verify.charAt(sum%11);
		return (number.length ==15 || number.length == 18 && number == Ai);
		/**
		if(number.length ==18){
			var myreg = /^(\d{17}x)|(\d{17}X)|(\d{18})$/;
			if(!myreg.test(number)){
				return false;
			}
		}else if(number.length == 15){
			var myreg = /^\d{15}$/;
			if(!myreg.test(number)){
				return false;
			}
		}else{
			return false;
		}
		return true;
		**/
	},
	IsDate : function(op, formatString){
		formatString = formatString || "ymd";
		var m, year, month, day;
		switch(formatString){
			case "ymd" :
				m = op.match(new RegExp("^((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})$"));
				if(m == null ) return false;
				day = m[6];
				month = m[5]*1;
				year =  (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
				break;
			case "dmy" :
				m = op.match(new RegExp("^(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))$"));
				if(m == null ) return false;
				day = m[1];
				month = m[3]*1;
				year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
				break;
			default :
				break;
		}
		if(!parseInt(month)) return false;
		month = month==0 ?12:month;
		var date = new Date(year, month-1, day);
        return (typeof(date) == "object" && year == date.getFullYear() && month == (date.getMonth()+1) && day == date.getDate());
		function GetFullYear(y){return ((y<30 ? "20" : "19") + y)|0;}
	}
 }



/****************************
作用:密码强度检测



用法: onkeyup="checkContext(this)"
*****************************/
var PasswordStrength =
{
	Level : ["极佳","一般","较弱","太短"],//强度提示信息
	LevelValue : [15, 10, 5, 0],//和上面提示信息对应的强度值



	Factor : [1, 1, 5],//强度加权数,分别为字母，数字，其它



	KindFactor : [0,0,10,20],//密码含几种组成的权数
	Regex : [/[a-zA-Z]/g, /\d/g, /[^a-zA-Z0-9]/g] //字符，数字，非字符数字（即特殊符号）
}

PasswordStrength.StrengthValue = function(pwd)
{
	var strengthValue = 0;
	var ComposedKind = 0;
	for(var i = 0 ; i < this.Regex.length;i++)
	{
		var chars = pwd.match(this.Regex[i]);//匹配当前密码中符合指定正则的字符，如果有多个字符以','分隔
		if(chars != null)
		{
			strengthValue += chars.length * this.Factor[i];
			ComposedKind ++;
		}
	}
	strengthValue += this.KindFactor[ComposedKind];
	return strengthValue;
}

PasswordStrength.StrengthLevel = function(pwd)
{
	var value = this.StrengthValue(pwd);
	for(var i = 0 ; i < this.LevelValue.length ; i ++)
	{
		if(value >= this.LevelValue[i] ) {return this.Level[i];}
	}
}

function checkContext(obj)
{
	var showmsg=PasswordStrength.StrengthLevel(obj.value);
	$("context").innerHTML = showmsg;
	$("context").style.paddingLeft="20px";
	switch(showmsg)
	{
		case "太短" : $("context").style.background="url(/style/level/error.gif) no-repeat 0 4px";
		break;
		case "较弱" : $("context").style.background="url(/style/level/error.gif) no-repeat 0 4px";
		break;
		case "极佳" : $("context").style.background="url(/style/level/ok.gif) no-repeat 0 4px";
		break;
		default : $("context").style.background="url(/style/level/info.gif) no-repeat 0 4px";
	}
}


/****************************
作用:获得一个对象的绝对位置
使用:
*****************************/
function getPosition(obj)
{
    var left = 0;
    var top  = 0;
    while (obj.offsetParent)
    {
        left += obj.offsetLeft ;
        top  += obj.offsetTop  ;
        obj   = obj.offsetParent;
    }
    left += obj.offsetLeft;
    top  += obj.offsetTop ;

    return {x:left, y:top};
}


/****************************
作用:显示弹出窗口
使用:onclick="dialogue('您确认要退出吗？','logout()')"
第2个参数是确认后，执行的函数



*****************************/
function dialogue(text,func)
{
	var objElement=document.documentElement
	var objMask=document.createElement("div"); //遮罩
	objMask.style.cssText="position:absolute;z-index:1000;left:0;top:0;background:#ccc;width:100%;height:"+ objElement.scrollHeight +"px";
	setOpacity(objMask,0.7)

	var objBox=document.createElement("div")
	objBox.style.cssText="position:absolute;z-index:1001;margin:-120px 0 0 -160px;left:50%;top:50%;width:320px;height:120px;border:1px solid #c10;background:white";
	window.onscroll=function (){objBox.style.top=(objElement.scrollTop + objElement.clientHeight/2)+"px"}

	var objBody=document.body
	with (objBody)
	{
		appendChild(objMask);
		appendChild(objBox);
		onselectstart = function(){return false}; //禁止对页面的任何操作
		oncontextmenu = function(){return false};
	}

	var objTitle=document.createElement("h4");
	objTitle.style.cssText="text-align:left;margin:1px;padding:3px;color:white;background:#c10"
	objTitle.innerHTML="提 示";

	var objTxt=document.createElement("p");
	objTxt.style.cssText="margin:16px 0;font-weight:bold"
	objTxt.innerHTML=text;

	var objYes=document.createElement("button");
	var objNo=document.createElement("button");
	objYes.className="submit";
	objNo.className="return";
	objYes.innerHTML="确 定";
	objNo.innerHTML="取 消";
	with (objBox)
	{
		appendChild(objTitle);
		appendChild(objTxt);
		appendChild(objYes);
		appendChild(objNo);
	}

	objYes.onclick=function()
	{
		eval(func);
		objBody.removeChild(objMask);
		objBody.removeChild(objBox);
		onselectstart = function(){return true};
		oncontextmenu = function(){return true};
	}
	objNo.onclick=function()
	{
		objBody.removeChild(objMask);
		objBody.removeChild(objBox);
		onselectstart = function(){return true};
		oncontextmenu = function(){return true};
	}
}


/*  Prototype JavaScript framework, version 1.3.1
 *  (c) 2005 Sam Stephenson <sam@conio.net>
 *  THIS FILE IS AUTOMATICALLY GENERATED. When sending patches, please diff
 *  against the source tree, available from the Prototype darcs repository. 
 *  Prototype is freely distributable under the terms of an MIT-style license.
 *  For details, see the Prototype web site: http://prototype.conio.net/
/*--------------------------------------------------------------------------*/
var Prototype = {
  Version: '1.3.1',
  emptyFunction: function() {}
}

var Class = {
  create: function() {
    return function() { 
      this.initialize.apply(this, arguments);
    }
  }
}

var Abstract = new Object();

Object.extend = function(destination, source) {
  for (property in source) {
    destination[property] = source[property];
  }
  return destination;
}

Object.prototype.extend = function(object) {
  return Object.extend.apply(this, [this, object]);
}

Function.prototype.bind = function(object) {
  var __method = this;
  return function() {
    __method.apply(object, arguments);
  }
}

Function.prototype.bindAsEventListener = function(object) {
  var __method = this;
  return function(event) {
    __method.call(object, event || window.event);
  }
}

Number.prototype.toColorPart = function() {
  var digits = this.toString(16);
  if (this < 16) return '0' + digits;
  return digits;
}

var Try = {
  these: function() {
    var returnValue;

    for (var i = 0; i < arguments.length; i++) {
      var lambda = arguments[i];
      try {
        returnValue = lambda();
        break;
      } catch (e) {}
    }

    return returnValue;
  }
}

/*--------------------------------------------------------------------------*/

var PeriodicalExecuter = Class.create();
PeriodicalExecuter.prototype = {
  initialize: function(callback, frequency) {
    this.callback = callback;
    this.frequency = frequency;
    this.currentlyExecuting = false;

    this.registerCallback();
  },

  registerCallback: function() {
    setInterval(this.onTimerEvent.bind(this), this.frequency * 1000);
  },

  onTimerEvent: function() {
    if (!this.currentlyExecuting) {
      try { 
        this.currentlyExecuting = true;
        this.callback(); 
      } finally { 
        this.currentlyExecuting = false;
      }
    }
  }
}

/*--------------------------------------------------------------------------*/

function $() {
  var elements = new Array();

  for (var i = 0; i < arguments.length; i++) {
    var element = arguments[i];
    if (typeof element == 'string')
      element = document.getElementById(element);

    if (arguments.length == 1) 
      return element;

    elements.push(element);
  }

  return elements;
}

if (!Array.prototype.push) {
  Array.prototype.push = function() {
		var startLength = this.length;
		for (var i = 0; i < arguments.length; i++)
      this[startLength + i] = arguments[i];
	  return this.length;
  }
}

if (!Function.prototype.apply) {
  // Based on code from http://www.youngpup.net/
  Function.prototype.apply = function(object, parameters) {
    var parameterStrings = new Array();
    if (!object)     object = window;
    if (!parameters) parameters = new Array();
    
    for (var i = 0; i < parameters.length; i++)
      parameterStrings[i] = 'parameters[' + i + ']';
    
    object.__apply__ = this;
    var result = eval('object.__apply__(' + 
      parameterStrings.join(', ') + ')');
    object.__apply__ = null;
    
    return result;
  }
}

String.prototype.extend({
  stripTags: function() {
    return this.replace(/<\/?[^>]+>/gi, '');
  },

  escapeHTML: function() {
    var div = document.createElement('div');
    var text = document.createTextNode(this);
    div.appendChild(text);
    return div.innerHTML;
  },

  unescapeHTML: function() {
    var div = document.createElement('div');
    div.innerHTML = this.stripTags();
    return div.childNodes[0].nodeValue;
  }
});

var Ajax = {
  getTransport: function() {
    return Try.these(
      function() {return new ActiveXObject('Msxml2.XMLHTTP')},
      function() {return new ActiveXObject('Microsoft.XMLHTTP')},
      function() {return new XMLHttpRequest()}
    ) || false;
  }
}

Ajax.Base = function() {};
Ajax.Base.prototype = {
  setOptions: function(options) {
    this.options = {
      method:       'post',
      asynchronous: true,
      parameters:   ''
    }.extend(options || {});
  },

  responseIsSuccess: function() {
    return this.transport.status == 'undefined'
        || this.transport.status == 0 
        || (this.transport.status >= 200 && this.transport.status < 300);
  },

  responseIsFailure: function() {
    return !this.responseIsSuccess();
  }
}

Ajax.Request = Class.create();
Ajax.Request.Events = 
  ['Uninitialized', 'Loading', 'Loaded', 'Interactive', 'Complete'];

Ajax.Request.prototype = (new Ajax.Base()).extend({
  initialize: function(url, options) {
    this.transport = Ajax.getTransport();
    this.setOptions(options);
    this.request(url);
  },

  request: function(url) {
    var parameters = this.options.parameters || '';
    if (parameters.length > 0) parameters += '&_=';

    try {
      if (this.options.method == 'get')
        url += '?' + parameters;

      this.transport.open(this.options.method, url,
        this.options.asynchronous);

      if (this.options.asynchronous) {
        this.transport.onreadystatechange = this.onStateChange.bind(this);
        setTimeout((function() {this.respondToReadyState(1)}).bind(this), 10);
      }

      this.setRequestHeaders();

      var body = this.options.postBody ? this.options.postBody : parameters;
      this.transport.send(this.options.method == 'post' ? body : null);

    } catch (e) {
    }
  },

  setRequestHeaders: function() {
    var requestHeaders = 
      ['X-Requested-With', 'XMLHttpRequest',
       'X-Prototype-Version', Prototype.Version];

    if (this.options.method == 'post') {
      requestHeaders.push('Content-type', 
        'application/x-www-form-urlencoded');

      /* Force "Connection: close" for Mozilla browsers to work around
       * a bug where XMLHttpReqeuest sends an incorrect Content-length
       * header. See Mozilla Bugzilla #246651. 
       */
      if (this.transport.overrideMimeType)
        requestHeaders.push('Connection', 'close');
    }

    if (this.options.requestHeaders)
      requestHeaders.push.apply(requestHeaders, this.options.requestHeaders);

    for (var i = 0; i < requestHeaders.length; i += 2)
      this.transport.setRequestHeader(requestHeaders[i], requestHeaders[i+1]);
  },

  onStateChange: function() {
    var readyState = this.transport.readyState;
    if (readyState != 1)
      this.respondToReadyState(this.transport.readyState);
  },

  respondToReadyState: function(readyState) {
    var event = Ajax.Request.Events[readyState];

    if (event == 'Complete')
      (this.options['on' + this.transport.status]
       || this.options['on' + (this.responseIsSuccess() ? 'Success' : 'Failure')]
       || Prototype.emptyFunction)(this.transport);

    (this.options['on' + event] || Prototype.emptyFunction)(this.transport);

    /* Avoid memory leak in MSIE: clean up the oncomplete event handler */
    if (event == 'Complete')
      this.transport.onreadystatechange = Prototype.emptyFunction;
  }
});

Ajax.Updater = Class.create();
Ajax.Updater.ScriptFragment = '(?:<script.*?>)((\n|.)*?)(?:<\/script>)';

Ajax.Updater.prototype.extend(Ajax.Request.prototype).extend({
  initialize: function(container, url, options) {
    this.containers = {
      success: container.success ? $(container.success) : $(container),
      failure: container.failure ? $(container.failure) :
        (container.success ? null : $(container))
    }

    this.transport = Ajax.getTransport();
    this.setOptions(options);

    var onComplete = this.options.onComplete || Prototype.emptyFunction;

    this.options.onComplete = (function() {
      this.updateContent();
      onComplete(this.transport);
    }).bind(this);

    this.request(url);
  },

  updateContent: function() {
    var receiver = this.responseIsSuccess() ?
      this.containers.success : this.containers.failure;

    var match    = new RegExp(Ajax.Updater.ScriptFragment, 'img');
    var response = this.transport.responseText.replace(match, '');
    var scripts  = this.transport.responseText.match(match);

    if (receiver) {
      if (this.options.insertion) {
        new this.options.insertion(receiver, response);
      } else {
        receiver.innerHTML = response;
      }
    }

    if (this.responseIsSuccess()) {
      if (this.onComplete)
        setTimeout((function() {this.onComplete(
          this.transport)}).bind(this), 10);
    }

    if (this.options.evalScripts && scripts) {
      match = new RegExp(Ajax.Updater.ScriptFragment, 'im');
      setTimeout((function() {
        for (var i = 0; i < scripts.length; i++)
          eval(scripts[i].match(match)[1]);
      }).bind(this), 10);
    }
  }
});

Ajax.PeriodicalUpdater = Class.create();
Ajax.PeriodicalUpdater.prototype = (new Ajax.Base()).extend({
  initialize: function(container, url, options) {
    this.setOptions(options);
    this.onComplete = this.options.onComplete;

    this.frequency = (this.options.frequency || 2);
    this.decay = 1;

    this.updater = {};
    this.container = container;
    this.url = url;

    this.start();
  },

  start: function() {
    this.options.onComplete = this.updateComplete.bind(this);
    this.onTimerEvent();
  },

  stop: function() {
    this.updater.onComplete = 'undefined';
    clearTimeout(this.timer);
    (this.onComplete || Ajax.emptyFunction).apply(this, arguments);
  },

  updateComplete: function(request) {
    if (this.options.decay) {
      this.decay = (request.responseText == this.lastText ? 
        this.decay * this.options.decay : 1);

      this.lastText = request.responseText;
    }
    this.timer = setTimeout(this.onTimerEvent.bind(this), 
      this.decay * this.frequency * 1000);
  },

  onTimerEvent: function() {
    this.updater = new Ajax.Updater(this.container, this.url, this.options);
  }
});

document.getElementsByClassName = function(className) {
  var children =document.all || document.getElementsByTagName('*');
  var elements = new Array();

  for (var i = 0; i < children.length; i++) {
    var child = children[i];
    var classNames = child.className.split(' ');
    for (var j = 0; j < classNames.length; j++) {
      if (classNames[j] == className) {
        elements.push(child);
        break;
      }
    }
  }
  return elements;
}

/*--------------------------------------------------------------------------*/

if (!window.Element) {
  var Element = new Object();
}

Object.extend(Element, {
  toggle: function() {
    for (var i = 0; i < arguments.length; i++) {
      var element = $(arguments[i]);
      element.style.display = 
        (element.style.display != 'block' ? 'block' : 'none');
    }
  },

  hide: function() {
    for (var i = 0; i < arguments.length; i++) {
      var element = $(arguments[i]);
      element.style.display = 'none';
    }
  },

  show: function() {
    for (var i = 0; i < arguments.length; i++) {
      var element = $(arguments[i]);
      element.style.display = 'block';
    }
  },

  disable: function() {
    for (var i = 0; i < arguments.length; i++) {
      var element = $(arguments[i]);
      element.disabled = 'true';
	}
  },

  enable: function() {
    for (var i = 0; i < arguments.length; i++) {
     var element = $(arguments[i]);
      element.disabled = '';
    }
  },
  
  remove: function(element) {
    element = $(element);
    element.parentNode.removeChild(element);
  },
   
  getHeight: function(element) {
    element = $(element);
    return element.offsetHeight; 
  },

  hasClassName: function(element, className) {
    element = $(element);
    if (!element)
      return;
    var a = element.className.split(' ');
    for (var i = 0; i < a.length; i++) {
      if (a[i] == className)
        return true;
    }
    return false;
  },

  addClassName: function(element, className) {
    element = $(element);
    Element.removeClassName(element, className);
    element.className += ' ' + className;
  },

  removeClassName: function(element, className) {
    element = $(element);
    if (!element)
      return;
    var newClassName = '';
    var a = element.className.split(' ');
    for (var i = 0; i < a.length; i++) {
      if (a[i] != className) {
        if (i > 0)
          newClassName += ' ';
        newClassName += a[i];
      }
    }
    element.className = newClassName;
  },
  
  // removes whitespace-only text node children
  cleanWhitespace: function(element) {
    var element = $(element);
    for (var i = 0; i < element.childNodes.length; i++) {
      var node = element.childNodes[i];
      if (node.nodeType == 3 && !/\S/.test(node.nodeValue)) 
        Element.remove(node);
    }
  }
});

var Toggle = new Object();
Toggle.display = Element.toggle;

/*--------------------------------------------------------------------------*/

Abstract.Insertion = function(adjacency) {
  this.adjacency = adjacency;
}

Abstract.Insertion.prototype = {
  initialize: function(element, content) {
    this.element = $(element);
    this.content = content;
    
    if (this.adjacency && this.element.insertAdjacentHTML) {
      this.element.insertAdjacentHTML(this.adjacency, this.content);
    } else {
      this.range = this.element.ownerDocument.createRange();
      if (this.initializeRange) this.initializeRange();
      this.fragment = this.range.createContextualFragment(this.content);
      this.insertContent();
    }
  }
}

var Insertion = new Object();

Insertion.Before = Class.create();
Insertion.Before.prototype = (new Abstract.Insertion('beforeBegin')).extend({
  initializeRange: function() {
    this.range.setStartBefore(this.element);
  },
  
  insertContent: function() {
    this.element.parentNode.insertBefore(this.fragment, this.element);
  }
});

Insertion.Top = Class.create();
Insertion.Top.prototype = (new Abstract.Insertion('afterBegin')).extend({
  initializeRange: function() {
    this.range.selectNodeContents(this.element);
    this.range.collapse(true);
  },
  
  insertContent: function() {  
    this.element.insertBefore(this.fragment, this.element.firstChild);
  }
});

Insertion.Bottom = Class.create();
Insertion.Bottom.prototype = (new Abstract.Insertion('beforeEnd')).extend({
  initializeRange: function() {
    this.range.selectNodeContents(this.element);
    this.range.collapse(this.element);
  },
  
  insertContent: function() {
    this.element.appendChild(this.fragment);
  }
});

Insertion.After = Class.create();
Insertion.After.prototype = (new Abstract.Insertion('afterEnd')).extend({
  initializeRange: function() {
    this.range.setStartAfter(this.element);
  },
  
  insertContent: function() {
    this.element.parentNode.insertBefore(this.fragment, 
      this.element.nextSibling);
  }
});

var Field = {
  clear: function() {
    for (var i = 0; i < arguments.length; i++)
      $(arguments[i]).value = '';
  },

  focus: function(element) {
    $(element).focus();
  },
  
  present: function() {
    for (var i = 0; i < arguments.length; i++)
      if ($(arguments[i]).value == '') return false;
    return true;
  },
  
  select: function(element) {
    $(element).select();
  },
   
  activate: function(element) {
    $(element).focus();
    $(element).select();
  }
}

/*--------------------------------------------------------------------------*/

var Form = {
  serialize: function(form) {
    var elements = Form.getElements($(form));
    var queryComponents = new Array();
    
    for (var i = 0; i < elements.length; i++) {
      var queryComponent = Form.Element.serialize(elements[i]);
      if (queryComponent)
        queryComponents.push(queryComponent);
    }
    
    return queryComponents.join('&');
  },
  
  getElements: function(form) {
    var form = $(form);
    var elements = new Array();

    for (tagName in Form.Element.Serializers) {
      var tagElements = form.getElementsByTagName(tagName);
      for (var j = 0; j < tagElements.length; j++)
        elements.push(tagElements[j]);
    }
    return elements;
  },
  
  getInputs: function(form, typeName, name) {
    var form = $(form);
    var inputs = form.getElementsByTagName('input');
    
    if (!typeName && !name)
      return inputs;
      
    var matchingInputs = new Array();
    for (var i = 0; i < inputs.length; i++) {
      var input = inputs[i];
      if ((typeName && input.type != typeName) ||
          (name && input.name != name)) 
        continue;
      matchingInputs.push(input);
    }

    return matchingInputs;
  },

  disable: function(form) {
    var elements = Form.getElements(form);
    for (var i = 0; i < elements.length; i++) {
      var element = elements[i];
      element.blur();
      element.disabled = 'true';
    }
  },

  enable: function(form) {
    var elements = Form.getElements(form);
    for (var i = 0; i < elements.length; i++) {
      var element = elements[i];
      element.disabled = '';
    }
  },

  focusFirstElement: function(form) {
    var form = $(form);
    var elements = Form.getElements(form);
    for (var i = 0; i < elements.length; i++) {
      var element = elements[i];
      if (element.type != 'hidden' && !element.disabled) {
        Field.activate(element);
        break;
      }
    }
  },

  reset: function(form) {
    $(form).reset();
  }
}

Form.Element = {
  serialize: function(element) {
    var element = $(element);
    var method = element.tagName.toLowerCase();
    var parameter = Form.Element.Serializers[method](element);
    
    if (parameter)
      return encodeURIComponent(parameter[0]) + '=' + 
        encodeURIComponent(parameter[1]);                   
  },
  
  getValue: function(element) {
    var element = $(element);
    var method = element.tagName.toLowerCase();
    var parameter = Form.Element.Serializers[method](element);
    
    if (parameter) 
      return parameter[1];
  }
}

Form.Element.Serializers = {
  input: function(element) {
    switch (element.type.toLowerCase()) {
      case 'submit':
      case 'hidden':
      case 'password':
      case 'text':
        return Form.Element.Serializers.textarea(element);
      case 'checkbox':  
      case 'radio':
        return Form.Element.Serializers.inputSelector(element);
    }
    return false;
  },

  inputSelector: function(element) {
    if (element.checked)
      return [element.name, element.value];
  },

  textarea: function(element) {
    return [element.name, element.value];
  },

  select: function(element) {
    var value = '';
    if (element.type == 'select-one') {
      var index = element.selectedIndex;
      if (index >= 0)
        value = element.options[index].value || element.options[index].text;
    } else {
      value = new Array();
      for (var i = 0; i < element.length; i++) {
        var opt = element.options[i];
        if (opt.selected)
          value.push(opt.value || opt.text);
      }
    }
    return [element.name, value];
  }
}

/*--------------------------------------------------------------------------*/

var $F = Form.Element.getValue;

/*--------------------------------------------------------------------------*/

Abstract.TimedObserver = function() {}
Abstract.TimedObserver.prototype = {
  initialize: function(element, frequency, callback) {
    this.frequency = frequency;
    this.element   = $(element);
    this.callback  = callback;
    
    this.lastValue = this.getValue();
    this.registerCallback();
  },
  
  registerCallback: function() {
    setInterval(this.onTimerEvent.bind(this), this.frequency * 1000);
  },
  
  onTimerEvent: function() {
    var value = this.getValue();
    if (this.lastValue != value) {
      this.callback(this.element, value);
      this.lastValue = value;
    }
  }
}

Form.Element.Observer = Class.create();
Form.Element.Observer.prototype = (new Abstract.TimedObserver()).extend({
  getValue: function() {
    return Form.Element.getValue(this.element);
  }
});

Form.Observer = Class.create();
Form.Observer.prototype = (new Abstract.TimedObserver()).extend({
  getValue: function() {
    return Form.serialize(this.element);
  }
});

/*--------------------------------------------------------------------------*/

Abstract.EventObserver = function() {}
Abstract.EventObserver.prototype = {
  initialize: function(element, callback) {
    this.element  = $(element);
    this.callback = callback;
    
    this.lastValue = this.getValue();
    if (this.element.tagName.toLowerCase() == 'form')
      this.registerFormCallbacks();
    else
      this.registerCallback(this.element);
  },
  
  onElementEvent: function() {
    var value = this.getValue();
    if (this.lastValue != value) {
      this.callback(this.element, value);
      this.lastValue = value;
    }
  },
  
  registerFormCallbacks: function() {
    var elements = Form.getElements(this.element);
    for (var i = 0; i < elements.length; i++)
      this.registerCallback(elements[i]);
  },
  
  registerCallback: function(element) {
    if (element.type) {
      switch (element.type.toLowerCase()) {
        case 'checkbox':  
        case 'radio':
          element.target = this;
          element.prev_onclick = element.onclick || Prototype.emptyFunction;
          element.onclick = function() {
            this.prev_onclick(); 
            this.target.onElementEvent();
          }
          break;
        case 'password':
        case 'text':
        case 'textarea':
        case 'select-one':
        case 'select-multiple':
          element.target = this;
          element.prev_onchange = element.onchange || Prototype.emptyFunction;
          element.onchange = function() {
            this.prev_onchange(); 
            this.target.onElementEvent();
          }
          break;
      }
    }    
  }
}

Form.Element.EventObserver = Class.create();
Form.Element.EventObserver.prototype = (new Abstract.EventObserver()).extend({
  getValue: function() {
    return Form.Element.getValue(this.element);
  }
});

Form.EventObserver = Class.create();
Form.EventObserver.prototype = (new Abstract.EventObserver()).extend({
  getValue: function() {
    return Form.serialize(this.element);
  }
});


if (!window.Event) {
  var Event = new Object();
}

Object.extend(Event, {
  KEY_BACKSPACE: 8,
  KEY_TAB:       9,
  KEY_RETURN:   13,
  KEY_ESC:      27,
  KEY_LEFT:     37,
  KEY_UP:       38,
  KEY_RIGHT:    39,
  KEY_DOWN:     40,
  KEY_DELETE:   46,

  element: function(event) {
    return event.target || event.srcElement;
  },

  isLeftClick: function(event) {
    return (((event.which) && (event.which == 1)) ||
            ((event.button) && (event.button == 1)));
  },

  pointerX: function(event) {
    return event.pageX || (event.clientX + 
      (document.documentElement.scrollLeft || document.body.scrollLeft));
  },

  pointerY: function(event) {
    return event.pageY || (event.clientY + 
      (document.documentElement.scrollTop || document.body.scrollTop));
  },

  stop: function(event) {
    if (event.preventDefault) { 
      event.preventDefault(); 
      event.stopPropagation(); 
    } else {
      event.returnValue = false;
    }
  },

  // find the first node with the given tagName, starting from the
  // node the event was triggered on; traverses the DOM upwards
  findElement: function(event, tagName) {
    var element = Event.element(event);
    while (element.parentNode && (!element.tagName ||
        (element.tagName.toUpperCase() != tagName.toUpperCase())))
      element = element.parentNode;
    return element;
  },

  observers: false,
  
  _observeAndCache: function(element, name, observer, useCapture) {
    if (!this.observers) this.observers = [];
    if (element.addEventListener) {
      this.observers.push([element, name, observer, useCapture]);
      element.addEventListener(name, observer, useCapture);
    } else if (element.attachEvent) {
      this.observers.push([element, name, observer, useCapture]);
      element.attachEvent('on' + name, observer);
    }
  },
  
  unloadCache: function() {
    if (!Event.observers) return;
    for (var i = 0; i < Event.observers.length; i++) {
      Event.stopObserving.apply(this, Event.observers[i]);
      Event.observers[i][0] = null;
    }
    Event.observers = false;
  },

  observe: function(element, name, observer, useCapture) {
    var element = $(element);
    useCapture = useCapture || false;
    
    if (name == 'keypress' &&
        ((navigator.appVersion.indexOf('AppleWebKit') > 0) 
        || element.attachEvent))
      name = 'keydown';
    
    this._observeAndCache(element, name, observer, useCapture);
  },

  stopObserving: function(element, name, observer, useCapture) {
    var element = $(element);
    useCapture = useCapture || false;
    
    if (name == 'keypress' &&
        ((navigator.appVersion.indexOf('AppleWebKit') > 0) 
        || element.detachEvent))
      name = 'keydown';
    
    if (element.removeEventListener) {
      element.removeEventListener(name, observer, useCapture);
    } else if (element.detachEvent) {
      element.detachEvent('on' + name, observer);
    }
  }
});

/* prevent memory leaks in IE */
//Event.observe(window, 'unload', Event.unloadCache, false);

var Position = {

  // set to true if needed, warning: firefox performance problems
  // NOT neeeded for page scrolling, only if draggable contained in
  // scrollable elements
  includeScrollOffsets: false, 

  // must be called before calling withinIncludingScrolloffset, every time the
  // page is scrolled
  prepare: function() {
    this.deltaX =  window.pageXOffset 
                || document.documentElement.scrollLeft 
                || document.body.scrollLeft 
                || 0;
    this.deltaY =  window.pageYOffset 
                || document.documentElement.scrollTop 
                || document.body.scrollTop 
                || 0;
  },

  realOffset: function(element) {
    var valueT = 0, valueL = 0;
    do {
      valueT += element.scrollTop  || 0;
      valueL += element.scrollLeft || 0; 
      element = element.parentNode;
    } while (element);
    return [valueL, valueT];
  },

  cumulativeOffset: function(element) {
    var valueT = 0, valueL = 0;
    do {
      valueT += element.offsetTop  || 0;
      valueL += element.offsetLeft || 0;
      element = element.offsetParent;
    } while (element);
    return [valueL, valueT];
  },

  // caches x/y coordinate pair to use with overlap
  within: function(element, x, y) {
    if (this.includeScrollOffsets)
      return this.withinIncludingScrolloffsets(element, x, y);
    this.xcomp = x;
    this.ycomp = y;
    this.offset = this.cumulativeOffset(element);

    return (y >= this.offset[1] &&
            y <  this.offset[1] + element.offsetHeight &&
            x >= this.offset[0] && 
            x <  this.offset[0] + element.offsetWidth);
  },

  withinIncludingScrolloffsets: function(element, x, y) {
    var offsetcache = this.realOffset(element);

    this.xcomp = x + offsetcache[0] - this.deltaX;
    this.ycomp = y + offsetcache[1] - this.deltaY;
    this.offset = this.cumulativeOffset(element);

    return (this.ycomp >= this.offset[1] &&
            this.ycomp <  this.offset[1] + element.offsetHeight &&
            this.xcomp >= this.offset[0] && 
            this.xcomp <  this.offset[0] + element.offsetWidth);
  },

  // within must be called directly before
  overlap: function(mode, element) {  
    if (!mode) return 0;  
    if (mode == 'vertical') 
      return ((this.offset[1] + element.offsetHeight) - this.ycomp) / 
        element.offsetHeight;
    if (mode == 'horizontal')
      return ((this.offset[0] + element.offsetWidth) - this.xcomp) / 
        element.offsetWidth;
  },

  clone: function(source, target) {
    source = $(source);
    target = $(target);
    target.style.position = 'absolute';
    var offsets = this.cumulativeOffset(source);
    target.style.top    = offsets[1] + 'px';
    target.style.left   = offsets[0] + 'px';
    target.style.width  = source.offsetWidth + 'px';
    target.style.height = source.offsetHeight + 'px';
  }
}
/*
var mainPicPath = (function() {
    var elements = document.getElementsByTagName('script');
    for (var i = 0, len = elements.length; i < len; i++) {
        if (elements[i].src && elements[i].src.match(/main[\w\-\.]*\.js/)) {
            var path = elements[i].src.substring(0, elements[i].src.lastIndexOf('/') + 1);
            return path.replace(/\/js\//g,"/images/");
        }
    }
    return "";
})();
*/

var mainPicPath = "/hftCRMWeb/images/";

window.alert1 = function(txt) {
	var frameObj = top.document.getElementById("mainFrame").contentWindow.document;
	var bottomFrameObj = top.document.getElementById("bottomFrame").contentWindow.document;
	var bottomShield =  bottomFrameObj.getElementById("bottomShield");
	if(bottomShield==null){
		bottomShield = bottomFrameObj.createElement("DIV"); 
		bottomShield.id = "bottomShield"; 
		bottomShield.style.cssText = "position:absolute;left:0px;top:0px;width:"+(window.screen.width-10)+"px;height:"+(window.screen.height-170)+"px;z-index:1000002;filter:alpha(opacity=1);background-color:#fff";
		bottomFrameObj.body.appendChild(bottomShield); 
	}
	var shield = frameObj.getElementById("shield");
	if(shield==null){
		shield = frameObj.createElement("DIV"); 
		shield.id = "shield"; 
		shield.style.cssText = "position:absolute;left:0px;top:0px;width:"+(window.screen.width-10)+"px;height:"+(window.screen.height-170)+"px;z-index:1000002;filter:alpha(opacity=1);background-color:#fff;display:none;";
		frameObj.body.appendChild(shield); 
	}else{
		shield.style.cssText = "position:absolute;left:0px;top:0px;width:"+(window.screen.width-10)+"px;height:"+(window.screen.height-170)+"px;z-index:1000002;filter:alpha(opacity=1);background-color:#fff;display:none;";
	}
	var alertFram = frameObj.getElementById("alertFram");
	if(alertFram==null){
		alertFram = frameObj.createElement("DIV"); 
		alertFram.id="alertFram"; 
		alertFram.style.cssText = "position:absolute;left:"+(window.screen.width-350)/2+"px;top:"+(window.screen.height-500)/2+"px;width:350px;height:100px;text-align:center;z-index:1000003;margin-left:0px;margin-top:0px;";
		frameObj.body.appendChild(alertFram); 
	}
	txt = "<br/><br/>"+String(txt);
	txt = txt.replace(/\n/gi,"<br/>");
	strHtml = "<img onclick=\"closeAlert();\" id=\"alertCloseBtn\" onmouseover=\"javascript:this.src='"+mainPicPath+"dialog_closebtn_over.gif'\" onmouseout=\"javascript:this.src='"+mainPicPath+"dialog_closebtn.gif'\" style=\"position:absolute;top:8px;left:300px;cursor: pointer;z-index:20001\" src='"+mainPicPath+"dialog_closebtn.gif'>"+
	"<div style=\"position:absolute;top:12px;left:20px;cursor: default;width:160px;height:20px;z-index:20001;color:#076D9E;font-size: 12px;\">好房通ERP - 行业标准引领者</div>"+
	"<div style=\"position:absolute;top:13px;left:21px;cursor: default;width:160px;height:20px;z-index:10000;color:#ffffff;font-size: 12px;\">好房通ERP - 行业标准引领者</div>"+
	"<table width=\"350px\" cellpadding=\"0\" cellspacing=\"0\">"+
	"<tr>"+
		"<td style=\"width:13px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_lt.png', sizingMethod='crop')\"></td>"+
		"<td onmousedown=\"moveEventAlert(this,'');return false;\" style=\"cursor: move; width:300px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_ct.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:13px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_rt.png', sizingMethod='crop');\"></td>"+
	"</tr>"+
	"<tr>"+
		"<td style=\"width:13px;height:100px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_mlb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"background-color:#EFF7FF;word-spacing:0px;width:300px;min-height:100px;vertical-align: middle;text-align: center;font-size: 12px;padding: 5px;\" id=\"msgTxt\">"+
		txt+
		"</br>"+
		"<div style=\"text-align: center;margin-left:12px;margin-top:10px;padding-top:20px;\">"+
		"<input onclick=\"closeAlert();\" id=\"alertYesBtn\" onmouseover=\"this.style.backgroundImage='url("+mainPicPath+"menu_jg1.gif)'\" onmouseout=\"this.style.backgroundImage='url("+mainPicPath+"menu_ys1.gif)'\" style=\"background-image: url('"+mainPicPath+"menu_ys1.gif');border: 0;width: 40px;height: 21px;font-size:12px;cursor: pointer;\" type=\"button\" value=\"确定\" />"+
		 "</div><br/>"+
		"</td>"+
		"<td style=\"width:13px;height:100px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_mrt.png', sizingMethod='crop')\"></td>"+
	"</tr>"+
	"<tr>"+
		"<td style=\"width:13px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_lb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:300px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_cb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:13px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_rb.png', sizingMethod='crop')\"></td>"+
	"</tr>"+
	"</table>";
	alertFram.innerHTML = strHtml; 
	alertFram.style.display = "block";
	shield.style.display = "block"; 
	bottomShield.style.display = "block";
	frameObj.getElementById("alertYesBtn").focus();
	frameObj.body.onselectstart = function(){return false;}; 
} 


function nopay(txt) {
	var frameObj = top.document.getElementById("mainFrame").contentWindow.document;
	var bottomFrameObj = top.document.getElementById("bottomFrame").contentWindow.document;
	var bottomShield =  bottomFrameObj.getElementById("bottomShield_nopay");
	if(bottomShield==null){
		bottomShield = bottomFrameObj.createElement("DIV"); 
		bottomShield.id = "bottomShield_nopay"; 
		bottomShield.style.cssText = "position:absolute;left:0px;top:0px;width:"+(window.screen.width-10)+"px;height:"+(window.screen.height-170)+"px;z-index:1000002;filter:alpha(opacity=1);background-color:#fff";
		bottomFrameObj.body.appendChild(bottomShield); 
	}
	var shield = frameObj.getElementById("shield_nopay");
	if(shield==null){
		shield = frameObj.createElement("DIV"); 
		shield.id = "shield_nopay"; 
		shield.style.cssText = "position:absolute;left:0px;top:0px;width:"+(window.screen.width-10)+"px;height:"+(window.screen.height-170)+"px;z-index:1000002;filter:alpha(opacity=1);background-color:#fff;display:none;";
		frameObj.body.appendChild(shield); 
	}else{
		shield.style.cssText = "position:absolute;left:0px;top:0px;width:"+(window.screen.width-10)+"px;height:"+(window.screen.height-170)+"px;z-index:1000002;filter:alpha(opacity=1);background-color:#fff;display:none;";
	}
	var alertFram = frameObj.getElementById("alertFram_nopay");
	if(alertFram==null){
		alertFram = frameObj.createElement("DIV"); 
		alertFram.id="alertFram_nopay"; 
		alertFram.style.cssText = "position:absolute;left:"+(window.screen.width-350)/2+"px;top:"+(window.screen.height-500)/2+"px;width:350px;height:100px;text-align:center;z-index:1000003;margin-left:0px;margin-top:0px;";
		frameObj.body.appendChild(alertFram); 
	}
	txt = "<br/><br/>"+String(txt);
	txt = txt.replace(/\n/gi,"<br/>");
	strHtml = "<img onclick=\"\" id=\"alertCloseBtn\" onmouseover=\"javascript:this.src='"+mainPicPath+"dialog_closebtn_over.gif'\" onmouseout=\"javascript:this.src='"+mainPicPath+"dialog_closebtn.gif'\" style=\"position:absolute;top:8px;left:300px;cursor: pointer;z-index:20001\" src='"+mainPicPath+"dialog_closebtn.gif'>"+
	"<div style=\"position:absolute;top:12px;left:20px;cursor: default;width:160px;height:20px;z-index:20001;color:#076D9E;font-size: 12px;\">好房通ERP - 行业标准引领者</div>"+
	"<div style=\"position:absolute;top:13px;left:21px;cursor: default;width:160px;height:20px;z-index:10000;color:#ffffff;font-size: 12px;\">好房通ERP - 行业标准引领者</div>"+
	"<table width=\"350px\" cellpadding=\"0\" cellspacing=\"0\">"+
	"<tr>"+
		"<td style=\"width:13px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_lt.png', sizingMethod='crop')\"></td>"+
		"<td onmousedown=\"moveEventAlert(this,'');return false;\" style=\"cursor: move; width:300px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_ct.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:13px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_rt.png', sizingMethod='crop');\"></td>"+
	"</tr>"+
	"<tr>"+
		"<td style=\"width:13px;height:100px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_mlb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"background-color:#EFF7FF;word-spacing:0px;width:300px;min-height:100px;vertical-align: middle;text-align: center;font-size: 12px;padding: 5px;\" id=\"msgTxt\">"+
		txt+
		"</br>"+
		"<div style=\"text-align: center;margin-left:12px;margin-top:10px;padding-top:20px;\">"+
		"<input onclick=\"\" id=\"alertYesBtn\" onmouseover=\"this.style.backgroundImage='url("+mainPicPath+"menu_jg1.gif)'\" onmouseout=\"this.style.backgroundImage='url("+mainPicPath+"menu_ys1.gif)'\" style=\"background-image: url('"+mainPicPath+"menu_ys1.gif');border: 0;width: 40px;height: 21px;font-size:12px;cursor: pointer;\" type=\"button\" value=\"确定\" />"+
		 "</div><br/>"+
		"</td>"+
		"<td style=\"width:13px;height:100px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_mrt.png', sizingMethod='crop')\"></td>"+
	"</tr>"+
	"<tr>"+
		"<td style=\"width:13px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_lb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:300px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_cb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:13px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_rb.png', sizingMethod='crop')\"></td>"+
	"</tr>"+
	"</table>";
	alertFram.innerHTML = strHtml; 
	alertFram.style.display = "block";
	shield.style.display = "block"; 
	bottomShield.style.display = "block";
	alertFram.focus(); 
	frameObj.body.onselectstart = function(){return false;}; 
} 

function closeAlert(){
	var frameObj = top.document.getElementById("mainFrame").contentWindow.document;
	var bottomFrameObj = top.document.getElementById("bottomFrame").contentWindow.document;
	frameObj.body.removeChild(frameObj.getElementById("alertFram"));
	frameObj.body.removeChild(frameObj.getElementById("shield"));
	bottomFrameObj.body.removeChild(bottomFrameObj.getElementById("bottomShield"));
}


window.confirm = function(txt,func,func1){
	var frameObj1 = top.document.getElementById("mainFrame").contentWindow.document;
	var bottomFrameObj1 = top.document.getElementById("bottomFrame").contentWindow.document;
	var bottomShield1 =  bottomFrameObj1.getElementById("bottomShield1");
	if(bottomShield1==null){
		bottomShield1 = bottomFrameObj1.createElement("DIV"); 
		bottomShield1.id = "bottomShield1"; 
		bottomShield1.style.cssText = "position:absolute;left:0px;top:0px;width:"+(window.screen.width-10)+"px;height:"+(window.screen.height-170)+"px;z-index:1000000;filter:alpha(opacity=1);background-color:#fff";
		bottomFrameObj1.body.appendChild(bottomShield1); 
	}
	
	var shield1 = frameObj1.getElementById("shield1");
	if(shield1==null){
		shield1 = frameObj1.createElement("DIV"); 
		shield1.id = "shield1"; 
		shield1.style.cssText = "position:absolute;left:0px;top:0px;width:"+(window.screen.width-10)+"px;height:"+(window.screen.height-170)+"px;z-index:1000000;filter:alpha(opacity=1);background-color:#fff";
		frameObj1.body.appendChild(shield1); 
	}
	var alertFram1 = frameObj1.getElementById("alertFram1");
	if(alertFram1==null){
		alertFram1 = frameObj1.createElement("DIV"); 
		alertFram1.id="alertFram1"; 
		alertFram1.style.cssText = "position:absolute;left:"+(window.screen.width-350)/2+"px;top:"+(window.screen.height-500)/2+"px;width:350px;height:120px;text-align:center;z-index:1000001;margin-left:0px;margin-top:0px;";
		frameObj1.body.appendChild(alertFram1); 
	}
	txt = "<br/>"+String(txt);
	txt = txt.replace(/\n/gi,"<br/>");
	strHtml = "<img id=\"alertCloseBtn1\" onmouseover=\"javascript:this.src='"+mainPicPath+"dialog_closebtn_over.gif'\" onmouseout=\"javascript:this.src='"+mainPicPath+"dialog_closebtn.gif'\" style=\"position:absolute;top:8px;left:300px;cursor: pointer;z-index:20001\" src='"+mainPicPath+"dialog_closebtn.gif'>"+
	"<div style=\"position:absolute;top:12px;left:20px;cursor: default;width:160px;height:20px;z-index:20001;color:#076D9E;font-size: 12px;\">好房通ERP - 行业标准引领者</div>"+
	"<div style=\"position:absolute;top:13px;left:21px;cursor: default;width:160px;height:20px;z-index:10000;color:#ffffff;font-size: 12px;\">好房通ERP - 行业标准引领者</div>"+
	"<table width=\"350px\" cellpadding=\"0\" cellspacing=\"0\">"+
	"<tr>"+
		"<td style=\"width:13px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_lt.png', sizingMethod='crop')\"></td>"+
		"<td onmousedown=\"moveEventAlert(this,'1');return false;\" style=\"cursor: move; width:300px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_ct.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:13px;height:33px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_rt.png', sizingMethod='crop');\"></td>"+
	"</tr>"+
	"<tr>"+
		"<td style=\"width:13px;height:120px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_mlb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"background-color:#EFF7FF;word-spacing:0px;width:300px;min-height:120px;vertical-align: middle;text-align: center;font-size: 12px;padding: 5px;\" id=\"msgTxt\">"+
		txt+
		"</br>"+
		"<div style=\"text-align: center;margin-left:12px;margin-top:10px;padding-top:20px;\">"+
		"<input id=\"alertSureBtn1\" onmouseover=\"this.style.backgroundImage='url("+mainPicPath+"menu_jg1.gif)'\" onmouseout=\"this.style.backgroundImage='url("+mainPicPath+"menu_ys1.gif)'\" style=\"background-image: url('"+mainPicPath+"menu_ys1.gif');border: 0;width: 40px;height: 21px;font-size:12px;cursor: pointer;\" type=\"button\" value=\"确定\" onclick=\"sureAlert1();\" />"+
		 "<input id=\"alertYesBtn1\" onmouseover=\"this.style.backgroundImage='url("+mainPicPath+"menu_jg1.gif)'\" onmouseout=\"this.style.backgroundImage='url("+mainPicPath+"menu_ys1.gif)'\" style=\"background-image: url('"+mainPicPath+"menu_ys1.gif');border: 0;width: 40px;height: 21px;font-size:12px;cursor: pointer;margin-left:20px;\" type=\"button\" value=\"取消\" onclick=\"closeAlert1();\" />"+
		 "</div>"+
		"</td>"+
		"<td style=\"width:13px;height:120px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_mrt.png', sizingMethod='crop')\"></td>"+
	"</tr>"+
	"<tr>"+
		"<td style=\"width:13px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_lb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:300px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_cb.png', sizingMethod='crop')\"></td>"+
		"<td style=\"width:13px;height:13px;FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+mainPicPath+"dialog_rb.png', sizingMethod='crop')\"></td>"+
	"</tr>"+
	"</table>";
	
	alertFram1.innerHTML = strHtml; 
	alertFram1.style.display = "block";
	shield1.style.display = "block"; 
	bottomShield1.style.display = "block";
	
	var closeBtn1 = frameObj1.getElementById("alertCloseBtn1");
	var yesBtn1 = frameObj1.getElementById("alertYesBtn1");
	var sureBtn1 = frameObj1.getElementById("alertSureBtn1");
	yesBtn1.onclick = closeAlert1;
	closeBtn1.onclick = closeAlert1;
	sureBtn1.onclick = sureAlert1;
	function closeAlert1(){
		frameObj1.body.removeChild(frameObj1.getElementById("alertFram1"));
		frameObj1.body.removeChild(frameObj1.getElementById("shield1"));
		bottomFrameObj1.body.removeChild(bottomFrameObj1.getElementById("bottomShield1"));
		if(typeof(func1)!="undefined"){
			func1();
		}
	} 
	
	function sureAlert1(){
		frameObj1.body.removeChild(frameObj1.getElementById("alertFram1"));
		frameObj1.body.removeChild(frameObj1.getElementById("shield1"));
		bottomFrameObj1.body.removeChild(bottomFrameObj1.getElementById("bottomShield1"));
		func();
	}
	frameObj1.getElementById("alertYesBtn1").focus();
	frameObj1.body.onselectstart = function(){return false;};
}
var flagAlert = false;

function moveEventAlert(obj,type,e){
	flagAlert = true;
	var doc = top.document.getElementById("mainFrame").contentWindow.document;
	var sc = doc.getElementById("alertFram"+type);
	sc.onmousedown=function(e){
		e = e || window.event;
		var x= e.layerX || e.offsetX;
	    var y= e.layerY || e.offsetY;
	    if(sc.setCapture){
	         sc.setCapture();
	     }else if(window.captureEvents){
	         window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
	     }
	  
	     doc.onmousemove=function(e){
	     	if(!flagAlert){return;}
	           e= e || window.event;
	           if(!e.pageX)e.pageX=e.clientX;
	           if(!e.pageY)e.pageY=e.clientY;
	           var tx=e.pageX-x-15;
	           var ty=e.pageY-y;     
	           sc.style.left=tx;
	           sc.style.top=ty;
	      };
	      
		    doc.onmouseup=function(){
	           //取消捕获范围
	           if(sc.releaseCapture){
	              sc.releaseCapture();
	           }else if(window.captureEvents){
	              window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
	           }
	           
	          //清除事件
	          doc.onmousemove=null;
	          doc.onmouseup=null;
	          flagAlert = false;
	      };
	}
	
}

function closeBottom(){
	var frameObj = top.document.getElementById("mainFrame").contentWindow.document;
	var bottomFrameObj = top.document.getElementById("bottomFrame").contentWindow.document;
	try{
		bottomFrameObj.body.removeChild(bottomFrameObj.getElementById("bottomShield"));
		frameObj.body.removeChild(frameObj.getElementById("alertFram"));
		frameObj.body.removeChild(frameObj.getElementById("shield"));
	}catch(e){}
	try{
		bottomFrameObj.body.removeChild(bottomFrameObj.getElementById("bottomShield1"));
		frameObj.body.removeChild(frameObj.getElementById("alertFram1"));
		frameObj.body.removeChild(frameObj.getElementById("shield1"));
	}catch(e){}
}
