package com.liang.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.fto.jthink.util.StringHelper;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;


/**
 * <p>Title: 列表选择器</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Selector {
	
	String name;
	Element selectorEL;
	String firstOption;
	String textName;
	String valueName;
	String selected;
	String otherAttrs;
	String[] otherOptionAttrNames;
	
	Hashtable linkedSelectorHT = new Hashtable();
	Hashtable spaceHTMLHT = new Hashtable();

	public static final String ALL = "所有";	
	public static final String NULL = "";
	public static final String NOTHING = null;
	
	/**
	 * 选择器构造方法
	 * @param name 选择器名称
	 * @param selectorEL 构建选择器必须的数据值
	 * @param firstOption 选择器第一项 有三种类型={Selector.ALL，Selector.NULL, Selector.NOTHING}={"所有"，空项，什么也没有}
	 * @param textName 选择器TEXT内容在selectorEL中所属性名称
	 * @param valueName 选择器VALUE值在selectorEL中所属性名称
	 * @param selected 默认的被选择中项的值
	 * @param otherAttrs 其它选择器属性，比如CSS样式，事件等
	 * 
	 */
  public Selector(String name, Element selectorEL, String firstOption, String textName, String valueName, String selected, String otherAttrs) {
  	this.name = name;
  	this.selectorEL = selectorEL;
  	this.firstOption = firstOption;
  	this.textName = textName;
  	this.valueName = valueName;
  	this.selected = selected;
  	this.otherAttrs = otherAttrs;
  }

  public Selector(String name, Element selectorEL, String firstOption, String textName, String valueName, String selected, String otherAttrs, String[] otherOptionAttrNames) {
  	this.name = name;
  	this.selectorEL = selectorEL;
  	this.firstOption = firstOption;
  	this.textName = textName;
  	this.valueName = valueName;
  	this.selected = selected;
  	this.otherAttrs = otherAttrs;
  	this.otherOptionAttrNames = otherOptionAttrNames;
  }

	/**
	 * 生成选择器对象串内容，在页面上打印输出即可
	 */
	public String toString(){
		String optionStr = " ";
		if(firstOption!=NOTHING){
			optionStr += "<option value=\"\" >"+ firstOption +"</option>";
		}
		List optionLT = selectorEL.getChildren();
		for(int i=0; i<optionLT.size(); i++){
			Element optionEL = (Element)optionLT.get(i);
			String option_id = XMLHelper.getAttributeString(optionEL, valueName);
			String option_name = XMLHelper.getAttributeString(optionEL, textName);
			optionStr += "<option "+ getSelectedStatusString(option_id, selected) +"  value=\"" + option_id + "\" "+getOtherOptionAttrNames(optionEL)+">"+ option_name +"</option>";
		}
		//optionStr += "</select>";
		
		return   build_Script()
					+ build_Header() 
					+ optionStr
					+ build_Footer();

	}

	/**
	 * 返回其它属性
	 */
	private String getOtherOptionAttrNames(Element optionEL){
		if(otherOptionAttrNames==null || otherOptionAttrNames.length==0){
			return "";
		}
		String ooans = "";
		for(int i=0;i<otherOptionAttrNames.length;i++){
			ooans += " "+otherOptionAttrNames[i]+"=\""+optionEL.getAttributeValue(otherOptionAttrNames[i])+"\"";
		}
		return ooans;
	}

	/**
	 * 加入一个被级联的选择器对象
	 * @param value 关联值，主选择器的Value值
	 * @param linkedSelector 被关联的告诉对象
	 */
	public void addLinkedSelector(String value, Object linkedSelector){
		linkedSelectorHT.put(value, linkedSelector);
	}
	/**
	 * 加入一个被级联的选择器对象
	 * @param value 关联值，主选择器的Value值
	 * @param linkedSelector 被关联的告诉对象
	 * @param spaceHTMLStr 间隔HTML，用于分隔级联的选择器
	 */
	public void addLinkedSelector(String value, Object linkedSelector, String spaceHTMLStr){
		linkedSelectorHT.put(value, linkedSelector); 
		spaceHTMLHT.put(value, spaceHTMLStr);
	}

	private String getSpaceHTML(String value){
		Object spaceHTML = spaceHTMLHT.get(value);
		return spaceHTML==null?"":spaceHTML.toString();
	}

	private String getScript$getLinkedSelectorBy(){
		
		return  " function "+name+"_getLinkedSelectorBy(key){"
			+ "  for(var i=0; i<"+name+"_linkedSelectors.length;i++){"
			+ "  	if("+name+"_linkedSelectors[i][0] == key){"
			+ "  		return "+name+"_linkedSelectors[i][1];"
			+ "  	}"
			+ "  }"
			+ "  return \"\";"
			+ " }";
		
	}
	
	private String getDefaultLinkedSelector(){
		if(linkedSelectorHT.size()>0){
			if(selected!=null){
				Object selector = linkedSelectorHT.get(selected);
				if(selector!=null){
					return "<span id=Linked_"+name+"_div >"+getSpaceHTML(selected)+selector.toString()+"</span>";
				}
			}
			if(selectorEL.getChildren().iterator().hasNext() && firstOption==NOTHING){
				String value = ((Element)selectorEL.getChildren().iterator().next()).getAttributeValue(valueName);
				
				if(value!=null){
					Object selector = linkedSelectorHT.get(value);
					if(selector!=null){
						return "<span id=Linked_"+name+"_div >"+getSpaceHTML(value)+selector.toString()+"</span>";
					}
				}
			}
			return "<span id=Linked_"+name+"_div ></span>";
		}
		return "";
	}
	
	private String getScript$onChange(){
		if(linkedSelectorHT.size()>0){
			return  " function "+name+"_setLinkedSelector(selectObj){"
						+ " document.getElementById(\"Linked_"+name+"_div\").innerHTML="+name+"_getLinkedSelectorBy(selectObj.value);"
					+ " }";
		}
		return "";
	}

	

   protected String build_Script(){
			if(linkedSelectorHT.size()>0){
			   	Enumeration keyEM = linkedSelectorHT.keys();
			   	String linkedSelectorsStr = "var "+name+"_linkedSelectors = new Array();";
			   	while(keyEM.hasMoreElements()){
			   		String key = (String)keyEM.nextElement();
						linkedSelectorsStr += " "+name+"_linkedSelectors["+name+"_linkedSelectors.length] = new Array();";
						linkedSelectorsStr += " "+name+"_linkedSelectors["+name+"_linkedSelectors.length-1][0] = \""+key+"\";";
						String linkedSelecor = StringHelper.replace(linkedSelectorHT.get(key).toString(),"\"","\\\"");
						linkedSelectorsStr += " "+name+"_linkedSelectors["+name+"_linkedSelectors.length-1][1] = \""+getSpaceHTML(key)+linkedSelecor+"\";";
			   	}
					return "<script language=\"JavaScript\">"
								+ linkedSelectorsStr + getScript$getLinkedSelectorBy()+getScript$onChange()
						+ "</script>";
			}
			return "";
   }


   private String build_Header(){
   	if(linkedSelectorHT.size()>0){
      return "<select name=\""+name+"\" "+otherAttrs + " onChange=\""+name+"_setLinkedSelector(this) \"  >";
   	}
   	return "<select name=\""+name+"\" "+otherAttrs + " >";
   }
   private String build_Footer(){
      return "</select>&nbsp;"+getDefaultLinkedSelector();
   }

	
	//get select object selected status
	private String getSelectedStatusString(String value1, String value2){
		String selectStr = "";
		if(value1!=null && value2!=null && value1.equals(value2)){
			selectStr = " selected ";
		}
		return selectStr;
	}

}