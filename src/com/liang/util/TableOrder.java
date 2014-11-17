package com.liang.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.xerces.utils.StringHasher;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.j2ee.web.util.HTMLHelper;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.StringHelper;



/**
 * 用于JSP页面上的表格排序。

 *
 * <p>Copyright (c) 2002-2004 美森软件系统有限公司,
 * http://www.mission-apps.com</p>
 * @author wjian@free-think.org
 * @version 1.00
 */
public class TableOrder {

  public final static String ASC = "ASC";
  public final static String DESC = "DESC";
  public final static String FLAG_ASC =
      "title=\"升序\"";
  public final static String FLAG_DESC =
      "title=\"降序\"";
  public final static String ORDER_MODE = "ORDER_MODE";

  private String url = "";
  public HashMap fieldsHT = new HashMap();
  private List orders = new ArrayList(2);
  public String defaultMode = DESC;

//  private String upImg = "";
//  private String downImg = "";

	/* 资源管理器 */
	private ResourceManager resManager;
	/* JThink中定义的Http请求 */
	public HttpRequest request;
	/* 事务管理器 */
	private TransactionManager transactionManager;
	
	private Operator operator;	


  public TableOrder(){}

  public TableOrder(HttpServletRequest req) throws Exception {
		resManager = new JBeanInitialization().initialize(req);
		request = (HttpRequest)resManager.getResource(HttpRequest.class.getName());
		operator =(Operator)resManager.getResource(HttpSession.class.getName(), Operator.class.getName());
//    this.upImg = req.getContextPath()+"/images/add99.gif";
//    this.downImg = req.getContextPath()+"/images/ope99.gif";
    addField("__TABLE_ORDER", "");
    
    request.getParameterNames();
  }


  /**
   * 加入连接URL
   */
  public void addURL(String url) {
    this.url = url;
  }

  /**
   * 加入查询串字段(单值)
   */
  public void addField(String key, String value) {
    fieldsHT.put(key, value);
  }

  /**
   * 加入查询串字段(多值)
   */
  public void addField(HashMap ht) {
    Iterator it = ht.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      String value = (String) ht.get(key);
      fieldsHT.put(key, value);
    }
  }

  /**
   * 加入查询串字段(多值)
   */
  public void addField(String[] keys, HashMap ht) {
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];
      String value = (String) ht.get(key);
      fieldsHT.put(key, value);
    }
  }

  /**
   * 加入被排序字段

   * @param field
   * @param mode - Is ASC or DESC
   */
  public void addOrderField(String field, String mode) {
    orders.add(field + " " + mode);
  }

  /**
   * 加入被排序字段

   */
  public void addOrderField(String field) {
    String mode = "";
    if (hasCurrOrderField(field)) {
      mode = getString(ORDER_MODE);
      if (mode.equals(DESC)) {
        mode = ASC;
      }
      else {
        mode = DESC;
      }
    }
    else {
      mode = defaultMode;
    }
    addOrderField(field, mode);
  }

  /**
   * 清除所有排序字段

   */
  public void clearOrderFields() {
    orders.clear();
  }

  /**
   * 设置默认排序
   */
  public void setDefaultOrderField(String fieldName, String mode) {
    if (mode != null) {
      defaultMode = mode;
      if (request.getParameter(ValidatorDataStructure.ORDERBY) == null) {
        request.putParameter(ValidatorDataStructure.ORDERBY, fieldName + " " + mode);
      }
      if (request.getParameter(ORDER_MODE) == null) {
        request.putParameter(ORDER_MODE, defaultMode);
      }

    }
  }

  /**
   * 返回排序串

   */
  public String getOrderString() {
    String str = "";
    if (orders.size() == 0) {
      return str;
    }
    Iterator it = orders.iterator();
    while (it.hasNext()) {
      str += "," + (String) it.next();
    }
    return str.substring(1);
  }

  /**
   * 返回格式化的URL串，包括连接URL，查询串字段，排序字段等
   */
  public String getFormatedURL() {
    return getFormatedURL(fieldsHT);
  }

  /**
   * 返回格式化的URL串，包括连接URL，查询串字段，排序字段等
   */
  public String getFormatedURL(String[] exceptingFields) {
    HashMap fds_HT = (HashMap) fieldsHT.clone();
    for (int i = 0; i < exceptingFields.length; i++) {
      fds_HT.remove(exceptingFields[i]);
    }
    return getFormatedURL(fds_HT);
  }

  private String getFormatedURL(HashMap fdsHT) {
    String formatedURL = "";
    String fieldStr = "";
    if (url.indexOf("?") >= 0) {
      fieldStr = "&";
    }
    else {
      fieldStr = "?";
    }
    if (orders.size() > 0) {
      fdsHT.put(ValidatorDataStructure.ORDERBY, getOrderString());
    }

    Iterator it = fdsHT.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      String value = (String) fdsHT.get(key);
      fieldStr += key + "=" + encodeString(value) + "&";
    }

    //if (orders.size() == 0) {
    if(fieldStr.length()>0){
      fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
    }
    return url + fieldStr;
  }


  /**
   * 返回格式化的URL串，包括连接URL，查询串字段，排序字段等
   */
  public String getFormatedURL(String selfUrl, String[] exceptingFields) {
    HashMap fds_HT = (HashMap) fieldsHT.clone();
    for (int i = 0; i < exceptingFields.length; i++) {
      fds_HT.remove(exceptingFields[i]);
    }
    return getFormatedURL(selfUrl, fds_HT);
  }

  private String getFormatedURL(String selfUrl, HashMap fdsHT) {
    String formatedURL = "";
    String fieldStr = "";
    if (url.indexOf("?") >= 0) {
      fieldStr = "&";
    }
    else {
      fieldStr = "?";
    }
    if (orders.size() > 0) {
      fdsHT.put(ValidatorDataStructure.ORDERBY, getOrderString());
    }

    Iterator it = fdsHT.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      String value = (String) fdsHT.get(key);
      fieldStr += key + "=" + encodeString(value) + "&";
    }

    //if (orders.size() == 0) {
    if(fieldStr.length()>0){
      fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
    }
    return selfUrl + url + fieldStr;
  }


  /**
   * 对字符进行URI编码，汉字,数字，字母除外。

   * 此方法主要用于对连接上的参数值进行编码

   * 比如：01234ABab汉字%&=' < 编码后为 %30%31%32%33%34汉字%25%26%3d%27%3c%41%42%43
   * @param str
   * @return
   */
  private String encodeString(String value) {
      try {
		      return com.liang.util.URLEncoder.encode(value, "utf-8");
    	} catch (UnsupportedEncodingException e) {
          throw new JThinkRuntimeException(e);
    	}   
  }

//  public String encodeString(String str){
//    StringBuffer sb = new StringBuffer(str);
//    StringBuffer sbs = new StringBuffer();
//    for(int i=0;i<sb.length();i++){
//      int c = (int)sb.charAt(i);
//      if(c>255 || c>='0' && c<='9' || c>='a' && c<='z' || c>='A' && c<='Z'){
//        sbs.append((char)c);
//      }else{
//        sbs.append("%" + Integer.toHexString(c));
//      }
//    }
//    return sbs.toString();
//  }


  /**
   * 返回事件URL
   */
  public String getEventURL(String displayStr) {
    return getEventURL(displayStr, "_self", "link_black");
  }

  /**
   * 返回事件URL
   */
  public String getEventURL(String displayStr, String target, String css) {
    String flagStr = "";
    if (isCurrOrderField(getOrderString())) {
      String mode = getString(ORDER_MODE);
      if (mode.equals(DESC)) {
        addField(ORDER_MODE, ASC);
        flagStr = FLAG_DESC;//StringHelper.replace(FLAG_DESC, "#DOWN_IMG#", downImg);
      }
      else {
        addField(ORDER_MODE, DESC);
        flagStr = FLAG_ASC;//StringHelper.replace(FLAG_ASC, "#UP_IMG#", upImg);
      }
      flagStr += " style=\"color: #FFFF00;\"";
    }
    else {
      addField(ORDER_MODE, defaultMode);
    }
    fieldsHT.remove("PAGE_OFFSET");
    return "<A href=\"" + getFormatedURL() +
        "\" target=\""+target+"\" class=\""+css+"\" "+flagStr+">" + displayStr + "</A>";
  }


  public boolean isCurrOrderField(String orderStr) {
    String oldOrderField = getString(ValidatorDataStructure.ORDERBY);
    return isCurrOrderField(oldOrderField, orderStr);
  }

  private boolean isCurrOrderField(String oldOrderField, String orderStr) {
    oldOrderField = StringHelper.replace(oldOrderField, DESC, " ");
    oldOrderField = StringHelper.replace(oldOrderField, ASC, " ");
    orderStr = StringHelper.replace(orderStr, DESC, " ");
    orderStr = StringHelper.replace(orderStr, ASC, " ");
    
    if(oldOrderField.trim().equalsIgnoreCase(orderStr.trim())){
      return true;
    }
    String[] oldOrderFields = StringHelper.split(oldOrderField, " ");
    String[] orderStrs = StringHelper.split(orderStr, " ");
    
    if(oldOrderFields.length>0 && orderStrs.length>0){
      return oldOrderFields[0].trim().equalsIgnoreCase(orderStrs[0].trim());
    }
    return false;

  }

  private boolean hasCurrOrderField(String orderStr) {
    String oldOrderField = getString(ValidatorDataStructure.ORDERBY);
    return hasCurrOrderField(oldOrderField, orderStr);
  }
  
  public boolean hasCurrOrderField(String oldOrderField, String orderStr){
   oldOrderField = StringHelper.replace(oldOrderField, DESC, "");
    oldOrderField = StringHelper.replace(oldOrderField, ASC, "");
    orderStr = StringHelper.replace(orderStr, DESC, "");
    orderStr = StringHelper.replace(orderStr, ASC, "");
    if(oldOrderField.equalsIgnoreCase(orderStr)){
      return true;
    }
    orderStr = orderStr.trim();
    String[] oldOrderStrs = StringHelper.split(oldOrderField, ",");
    for(int i=0;i<oldOrderStrs.length;i++){
      if(oldOrderStrs[i].trim().equalsIgnoreCase(orderStr)){
        return true;
      }
    }
    return false;
  }

	/**
	 * 构建表格头

	 */
	public String buildTableTitle(String titleValue, String titleText){
   		addOrderField(titleValue);
   		String url = getEventURL(titleText);
   		clearOrderFields();
   		return url;
	}

	public String buildTableTitle(String[] titleValues, String titleText){
		for(int i=0;i<titleValues.length;i++){
   		addOrderField(titleValues[i]);
		}
		String url = getEventURL(titleText);
		clearOrderFields();
		return url;
	}


	public String getString(String key){
		String value = request.getParameter(key);
		return value==null?"":value;
	}





  /**
   * 返回格式化的URL串，包括连接URL，查询串字段，排序字段等
   */
  public String getFormatedTitleURL(String[] thisField) {
    return getFormatedTitleURL(fieldsHT, thisField);
  }


  private String getFormatedTitleURL(HashMap fdsHT, String[] thisField) {

    String formatedURL = "";
    String fieldStr = "";
    if (url.indexOf("?") >= 0) {
      fieldStr = "&";
    }
    else {
      fieldStr = "?";
    }
    if (orders.size() > 0) {
      fdsHT.put(ValidatorDataStructure.ORDERBY, getOrderString());
    }
    Iterator it = fdsHT.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();

      int i=0;
			boolean hasFild = false;
      for(; i<thisField.length; i++){
	      if(key.equals(thisField[i])){
	      	hasFild = true;
	      	break;
	      }
      }
      if(!hasFild){
	      String value = (String) fdsHT.get(key);
	      fieldStr += key + "=" + encodeString(value) + "&";
      }

    }

    //if (orders.size() == 0) {
    if(fieldStr.length()>0){
      fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
    }
    return url + fieldStr;


  }





  public static void main(String[] args) throws Exception {

    TableOrder to = null;

		to = new TableOrder();
		to.addURL("page.jsp");
		to.addField("STATUS", "CLOSED");
		to.addField("NAME", "200406");
		to.addOrderField("NAME1", to.ASC);
        to.addOrderField("NAME2", to.ASC);
		//to.addOrderField("ID", to.ASC);


    //
//
//
//		HashMap ht = new HashMap();
//		ht.put("STATUS", "CLOSED");
//		ht.put("NAME", "200406");
//		ht.put("FIELD1", "1");
//		ht.put("FIELD2", "2");
//		ht.put("FIELD3", "3");
//
//
//		to = new TableOrder();
//		to.addURL("page.jsp?PAGE_OFFSET=1");
//		to.addField(ht);
//		to.addOrderField("NAME", to.DESC);
//		to.addOrderField("ID", to.ASC);
//
//		to = new TableOrder();
//		to.addURL("page.jsp?PAGE_OFFSET=1");
//		to.addField(new String[]{"NAME", "FIELD1", "FIELD2"}, ht);
//		to.addOrderField("NAME", to.DESC);
//		to.addOrderField("ID", to.ASC);


  }

}
