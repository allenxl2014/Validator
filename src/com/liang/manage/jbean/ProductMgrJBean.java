package com.liang.manage.jbean;

import java.util.HashMap;

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

public class ProductMgrJBean  extends JBeanApplication {

	private ResourceManager resManager;
	
	private DBBusinessAssembler dbManager;
	private Operator operator;
	private IDGenerator idGenerator;

	public ProductMgrJBean() {
	}

	public ProductMgrJBean(HttpServletRequest req) throws Exception {
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
	
	/**
	 * 获得产品列表
	 * @return
	 * @throws Exception
	 */
	public Element getProductList() throws Exception{
		try{
			transactionManager.begin();
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("compId", "=", operator.getCompId()));
			
			Column[] columns = new Column[]{
					new Column("productId"),
					new Column("productName"),
			};
			
			return dbManager.getRecords(operator, new String[]{"productInfo"}, false, columns, condition, null,
					"addtime desc", null, null, null);
			
		}finally{
			transactionManager.close();
		}
	}

	/**
	 * 获得经销商列表
	 * @return
	 * @throws Exception
	 */
	public Element getDistList() throws Exception{
		try{
			transactionManager.begin();
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("compId", "=", operator.getCompId()));
			
			Column[] columns = new Column[]{
					new Column("distId"),
					new Column("distName"),
					new Column("cityName"),
			};
			
			String tableName = "distributor LEFT JOIN City ON distributor.distCity = City.cityId";
			return dbManager.getRecords(operator, new String[]{tableName}, false, columns, condition, null,
					"createTime desc", null, null, null);
			
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 添加产品出库
	 * @throws Exception
	 */
	public void addOuter() throws Exception{
		String productId = getString("productId");
		String distId = getString("distId");
		String cityId = getString("cityId");
		String cityName = getString("cityName");
		String startNo = getString("startNo");
		String endNo = getString("endNo");
		String contractNo = getString("contractNo");
		String transportNo = getString("transportNo");
		HashMap inputHM = new HashMap();
		inputHM.put("compId", operator.getCompId());
		inputHM.put("contractNo", contractNo);
		inputHM.put("distId", distId);
		inputHM.put("productId", productId);
		inputHM.put("startNo", startNo);
		inputHM.put("endNo", endNo);
		inputHM.put("transportNo", transportNo);
		inputHM.put("outTime", DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate()));
		inputHM.put("outUid", operator.getOperator());
		inputHM.put("inSale", "0");
		
		Condition condition = new Condition();
		condition.add(new ConditionItem("productId", "=", productId));
		condition.add(new ConditionItem("productSeq", ">=", startNo));
		condition.add(new ConditionItem("productSeq", "<=", endNo));
		
		HashMap itemHM = new HashMap();
		itemHM.put("distId", distId);
		itemHM.put("itemStatus", "1");

		try{
			transactionManager.begin();
			String shipmentId = idGenerator.generateId("shipment", "shipmentId");
			inputHM.put("shipmentId", shipmentId);
			dbManager.createRecord(operator, "shipment", inputHM);
			
			itemHM.put("shipmentId", shipmentId);
			dbManager.updateRecord(operator, "productItem", itemHM, condition);
			transactionManager.commit();
		}catch(Exception e){
			transactionManager.rollback();
			throw e;
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Element getContractList() throws Exception{
		try{
			transactionManager.begin();
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("shipment.compId", "=", operator.getCompId()));
			condition.add(new ConditionItem("inSale", "=", "0"));
			
			Column[] columns = new Column[]{
					new Column("shipmentId"),
					new Column("shipment.productId"),
					new Column("contractNo"),
					new Column("distName"),
					new Column("productName"),
					new Column("startNo"),
					new Column("endNo"),
					new Column("transportNo"),
					new Column("outTime"),
					new Column("userName"),
			};
			
			String tableName = "shipment LEFT JOIN users ON shipment.outUid = users.userId" +
					" LEFT JOIN productInfo ON productInfo.productId = shipment.productId" + 
					" LEFT JOIN distributor ON distributor.distId = shipment.distId";
			return dbManager.getRecords(operator, new String[]{tableName}, false, columns, condition, null,
					"outTime desc", null, null, null);
			
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 更新入库
	 * @throws Exception
	 */
	public void addInner() throws Exception{
		String shipmentId = getString("shipmentId");
		String productId = getString("productId");
		String startNo = getString("startNo");
		String endNo = getString("endNo");
		System.out.println("shipmentId==================" + shipmentId);
		System.out.println("productId==================" + productId);
		System.out.println("startNo==================" + startNo);
		System.out.println("endNo==================" + endNo);
		
		HashMap inputHM = new HashMap();
		inputHM.put("inSale", "1");
		inputHM.put("inTime", DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate()));
		inputHM.put("inUser", operator.getOperator());
		
		Condition condition = new Condition();
		condition.add(new ConditionItem("shipmentId", "=", shipmentId));
		
		HashMap itemHM = new HashMap();
		itemHM.put("itemStatus", "2");
		Condition itemCon = new Condition();
		itemCon.add(new ConditionItem("productId", "=", productId));
		itemCon.add(new ConditionItem("productSeq", ">=", startNo));
		itemCon.add(new ConditionItem("productSeq", "<=", endNo));
		
		try{
			transactionManager.begin();
			
			dbManager.updateRecord(operator, "shipment", inputHM, condition);
			
			dbManager.updateRecord(operator, "productItem", itemHM, itemCon);
			transactionManager.commit();
		}catch(Exception e){
			transactionManager.rollback();
			throw e;
		}finally{
			transactionManager.close();
		}
	}
	
	
}
