package com.liang.ejb.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import com.liang.util.ValidatorErrorCode;
import com.liang.util.OutputXMLBuilder;
import com.liang.util.Operator;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DBBusinessAssembler {
  DBManager dbManager = null;
  
  public DBBusinessAssembler(ResourceManager rm) {
    dbManager = new DBManager(rm);
  }


  /**
   * Create record
   * @param inputEL 
   * @return
   * @throws JThinkException
   */
  public void createRecord( Operator operator, String tableName, HashMap fieldsHM) throws JThinkException {
    /* Create record */
    dbManager.createRecord( operator, tableName, fieldsHM);
  }

  /**
   * Update record
   * @param inputEL
   * @return
   * @throws JThinkException
   */
  public void updateRecord( Operator operator, String tableName, HashMap fieldsHM, Condition condition) throws JThinkException {
    /* Update record */
    dbManager.updateRecord( operator, tableName, fieldsHM, condition);

  }

  /**
   * Delete record
   * @param inputEL
   * @return
   * @throws JThinkException
   */
  public void deleteRecord( Operator operator, String tableName, Condition condition) throws JThinkException {

    /* Delete record */
    dbManager.deleteRecord( operator, tableName, condition);

  }


  /**
   * Get record list
   * @param connId
   * @param tableName
   * @param fieldNames
   * @param conditionHT
   * @param orderBy
   * @param pageOffsetStr
   * @param pageRowSizeStr
   * @param pageRowTotalStr
   * @param rootELName
   * @param recordELName
   * @return
   * @throws JThinkException
   */
  private Element getRecords(Operator operator, String tableName,boolean distinct,
                                 Column[] columns, Condition condition,
                                 String groupby, String orderby, String pageOffsetStr,
                                 String pageRowSizeStr, String pageRowTotalStr,
                                 String rootELName) throws
      JThinkException {

    String sqlStr = "";
    List recordCL = null;

    if (pageOffsetStr != null && pageRowSizeStr != null) { // Has paging info
      int pageOffset = Integer.parseInt(pageOffsetStr);
      int pageRowSize = Integer.parseInt(pageRowSizeStr);
      /* Query Data */
      recordCL = dbManager.getRecords(operator, tableName, distinct, columns,
                                              condition, groupby, orderby,
                                              pageOffset, pageRowSize);
      /* Get page row total */
      if (pageRowTotalStr == null || pageRowTotalStr.equals("0")) {
        pageRowTotalStr = String.valueOf(dbManager.get_Record_Count(
            operator, tableName, condition, "*"));
      }
    }
    else { // Has not paging info
      recordCL = dbManager.getRecords(operator, tableName, distinct, columns,
                                              condition, groupby, orderby);
    }

    Element resultEL = OutputXMLBuilder.buildReturnElement(rootELName, 
        recordCL);
    XMLHelper.setAttribute(resultEL, "PAGE_ROW_TOTAL", pageRowTotalStr);

    return resultEL;
  }

  /**
   * Get record list
   * @param connId
   * @param tableNames
   * @param fieldNames
   * @param conditionHT
   * @param orderBy
   * @param pageOffsetStr
   * @param pageRowSizeStr
   * @param pageRowTotalStr
   * @param rootELName
   * @param recordELName
   * @return
   * @throws JThinkException
   */
  private Element getRecords(Operator operator, String[] tableNames,boolean distinct,
                                 Column[] columns, Condition condition,
                                 String groupby, String orderby, String pageOffsetStr,
                                 String pageRowSizeStr, String pageRowTotalStr,
                                 String rootELName) throws
      JThinkException {

    String sqlStr = "";
    List recordCL = null;

    if (pageOffsetStr != null && pageRowSizeStr != null) { // Has paging info
      int pageOffset = Integer.parseInt(pageOffsetStr);
      int pageRowSize = Integer.parseInt(pageRowSizeStr);
      /* Query Data */
      recordCL = dbManager.getRecords(operator, tableNames, distinct, columns,
                                              condition, groupby, orderby,
                                              pageOffset, pageRowSize);
      /* Get page row total */
      if (pageRowTotalStr == null || pageRowTotalStr.equals("0")) {
        pageRowTotalStr = String.valueOf(dbManager.get_Record_Count(
            operator, tableNames, condition, "*"));
      }
    }
    else { // Has not paging info
      recordCL = dbManager.getRecords(operator, tableNames, distinct, columns,
                                              condition, groupby, orderby);
    }

    Element resultEL = OutputXMLBuilder.buildReturnElement(rootELName,
        recordCL);
    XMLHelper.setAttribute(resultEL, "PAGE_ROW_TOTAL", pageRowTotalStr);

    return resultEL;
  }


  /**
   * Get record list
   * @param inputEL
   * @return
   * @throws JThinkException
   */
  public Element getRecords(Operator operator, String[] tableNames, boolean distinct,
                                 Column[] columns, Condition condition,
                                 String groupby, String orderby, String pageOffsetStr,
                                 String pageRowSizeStr, String pageRowTotalStr) throws JThinkException {

		Element resultEL = null;
		if(tableNames.length>1){
			/* 对多个表合并查询 */
	   	resultEL = getRecords( operator, tableNames,distinct,
                                 columns, condition,
                                 groupby, orderby, pageOffsetStr,
                                 pageRowSizeStr, pageRowTotalStr,
	                               "getRecords");

		}else{
    	/* 只对一个表操作 */
    	resultEL = getRecords( operator, tableNames[0], distinct,
                                 columns, condition,
                                 groupby, orderby, pageOffsetStr,
                                 pageRowSizeStr, pageRowTotalStr,
                                 "getRecords");
		}
    
    return resultEL;
  }


	

  /**
   * Get record info
   * @param inputEL
   * @return
   * @throws JThinkException
   */
  public Element getRecordInfo(Operator operator, String tableName, Column[] columns, Condition condition) throws JThinkException {

    /* Get record list*/
    List recordLT = dbManager.getRecords( operator, tableName, false, columns,
                                                 condition,
                                                 null, null);
                                                 
    if (recordLT.size() == 0) {
      throw new JThinkException(ValidatorErrorCode.HPY_OBJECT_NOT_EXIST, "记录不存在!检查初始数据是否正确配置!");
    }
    /* Return result */
    return (Element)recordLT.get(0);
  }

	
	/**
	 * 计算表数据行数量
	 */
	public int count(Operator operator, String[] tableNames, Condition condition) throws JThinkException{
			return dbManager.get_Record_Count( operator, tableNames, condition, "*");
	}

	/**
	 * 计算表数据行数量
	 */
	public int count(Operator operator, String tableName, Condition condition) throws JThinkException{
			return dbManager.get_Record_Count( operator, tableName, condition, "*");
	}
	
  /**
   * 返回指定列的最大值


   */
  public String max(Operator operator, String tableName, String fieldName, Condition condition) throws JThinkException{
    Column[] columns = {
      new Column("max("+fieldName+") as MaxValue"),
    };
    Iterator recordIT = dbManager.getRecords( operator, tableName, false, columns,
                                                 condition,
                                                 null, null).iterator();
    if(recordIT.hasNext()){
     return ((Element)recordIT.next()).getAttributeValue("MaxValue");
    }
    return null;
  }
  
  /**
   * 返回指定列的最小值


   */
  public String min(Operator operator, String tableName, String fieldName, Condition condition) throws JThinkException{
    Column[] columns = {
      new Column("min("+fieldName+") as MinValue"),
    };
    Iterator recordIT = dbManager.getRecords( operator, tableName, false, columns,
                                                 condition,
                                                 null, null).iterator();
    if(recordIT.hasNext()){
     return ((Element)recordIT.next()).getAttributeValue("MinValue");
    }
    return null;
  }
  
	/**
	 * 执行一个查询操作的SQL语句
	 */
	public List executeSQL(Operator operator, SQL sql) throws JThinkException{
		
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = dbManager.getSQLBuilder(connId);
    SQLExecutor sqlExecutor = dbManager.getSQLExecutor(connId);   
    
	return (List)sqlExecutor.execute(sql);
		
	}


	/**
	 * 返回数据库所有表的名称属性
	 */
	public Element getTableList(Operator operator, String connId) throws JThinkException{
		return OutputXMLBuilder.buildReturnElement("get_Table_List", dbManager.get_Table_List(operator));
	}
	
	/**
	 * 返回指定表的所有字段以及字段相关属性
	 */
	public Element getFieldList(Operator operator, String tableName) throws JThinkException{
		return OutputXMLBuilder.buildReturnElement("get_Field_List", dbManager.get_Field_List(operator, tableName));
	}


}