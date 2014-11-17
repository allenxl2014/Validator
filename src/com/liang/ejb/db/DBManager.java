package com.liang.ejb.db;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.jdbc.SQLExecutorEvent;
import org.fto.jthink.jdbc.SQLExecutorListener;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.TransactionManager;
import org.jdom.Element;

import com.liang.util.Operator;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

class DBManager{

	private static final Logger logger = LogManager.getLogger(DBManager.class);
//	private ResourceManager resManager;
	private JDBCTransaction transaction;
//	private SQLBuilder sqlBuilder;
//	private SQLExecutor sqlExecutor;


  public DBManager(ResourceManager resManager){
		TransactionManager transactionManager = (TransactionManager)resManager.getResource(TransactionManager.class.getName());
		transaction = (JDBCTransaction)transactionManager.getTransaction(JDBCTransaction.class.getName());
		
  }

	public SQLBuilder getSQLBuilder(String connId){
		/* 创建SQLBuilder */
		return transaction.getSQLBuilderFactory(connId).create("");
	}


	public SQLExecutor getSQLExecutor(String connId)	{
		/* 创建SQLExecutor */
		SQLExecutor sqlExecutor = transaction.getSQLExecutorFactory(connId).create();
		
		/* 设置SQLExecutor监听器 */
		sqlExecutor.addSQLExecutorListener(new SQLExecutorListener(){
			/* 监听器的事件方法，当在执行SQL语句时调用此方法 */
			public void executeSQLCommand(SQLExecutorEvent evt) {
				logger.debug(evt.getSQL().getSQLString());
			}
			
		});
		
		return sqlExecutor;
	}
	

  /**
   * Create record
   * @param tableName
   * @param fieldsHT
   * @throws JThinkException
   */
  public void createRecord(Operator operator, String tableName, HashMap fieldsHM) throws
      JThinkException {
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
    
    SQL sql = sqlBuilder.constructSQLForInsert(tableName, fieldsHM);
    sqlExecutor.execute(sql);
  }

  /**
   * Update record
   * @param tableName
   * @param fieldsHT
   * @param conditionHT
   * @throws JThinkException
   */
  public void updateRecord( Operator operator, String tableName, HashMap fieldsHM,
                            Condition condition) throws JThinkException {
                            	
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
    
    SQL sql = sqlBuilder.constructSQLForUpdate(tableName, fieldsHM,
        condition);
    sqlExecutor.execute(sql);
    
  }

  public void deleteRecord( Operator operator, String tableName, Condition condition) throws
      JThinkException {
      	
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
      	
    SQL sql = sqlBuilder.constructSQLForDelete(tableName, condition);
    
    sqlExecutor.execute(sql);

  }
  
  
	/**
	 * 查询一个表，不分页
	 */
  public List getRecords( Operator operator, String tableName, boolean distinct,
                              Column[] columns, Condition condition,
                              String groupby, String orderby) throws
      JThinkException {
      	
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
      	
    SQL sql = sqlBuilder.constructSQLForSelect(tableName, distinct, columns,
        condition, groupby, orderby);
        
    return (List) sqlExecutor.execute(sql);
  }

	/**
	 * 查询一个表，要分页
	 */
  public List getRecords( Operator operator, String tableName, boolean distinct,
                              Column[] columns, Condition condition,
                              String groupby, String orderby, int pageOffset, int pageRowSize
                              ) throws
      JThinkException {
      	
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
      	
    SQL sql = sqlBuilder.constructSQLForSelect(tableName, distinct, columns,
        condition,
        groupby, orderby, pageOffset * pageRowSize - pageRowSize, pageRowSize);
    List dataInfos = (List) sqlExecutor.execute(sql);
    return dataInfos;
  }
  
	/**
	 * 合并查询多个表，不分页
	 */
  public List getRecords( Operator operator, String tableNames[],boolean distinct,
                              Column[] columns, Condition condition,
                              String groupby, String orderby) throws
      JThinkException {

    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);

    String subSqlStr = "";
    List values = new ArrayList();
		for(int i=0; i<tableNames.length; i++){
			SQL sql = sqlBuilder.constructSQLForSelect(tableNames[i], null, condition);
    	subSqlStr += sql.getSQLString();
	    addToList(values, sql.getValues());
	    if(i<tableNames.length-1){
	    	subSqlStr += "\n   UNION ALL \n";
	    }
		}
    SQL sql = sqlBuilder.constructSQLForSelect("("+subSqlStr+") AS RECORDS", distinct, columns,
        null,
        groupby, orderby);
    addToList(values, sql.getValues());
    return (List) sqlExecutor.executeQuery(sql.getSQLString(), values.toArray());
  }

	/**
	 * 向List中加入数组中的每一个对象
	 */
	private void addToList(List lt, Object[] objs){
		int len = objs.length;
		for(int i=0;i<len;i++){
			lt.add(objs[i]);
		}
	}

	/**
	 * 合并查询多个表，要分页
	 */
  public List getRecords( Operator operator, String tableNames[],boolean distinct,
                              Column[] columns, Condition condition,
                              String groupby, String orderby, int pageOffset, int pageRowSize) throws
      JThinkException {
      	
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
      	
    String subSqlStr = "";  	
    List values = new ArrayList();
		for(int i=0; i<tableNames.length; i++){
			SQL sql = sqlBuilder.constructSQLForSelect(tableNames[i], null, condition);
    	subSqlStr += sql.getSQLString();
	    addToList(values, sql.getValues());

	    if(i<tableNames.length-1){
	    	subSqlStr += "\n   UNION ALL \n";
	    }
		}
    SQL sql = sqlBuilder.constructSQLForSelect("("+subSqlStr+") AS RECORDS", distinct, columns,
        null,
        groupby, orderby, pageOffset * pageRowSize - pageRowSize, pageRowSize);
		addToList(values, sql.getValues());
		
    return (List) sqlExecutor.executeQuery(sql.getSQLString(), values.toArray(),
                                       sql.getRowStartIndex(),
                                       sql.getRowLength());
  }
  
  
  /**
   * 返回表记录数, 一个表
   */
  public int get_Record_Count( Operator operator, String tableName,
                              Condition condition, String fieldName) throws
      JThinkException {
      	
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
      	
    int pageRowTotal = 0;
    SQL sql = sqlBuilder.constructSQLForCount(tableName, fieldName,
        "ATTR_NAME", condition);
    List countLT = (List) sqlExecutor.execute(sql);
    if (countLT.size() > 0) {
      pageRowTotal = Integer.parseInt( ((Element) countLT.get(0)).
          getAttributeValue("ATTR_NAME"));
    }
    return pageRowTotal;
  }
  
  /**
   * 返回表记录数, 多个表
   */
  public int get_Record_Count(Operator operator, String tableNames[],
                              Condition condition, String fieldName) throws
      JThinkException {
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);

    int pageRowTotal = 0;
		for(int i=0; i<tableNames.length; i++){
	    SQL sql = sqlBuilder.constructSQLForCount(tableNames[i], fieldName,
	        "ATTR_NAME", condition);
	    List countLT = (List) sqlExecutor.execute(sql);
  	    if (countLT.size() > 0) {
	      pageRowTotal += Integer.parseInt( ((Element) countLT.get(0)).
	          getAttributeValue("ATTR_NAME"));
	    }
		}
    return pageRowTotal;
  }
  
  

	/**
	 * 返回数据库所有表的名称以及相关信息
	 */
	public List get_Table_List(Operator operator) throws JThinkException{
    String connId = operator.getDSConnectionId();
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
		ResultSet rs = null;
		try {
			DatabaseMetaData ewkMD = transaction.getConnection(connId).getMetaData();

			rs = ewkMD.getTables(null,"dbo",null,null);
			return (List)sqlExecutor.getResultMaker().create(rs);
			
		} catch (SQLException e) {
			throw new JThinkException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, "SQL exception", e);
		}finally{
			releaseResultSet(rs);
		}
		
	}

	/**
	 * 返回数据库表所有字段信息
	 */
	public List get_Field_List(Operator operator, String tableName) throws JThinkException{
    String connId = operator.getDSConnectionId();
    SQLBuilder sqlBuilder = getSQLBuilder(connId);
    SQLExecutor sqlExecutor = getSQLExecutor(connId);
		ResultSet rs = null;
		try {
			DatabaseMetaData ewkMD = transaction.getConnection(connId).getMetaData();
			rs = ewkMD.getColumns(null, null,tableName,null);
			List lt = (List)sqlExecutor.getResultMaker().create(rs);
			return lt;
			
		} catch (SQLException e) {
			throw new JThinkException(JThinkErrorCode.ERRCODE_DB_EXEC_SQL_EXCEPTION, "SQL exception", e);
		}finally{
			releaseResultSet(rs);
		}

	}	

//	/**
//	 * 动态修改 IS_READ 和 IS_CHANGE for SM4X2005
//	 * @param flagType 标志类型:CASE/TASK,扩展用，暂时不用传值
//	 * @param flagAction 动作类型:READ/CHANGE
//	 * @param caseID CASE_ID
//	 * @param empID 用户ID
//	 * @param 数据库连接ID
//	 */
//  public void setReadChangeFlag(String flagType, String flagAction, String caseID, String empID, ) throws JThinkException {
//    String sqlStr = DB_SQLBuilder.constructSQL_ForUpdateReadChangeFlag(flagType, flagAction, caseID, empID);
//    executeUpdate_Command(getConnection(connID), sqlStr);
//  }


  /**
   * 释放ResultSet资源
   */
  private static void releaseResultSet(ResultSet resultset) throws JThinkException {
    try {
      if (resultset != null) {
        resultset.close();
      }
    }
    catch (SQLException ex) {
      throw new JThinkException(JThinkErrorCode.ERRCODE_DB_CLOSE_RS_FAILURE, ex);
    }
  }


}