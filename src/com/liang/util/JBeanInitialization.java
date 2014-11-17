package com.liang.util;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.jdbc.SQLExecutorEvent;
import org.fto.jthink.jdbc.SQLExecutorListener;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.DefaultTransactionManager;
import org.fto.jthink.transaction.TransactionFactory;
import org.fto.jthink.transaction.TransactionManager;


public class JBeanInitialization {
	/* 返回日志处理接口 */
	public static final Logger logger = LogManager.getLogger(JBeanInitialization.class);

	/* 资源管理器 */
	public ResourceManager resManager;
	/* JThink中定义的Http请求 */
	public HttpRequest request;
	/* 事务管理器 */
	public TransactionManager transactionManager;
	/* 用于执行SQL语句 */
	public SQLExecutor sqlExecutor;
	/* 用于构建SQL语句 */
	public SQLBuilder sqlBuilder;
	/* 在fto-jthink.xml中定义的数据源连接ID */
	public static final String connId = "SampleDataSource_mysql";
	
	public JBeanInitialization(){}
	
	/**
	 * 构造方法
	 * @param req HttpServletRequest请求
	 * @throws Exception
	 */
//	public JBeanInitialization(HttpServletRequest req) throws Exception{
//		initialize(req);
//	}
	
//	/**
//	 * 初始化JavaBean
//	 * @param req
//	 * @throws Exception
//	 */
//	public void initialize(HttpServletRequest req) throws Exception {
//		
//		/* 初始化JavaBean，返回资源管理器 */
//		resManager = new XYTBeanInitialization().initialize(req);
//		
//		/* 返回客户请求 */
//		request = (HttpRequest)resManager.getResource(HttpRequest.class.getName());
//		
//		/* 返回事务管理器 */
//		transactionManager = (TransactionManager)resManager.getResource(TransactionManager.class.getName());
//		
//		/* 返回JDBC事务 */
//		JDBCTransaction transaction = (JDBCTransaction)transactionManager.getTransaction(JDBCTransaction.class.getName());
//
//		/* 创建SQLExecutor */
//		sqlExecutor = transaction.getSQLExecutorFactory(connId).create();
//		
//		/* 设置SQLExecutor监听器 */
//		sqlExecutor.addSQLExecutorListener(new SQLExecutorListener(){
//			/* 监听器的事件方法，当在执行SQL语句时调用此方法 */
//			public void executeSQLCommand(SQLExecutorEvent evt) {
//				logger.debug(evt.getSQL().getSQLString());
//			}
//			
//		});
//		
//		/* 创建SQLBuilder */
//		sqlBuilder = transaction.getSQLBuilderFactory(connId).create("");
//	}
	
	/**
	 * 初始化JB
	 */
	public ResourceManager initialize(HttpServletRequest req){
		
		logger.debug("初始化JavaBean.");
		
		/* 返回资源管理器 */
		ResourceManager resManager = (ResourceManager)req.getAttribute(ResourceManager.class.getName());
		
		Configuration jthinkConfig = (Configuration)resManager.getResource(WEBApplicationContext.class.getName(), Configuration.class.getName());
		
		if(resManager.getResource(TransactionManager.class.getName())==null){
			/* 设置资源, 初始化事务管理器, 将TransactionManager加入到ResourceManager中 */
			logger.debug("初始化事务管理器, TransactionManager.");
			TransactionManager transactionManager = new DefaultTransactionManager(resManager, jthinkConfig);
			resManager.setResource(TransactionManager.class.getName(), transactionManager);
			
			// 加入WEB端事务到事务管理器

			logger.debug("初始化Web端事务, WebTransaction.");
			TransactionFactory transactionFactory = transactionManager.getTransactionFactory("WebTransaction");
			JDBCTransaction transaction  = (JDBCTransaction)transactionFactory.create();
			transactionManager.addTransaction(JDBCTransaction.class.getName(), transaction);
			
		}
		
		return resManager;
	}

}
