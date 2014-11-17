package com.liang.ejb.id;

//import java.io.File;
//import java.util.HashMap;
//
//import org.fto.jthink.exception.JThinkException;
//import org.fto.jthink.j2ee.web.WEBApplicationContext;
//import org.fto.jthink.resource.ResourceManager;
//import org.fto.jthink.util.XMLHelper;
//import org.jdom.Element;
//
//import com.myfun.util.MyfunConfiguration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.jdbc.SQL;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;
import com.liang.util.JBeanApplication;
import com.liang.util.Operator;

/**
 * @author Allen
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class IDGenerator extends JBeanApplication {
	
private DBBusinessAssembler dbManager;
  
  public IDGenerator(){}
  
  public IDGenerator(HttpServletRequest req) throws Exception{
    initialize(req);
  }
  
  public void initialize(HttpServletRequest req) throws Exception{
    super.initialize(req);
    
    dbManager = new DBBusinessAssembler(resManager);

    operator = (Operator)resManager.getResource(HttpSession.class.getName(),Operator.class.getName());
  }
	
  /**
   * 生成ID
   */
  public String generateId(String tbType, String idType) throws JThinkException{
  	try{
  		transactionManager.begin();
  		String result = "1";
	    String sqlStr = "SELECT (MAX(" + idType + ") + 1) AS MAXID FROM " + tbType;
	    
	    SQL sql = new SQL(SQL.SELECT, sqlStr, null);
	    
	    List dataInfos = dbManager.executeSQL(operator, sql);
	    
	    if(dataInfos.size() > 0){
	    	Element dataInfoEL = (Element)dataInfos.get(0);
	    	String maxId = dataInfoEL.getAttributeValue("MAXID");
	    	if(maxId != null && !maxId.equals("")){
	    		result = maxId;
	    	}
	    }
	    return result;
  	}finally{
  		transactionManager.close();
  	}
  }   
	
	
}
