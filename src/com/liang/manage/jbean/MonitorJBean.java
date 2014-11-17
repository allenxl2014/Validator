package com.liang.manage.jbean;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.util.DateTimeHelper;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;
import com.liang.ejb.id.IDGenerator;
import com.liang.util.JBeanApplication;
import com.liang.util.Operator;
import com.liang.util.ValidatorBeanInitialization;
import com.liang.util.ValidatorDataStructure;

public class MonitorJBean extends JBeanApplication {

	private ResourceManager resManager;
	
	private DBBusinessAssembler dbManager;
	private Operator operator;
	private IDGenerator idGenerator;

	public MonitorJBean() {}

	public MonitorJBean(HttpServletRequest req) throws Exception {
		initialize(req);
	}
	public void initialize(HttpServletRequest req) throws Exception{
		super.initialize(req);
		
	    /* 初始化JavaBean，返回资源管理器 */
	    resManager = new ValidatorBeanInitialization().initialize(req);
	    
		operator = (Operator)resManager.getResource(HttpSession.class.getName(), Operator.class.getName());
		dbManager = new DBBusinessAssembler(resManager);
		
		idGenerator = new IDGenerator(request.getServletRequest());
	}
	
	/*
	 * 公司列表
	 */
	public Element getProductList() throws Exception{
		try{
			transactionManager.begin();
			Condition condition = new Condition();
			String compId = operator.getCompId();
			condition.add(new ConditionItem("compId", "=", compId));
			Column[] columns = new Column[]{
					new Column("productId"),
					new Column("productName"),
			};
			
			return dbManager.getRecords(operator, new String[]{"productInfo"}, false, columns, condition, null,
					null, null,null,null);
			
		}finally{
			transactionManager.close();
		}
	}
	
	
	/**
	 * 获得产品列表
	 * @return
	 * @throws Exception
	 */
	public Element getMonitorList() throws Exception{
		try{
			transactionManager.begin();
			String productId = getString("productId");
			String productNo = getString("productNo");
			String isValidate = getString("isValidate");
			String validateTime1 = getString("validateTime1");
			String validateTime2 = getString("validateTime2");
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("productIndex.compId", "=", operator.getCompId()));
			
			if(!productId.trim().equals("")){
				condition.add(new ConditionItem("productIndex.productId", "=", productId));
			}
			if(!productNo.trim().equals("")){
				condition.add(new ConditionItem("productIndex.productNo", "=", productNo));
			}
			if(isValidate.trim().equals("0")){
				condition.add(new ConditionItem("productIndex.isValidate", "=", isValidate));
			}else if(!isValidate.trim().equals("")){
				condition.add(new ConditionItem("validateLog.validateSucc", "=", isValidate));
			}
			if(!validateTime1.equals("")){
				condition.add(new ConditionItem("validateLog.validateTime", ">=", validateTime1));
			}
			if(!validateTime2.equals("")){
				condition.add(new ConditionItem("validateLog.validateTime", "<=", DateTimeHelper.formatDateTimetoString(validateTime2, "yyyy-MM-dd HH:mm:ss")));
			}
			
			Column[] columns = new Column[]{
					new Column("productIndex.piId"),
					new Column("productIndex.productId"),
					new Column("productIndex.productSerial"),
					new Column("productIndex.productNo"),
					new Column("productIndex.isValidate"),
					new Column("productInfo.productName"),
					new Column("productInfo.productAds"),
					new Column("productInfo.productTime"),
					new Column("productInfo.productPeriod"),
					new Column("validateLog.validateSucc"),
					new Column("validateLog.vlPhone"),
					new Column("validateLog.vlCityId"),
					new Column("validateLog.vlCity"),
					new Column("validateLog.validateTime"),
					new Column("validateLog.positionX"),
					new Column("validateLog.positionY"),
					
			};
			String tableName = "productIndex LEFT JOIN productInfo on productIndex.productId = productInfo.productId AND productIndex.compId = productInfo.compId" +
					" LEFT JOIN validateLog ON productIndex.productId = validateLog.productId and productIndex.itemId = validateLog.itemId";
			
			return dbManager.getRecords(operator, new String[]{tableName}, false, columns, condition, null,
					request.getParameter(ValidatorDataStructure.ORDERBY), 
					String.valueOf(getPageOffset()),
					String.valueOf(getPageRows()), 
					String.valueOf(getPageTotalRows()));
			
		}finally{
			transactionManager.close();
		}
	}

	
	
	/**
	 * 获得监控扫描产品列表
	 * @return
	 * @throws Exception
	 */
	public Element getNewAuthorList(String monitTime) throws Exception{
		try{
			transactionManager.begin();
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("productIndex.compId", "=", operator.getCompId()));
			condition.add(new ConditionItem("validateLog.validateTime", ">=", monitTime));

			
			Column[] columns = new Column[]{
					new Column("productIndex.piId"),
					new Column("productIndex.productId"),
					new Column("productIndex.productSerial"),
					new Column("productIndex.productNo"),
					new Column("productIndex.isValidate"),
					new Column("productInfo.productName"),
					new Column("productInfo.productAds"),
					new Column("productInfo.productTime"),
					new Column("productInfo.productPeriod"),
					new Column("validateLog.validateSucc"),
					new Column("validateLog.vlPhone"),
					new Column("validateLog.vlCityId"),
					new Column("validateLog.vlCity"),
					new Column("validateLog.validateTime"),
					new Column("validateLog.positionX"),
					new Column("validateLog.positionY"),
			};
			String tableName = "productIndex " +
					"LEFT JOIN productInfo on productIndex.productId = productInfo.productId AND productIndex.compId = productInfo.compId " +
					"LEFT JOIN validateLog ON productIndex.productId = validateLog.productId and productIndex.itemId = validateLog.itemId";
			
			return dbManager.getRecords(operator, new String[]{tableName}, false, columns, condition, null,
					"productIndex.validateTime", null, null, null);
			
		}finally{
			transactionManager.close();
		}
	}
	
}
