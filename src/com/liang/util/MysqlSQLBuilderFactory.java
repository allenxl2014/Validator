
package com.liang.util;

import java.util.HashMap;
import java.util.Iterator;

import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLBuilderFactory;
import org.fto.jthink.jdbc.mysql.MysqlSQLBuilder;
import org.fto.jthink.util.StringHelper;

/**
 * SQLBuilder工厂，创建针对MYSQL数据库的SQLBuilder。
 * 
 * 
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink Sample Message Board 1.0
 * @see 	  JThink 1.0
 * 
 */
public class MysqlSQLBuilderFactory implements SQLBuilderFactory {

	public SQLBuilder create(String sqlBuilderId) {
		return new ValidatorMysqlBuilder();
	}


}
