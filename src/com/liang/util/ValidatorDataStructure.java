package com.liang.util;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ValidatorDataStructure {

  public final static String OBJECT_ID = "OBJECT_ID";

  /**
   * 条件节点名称，用在构建查询条件的XML条件节点的名称

   */
  public final static String CONDITION_INFO = "ConditionInfo";
  /**
   * 附加条件名称，用在构建查询条件的XML条件节点的附加条件

   */
  public final static String CONDITION_TEXT = "CONDITION_TEXT";
  /**
   * 字段名称，用在构建查询条件的XML返回字段节点的名称

   */
  public final static String FIELDS_INFO = "FieldsOutInfo";
  /**
   * 排序，用在构建查询条件的XML中指定排序字段

   */
  public final static String ORDERBY = "ORDERBY";
  /**
   * 记录，用于返回结果集的XML节点名称
   */
  public final static String RECORD = "Record";

  /**
   * 表名称，主要用在须要指定表名称的方法中   */
  public final static String TABLE_NAME = "TABLE_NAME";
  /**
   * 主键名称列表，用在须要指定主键名称的方法中   */
  public final static String TABLE_PRIMARY_KEY_NAMES = "TABLE_PRIMARY_KEY_NAMES";
  /**
   * 连接ID名称，用在须要指定数据库连接ID的方法中
   */
  public final static String DS_CONNECTION_ID = "DS_CONNECTION_ID";

  /**
   * 页偏移值，用于分页
   */
  public final static String PAGE_OFFSET = "PAGE_OFFSET";
  /**
   * 每一页的的记录行数，用于分页
   */
  public final static String PAGE_ROW_SIZE = "PAGE_ROW_SIZE";
  /**
   * 总行数，用于分页
   */
  public final static String PAGE_ROW_TOTAL = "PAGE_ROW_TOTAL";

  /**
   * 当前操作者ID，用于会话   */
  public final static String OPERATOR = "OPERATOR";

	/**
	 * UNKNOWABLE
	 */
  public static final String UNKNOWABLE = "";

	/**
	 * 数据库连接定义	 */
	public static final String DS_CONN_ID$Validator = "SampleDataSource_mysql";

}
