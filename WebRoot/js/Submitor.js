	/*----------------------------------------------------------
	Submitor.js
	检查是否有重复提交的功能

	create by wenjian
	
	modify by abnerchai
	调用方法：

			var sub = new Submitor();
			sub.alreadySubmited();//检查是否己经提交过了

			sub.clearRepeatSubmitFlag();//清除己经提交的标志

			sub.submit('base');/提交document中名字为base的form
	------------------------------------------------------------*/
	
	var __repeatSubmitDegree = 0;
	function Submitor(form) {
	
		/*-------------------------------------------------------
		方法名：submit(pform)
		方法功能：

				提交一个给定名称的表单				
		---------------------------------------------------------*/
		this.submit = function submit(pform){
			if(alreadySubmited()){
				return false;	
			}
			if(pform!=null){
				//eval("document."+pFormName+".submit();");
				pform.submit()
			}else{
				form.submit();
			}
		}
		
		/*-------------------------------------------------------
		方法名：location(page)
		方法功能：

				重定位页面URL				
		---------------------------------------------------------*/
		this.location = function location(page){
			if(alreadySubmited()){
				return false;	
			}
			form.location = page;
		}
		
		
		/*--------------------------------------------------------
		方法名：alreadySubmited()
		方法功能：

				检查表单是否己经提交

		返回值：
						如果己经提交，返回true，并提示，请不要重复提交。

						如果没有提交，返回false;	
		----------------------------------------------------------*/
		
		this.alreadySubmited = function alreadySubmited(){
			var rs_flag = false;
			if(__repeatSubmitDegree>0){
				rs_flag = true;
				alert("请不要重复发出相同请求!");
			}else{
				__repeatSubmitDegree++;
			}
			return rs_flag;
		}
		
		/*----------------------------------------------------------
		方法名：clearRepeatSubmitFlag()
		方法功能：

				清除己重复提交的标志
		
		------------------------------------------------------------*/
		this.clearRepeatSubmitFlag = function clearRepeatSubmitFlag(){
			__repeatSubmitDegree = 0;
		}
	}