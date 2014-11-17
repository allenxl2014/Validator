package com.liang.util;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.j2ee.web.HttpRequest;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RequestHelper {

	private RequestHelper(){}

//  /**
//   * 根据给定的表字段, 将指定的HashMap中的值复制到别一个HashMap
//   */
//  public static HashMap parseToHashMap(HashMap requestHM, String tableName){
//    HashMap fieldsHM = new HashMap();
//    String[] fields = CTTBStructure.getTableFields(tableName);
//    for(int i=0;i<fields.length;i++){
//      Object value = requestHM.get(fields[i]);
//      if(value!=null){
//        fieldsHM.put(fields[i], value);
//      }
//    }
//    return fieldsHM;
//  }
//	
//	/**
//	 * 将请求中的相关数据字段加入到HashMap中,根据给定的表名称
//	 */
//	public static HashMap parseToHashMap(HttpRequest request, String tableName){
//		HashMap fieldsHM = new HashMap();
//		String[] fields = CTTBStructure.getTableFields(tableName);
//		for(int i=0;i<fields.length;i++){
//			String value = request.getParameter(fields[i]);
//			if(value!=null){
//				fieldsHM.put(fields[i], value);
//			}
//		}
//		return fieldsHM;
//	}

	/**
	 * 将请求中的所有数据字段加入到HashMap中	 */	
	public static HashMap parseToHashMap(HttpRequest request){
		HashMap fieldsHM = new HashMap();
		Enumeration fields = request.getParameterNames();
		while(fields.hasMoreElements()){
			String key = (String)fields.nextElement();
			String value = request.getParameter(key);
			if(value!=null){
				fieldsHM.put(key, value);
			}
		}
		return fieldsHM;
	}

	/**
	 * 将请求中的所有数据字段加入到HashMap中	 */	
	public static HashMap parseToHashMap(HttpServletRequest request){
		HashMap fieldsHM = new HashMap();
		Enumeration fields = request.getParameterNames();
		while(fields.hasMoreElements()){
			String key = (String)fields.nextElement();
			String value = request.getParameter(key);
			if(value!=null){
				fieldsHM.put(key, value);
			}
		}
		return fieldsHM;
	}

}
