package com.liang.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.util.StringHelper;

public class ValidatorMysqlBuilder extends SQLBuilder {

	  public SQL constructSQL(String type, String sqlParams){
	    return null;
	  }
	/**
	 * 构建Insert操作的SQL声明
	 * 
	 * @param tableName  表名
	 * @param columns    将要创建的记录的列和列值
	 * 
	 * @return 包含SQL串和值的SQL声明
	 */
	public SQL constructSQLForInsert(String tableName, HashMap columns){
		if(tableName==null){
			throw new IllegalArgumentException(
					"The name of an table cannot be null.");
		}
		if(columns.size()==0){
			throw new IllegalArgumentException(
					"The value of an columns cannot be empty.");
		}

    String sqlstr = "INSERT INTO "
      + tableName
      + " (#NAMES#) VALUES (#VALUES#) ";
    
	  String names = "";
	  String valueStatement = "";
//	  Object[] values  = new Object[columns.size()];
	  List values  = new ArrayList(columns.size());
	  int i=0;
	  Iterator columnNamesIT = columns.keySet().iterator();
//		while(columnNamesIT.hasNext()){
//			String columnName = (String)columnNamesIT.next();
//			Object value = columns.get(columnName);
//			if(value!=null){
//				names += "," + columnName;
//				valueStatement += ",?";
//				values[i] = value;
//			};
//			i++;
//		}
		while(columnNamesIT.hasNext()){
			String columnName = (String)columnNamesIT.next();
			Object value = columns.get(columnName);
			if(value instanceof Column){
				valueStatement += "," + columnName + "=" + ((Column)value).getColumnName();
			}else if(value != null){
				names += "," + columnName;
				valueStatement += ",?";
				values.add(value);
			}
		}
		names = names.substring(1);
		valueStatement = valueStatement.substring(1);
		sqlstr = StringHelper.replace(sqlstr, "#NAMES#", names);
		sqlstr = StringHelper.replace(sqlstr, "#VALUES#", valueStatement);
		
		return new SQL(SQL.UPDATE, sqlstr, values.toArray());
		
	}
	/**
	 * 构建更新记录操作的SQL声明
	 * 
	 * @param tableName  表名
	 * @param columns    包含有列和列值的HashMap
	 * @param condition  条件
	 * 
	 * @return 包含SQL串和值的SQL声明
	 */
  public SQL constructSQLForUpdate(String tableName,HashMap columns, Condition condition){
		if(tableName==null){
			throw new IllegalArgumentException(
					"The name of an table cannot be null.");
		}
		if(columns.size()==0){
			throw new IllegalArgumentException(
					"The value of an columns cannot be empty.");
		}
  	
    String sqlstr = "UPDATE "
        + tableName
        + " SET #NAME_AND_VALUES# ";
    
    List values  = new ArrayList(columns.size()+condition.size());
    
    /* 处理被更新的字段 */
    String namesAndValues = "";
	  Iterator columnNamesIT = columns.keySet().iterator();
		while(columnNamesIT.hasNext()){
			String columnName = (String)columnNamesIT.next();
			String value = (String)columns.get(columnName);
			if(value==null){
				namesAndValues += "," + columnName + "=NULL";
			}else{
				namesAndValues += "," + columnName + "=?";
				values.add(value);
			}
		}
		namesAndValues = namesAndValues.substring(1);
		sqlstr = StringHelper.replace(sqlstr, "#NAME_AND_VALUES#", namesAndValues);
		
    /* 处理条件 */
    if(condition!=null && condition.size()>0){
    	sqlstr += " WHERE "+condition.getConditionString();
    	Object[] objs = condition.getValues();
    	int len=objs.length;
    	for(int i=0;i<len;i++){
    		values.add(objs[i]);
    	}
    }
		
    return new SQL(SQL.UPDATE, sqlstr, values.toArray());
  }

  /**
   * 构建删除记录操作的SQL声明
   * 
   * @param tableName 表名
   * @param condition 条件
   * 
   * @return 包含SQL串和值的SQL声明
   */
  public SQL constructSQLForDelete(String tableName, Condition condition){
		if(tableName==null){
			throw new IllegalArgumentException(
					"The name of an table cannot be null.");
		}
		String sqlstr = "DELETE FROM " + tableName;
		Object[] values = null;
		if (condition != null && condition.size()>0) {
			sqlstr += " WHERE " + condition.getConditionString();
			values = condition.getValues();
		}else{
			values = new Object[0];
		}
		return new SQL(SQL.UPDATE, sqlstr, values);
	}
  
  
  /**
   * 构建查询记录操作的SQL。
   *
   * @param tableName          表名称，确定对那个表进行操作
   * @param distinct           在SELECT中是否加上DISTINCT子句
   * @param columns            列名，指定须要返回的数据字段列
   * @param condition          条件
   * @param groupby            分组字段
   * @param orderby            排序字段 
   *
   * @return 包含SQL串和值的SQL声明
   */
  public SQL constructSQLForSelect(String tableName,
                                              boolean distinct,
                                              Column[] columns,
                                              Condition condition,
                                              String groupby, String orderby
                                              ){
		if(tableName==null){
			throw new IllegalArgumentException(
					"The name of an table cannot be null.");
		}

    String distinctStr = null;
    String columnNamesStr = null;
    String conditionStr = null;
    String groupbyStr = null;
    String orderbyStr = null;
    
    List values = new ArrayList();

    /* 生成DISTINCT串 */
    if (distinct) {
      distinctStr = " DISTINCT ";
    }
    else {
      distinctStr = "";
    }
    /* 生成返回列的串 */
    if (columns != null && columns.length != 0) {
    	SQL columnSQL = constructSelectedColumn(columns);
      columnNamesStr = columnSQL.getSQLString();
    	Object[] objs = columnSQL.getValues();
    	int len=objs.length;
    	for(int i=0;i<len;i++){
    		values.add(objs[i]);
    	}
    }else{
      columnNamesStr = "*";
    }
    /* 生成查询条件串 */
    if (condition != null && condition.size() != 0) {
      conditionStr = " WHERE " + condition.getConditionString();
    	Object[] objs = condition.getValues();
    	int len=objs.length;
    	for(int i=0;i<len;i++){
    		values.add(objs[i]);
    	}      
    }else{
      conditionStr = "";
    }
    /* 生成GROUP BY串 */
    if (groupby != null && groupby.length() != 0) {
      groupbyStr = " GROUP BY " + groupby;
    }else{
      groupbyStr = "";
    }
    /* 生成ORDER BY串 */
    if (orderby != null && orderby.length() != 0) {
      orderbyStr = " ORDER BY " + orderby;
    }else{
      orderbyStr = "";
    }
    /* 返回SQL查询串 */
    String sqlStr = "SELECT " + distinctStr + columnNamesStr
												        + " FROM " + tableName
												        + conditionStr + groupbyStr + orderbyStr;

    return new SQL(SQL.SELECT, sqlStr, values.toArray());
  }

  
  /**
   * 构建查询记录操作的SQL。
   *
   * @param tableName          表名称，确定对那个表进行操作
   * @param columns           列名，指定须要返回的数据字段列
   * @param condition          条件
   *
   * @return        包含SQL串和值的SQL声明
   *
   */
  public SQL constructSQLForSelect(String tableName,
                                            Column[] columns,
																						Condition condition){
    return constructSQLForSelect(tableName, false, columns, condition, null, null);
  }

  

  /**
   * 构建查询记录操作的SQL。此方法可以实例数据分页
   *
   * @param tableName          表名称，确定对那个表进行操作
   * @param distinct           在SELECT中是否加上DISTINCT子句
   * @param columns            列名，指定须要返回的数据字段列
   * @param condition          条件
   * @param groupby            分组字段
   * @param orderby            排序字段
   * @param startIndex         开始行记录索引, 此值不能小于0
   * @param rowLen             返回记录的行数, 此值不能小于0
   * 
   * @return 	包含SQL串和值的SQL语句对象
   */
  public SQL constructSQLForSelect(String tableName,
                                              boolean distinct,
                                              Column[] columns,
                                              Condition condition,
                                              String groupby, String orderby,
																							int startIndex, int rowLen
                                              ){
    if(startIndex<0){
    	throw new JThinkRuntimeException("startIndex不能小于0!");
    }
    if(rowLen<0){
    	throw new JThinkRuntimeException("rowLen不能小于0!");
    }

    String distinctStr = null;
    String columnNamesStr = null;
    String conditionStr = null;
    String groupbyStr = null;
    String orderbyStr = null;
    String limitStr = null;

    List values = new ArrayList();
    
    /* 生成DISTINCT串 */
    if (distinct) {
      distinctStr = " DISTINCT ";
    }
    else {
      distinctStr = "";
    }
    /* 生成返回列的串 */
    if (columns != null && columns.length != 0) {
    	SQL columnSQL = constructSelectedColumn(columns);
      columnNamesStr = columnSQL.getSQLString();
    	Object[] objs = columnSQL.getValues();
    	int len=objs.length;
    	for(int i=0;i<len;i++){
    		values.add(objs[i]);
    	}
    }else{
      columnNamesStr = "*";
    }
    /* 生成查询条件串 */
    if (condition != null && condition.size() != 0) {
      conditionStr = " WHERE " + condition.getConditionString();
    	Object[] objs = condition.getValues();
    	int len=objs.length;
    	for(int i=0;i<len;i++){
    		values.add(objs[i]);
    	}      
    }else{
      conditionStr = "";
    }
    /* 生成GROUP BY串 */
    if (groupby != null && groupby.length() != 0) {
      groupbyStr = " GROUP BY " + groupby;
    }else{
      groupbyStr = "";
    }
    /* 生成ORDER BY串 */
    if (orderby != null && orderby.length() != 0) {
      orderbyStr = " ORDER BY " + orderby;
    }else{
      orderbyStr = "";
    }
    /* 生成limit串 */
    if (rowLen != -1) {
    	limitStr = " LIMIT "+startIndex +","+ rowLen;
    }
    else {
    	limitStr = "";
    }
    
    /* 生成SQL查询串 */
    String sqlStr = "SELECT " + distinctStr + columnNamesStr
        + " FROM " + tableName
        + conditionStr + groupbyStr + orderbyStr + limitStr;
    
    return new SQL(SQL.SELECT, sqlStr, values.toArray(), startIndex, rowLen);
  } 
 
  
  
  /**
   * 构建统计记录数量的SQL。
   *
   * @param tableName      表名称，确定对那个表进行操作
   * @param fieldName      字段列名，可以是“*”字符, 也可以是列名
   * @param attrName       属性名，通过此属性名称得到统计数据值
   * @param condition      条件
   *
   * @return               包含SQL串和值的SQL声明
   */
  public SQL constructSQLForCount(
  		String tableName, String fieldName, String attrName,Condition condition) {
    fieldName = fieldName==null?"*":fieldName;
    String sqlstr = "SELECT COUNT("+fieldName+") AS " + attrName + " FROM " + tableName;
    Object[] values = null;
    if (condition != null && condition.size() != 0) {
      sqlstr = sqlstr + " WHERE " + condition.getConditionString();
      values = condition.getValues();
    }else{
    	values = new Object[0]; 
    }
    return new SQL(SQL.SELECT, sqlstr, values);
  }

  
  /**
   * 构建被选择的列
   * 
   * @param columns 列对象数组  
   * 
   * @return  描述列的SQL子语句
   */
  protected SQL constructSelectedColumn(Column[] columns){
  	String columnsStr = "";
  	List valuesLT = new ArrayList();
  	int len = columns.length;
  	for(int i=0;i<len;i++){
  		SQL columnSQL = columns[i].getColumn();
  		columnsStr += ","+columnSQL.getSQLString();
			Object[] values = columnSQL.getValues();
			if(values!=null){
				int vlen = values.length;
				for(int vi=0;vi<vlen;vi++){
					valuesLT.add(values[vi]);
				}
			}
  	}
  	if(columnsStr.length()>0){
  		columnsStr = columnsStr.substring(1);
  	}
  	return new SQL(SQL.UNDEFINED,columnsStr, valuesLT.toArray());
  }

}
