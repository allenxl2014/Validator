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

public class ProductJBean extends JBeanApplication {

	private ResourceManager resManager;
	
	private DBBusinessAssembler dbManager;
	private Operator operator;
	private IDGenerator idGenerator;

	public ProductJBean() {
	}

	public ProductJBean(HttpServletRequest req) throws Exception {
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
			String productName = getString("productName");
			String addtime1 = getString("addtime1");
			String addtime2 = getString("addtime2");
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("compId", "=", operator.getCompId()));
			System.out.println( operator.getCompId());
			
			if(!productName.trim().equals("")){
				condition.add(new ConditionItem("productName", "LIKE", "%"+productName+"%"));
			}
			if(!addtime1.equals("")){
				condition.add(new ConditionItem("addtime", ">=", addtime1));
			}
			if(!addtime2.equals("")){
				condition.add(new ConditionItem("addtime", "<=", DateTimeHelper.formatDateTimetoString(addtime2, "yyyy-MM-dd HH:mm:ss")));
			}
			
			Column[] columns = new Column[]{
					new Column("*"),
			};
			
			return dbManager.getRecords(operator, new String[]{"productInfo"}, false, columns, condition, null,
					request.getParameter(ValidatorDataStructure.ORDERBY), 
					String.valueOf(getPageOffset()),
					String.valueOf(getPageRows()), 
					String.valueOf(getPageTotalRows()));
			
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 为下拉选择准备数据
	 * @return
	 * @throws Exception
	 */
	public Element getCompList() throws Exception{
		try{
			transactionManager.begin();
			Condition condition = new Condition();
			condition.add(new ConditionItem("status", "=", "1"));
			
			Column[] columns = new Column[]{
				new Column("compId"),	
				new Column("compCname"),	
			};
			return dbManager.getRecords(operator, new String[]{"compInfo"}, false, columns, condition, null,
					"createTime desc", null, null, null);
			
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 添加产品
	 * @throws Exception
	 */
	public void addProduct() throws Exception{
		HashMap inputHM = new HashMap();
		inputHM.put("compId", request.getParameter("compId"));
		inputHM.put("productName", request.getParameter("productName"));
		inputHM.put("productAds", request.getParameter("productAds"));
		inputHM.put("productAddr", request.getParameter("productAddr"));
		inputHM.put("productCreator", request.getParameter("productCreator"));
		inputHM.put("productDesc", request.getParameter("productDesc"));
//		inputHM.put("productPhoto", request.getParameter("productPhoto"));
		inputHM.put("productTime", request.getParameter("productTime"));
		inputHM.put("productPeriod", request.getParameter("productPeriod"));
		inputHM.put("addtime", DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate()));
		inputHM.put("productTag", request.getParameter("productTag"));

		try{
			transactionManager.begin();
			
			String productId = idGenerator.generateId("productInfo", "productId");
			inputHM.put("productId", productId);

			dbManager.createRecord(operator, "productInfo", inputHM);
			
			transactionManager.commit();
		}catch(Exception e){
			transactionManager.rollback();
			throw e;
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 获得产品详细信息
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public Element getProductInfo(String productId)throws Exception{
		try{
			transactionManager.begin();
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("productId", "=", productId));
			
			return dbManager.getRecordInfo(operator, "productInfo", null, condition);
			
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 编辑产品
	 * @param productId
	 * @throws Exception
	 */
	public void editProduct(String productId) throws Exception{
		HashMap inputHM = new HashMap();
		inputHM.put("productName", request.getParameter("productName"));
		inputHM.put("productAds", request.getParameter("productAds"));
		inputHM.put("productAddr", request.getParameter("productAddr"));
		inputHM.put("productCreator", request.getParameter("productCreator"));
		inputHM.put("productDesc", request.getParameter("productDesc"));
//		inputHM.put("productPhoto", request.getParameter("productPhoto"));
		inputHM.put("productTime", request.getParameter("productTime"));
		inputHM.put("productPeriod", request.getParameter("productPeriod"));
		inputHM.put("productTag", request.getParameter("productTag"));

		try{
			transactionManager.begin();
			Condition condition = new Condition();
			condition.add(new ConditionItem("productId", "=", productId));

			dbManager.updateRecord(operator, "productInfo", inputHM, condition);
			
			transactionManager.commit();
		}catch(Exception e){
			transactionManager.rollback();
			throw e;
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 获取具体产品列表
	 * @return
	 * @throws Exception
	 */
	public Element getItemList() throws Exception{
		String productId = getString("productId");
		String packType = getString("packType");
		String itemStatus = getString("itemStatus");
		String itemSerial = getString("itemSerial");
		String outTime1 = getString("outTime1");
		String outTime2 = getString("outTime2");
		String priTb = "productItem";
		if(packType.equals("1")){
			priTb = "productPack";
		}
		Condition condition = new Condition();
		condition.add(new ConditionItem("productId", "=", productId));
		if(!itemStatus.equals("")){
			condition.add(new ConditionItem(priTb+".itemStatus", "=", itemStatus));
		}
		if(!outTime1.equals("")){
			condition.add(new ConditionItem("shipIn.shipTime", ">=", outTime1));
		}
		if(!outTime2.equals("")){
			condition.add(new ConditionItem("shipIn.shipTime", "<=", outTime2));
		}
		if(!itemSerial.equals("")){
			condition.add(new ConditionItem(priTb+".itemSerial", "like", "%" + itemSerial + "%"));
		}
		
		Column[] columns = new Column[]{
				new Column(priTb+".*"),
				new Column("outTime", "shipIn.shipTime"),
		};
		
		String tableName = priTb +
				" LEFT JOIN shipmentLog shipIn ON "+priTb+".itemId = shipIn.caseId and shipIn.shipType = 0 and " +
				" shipIn.caseType = 0 and shipIn.compId = " + operator.getCompId();
		try{
			transactionManager.begin();
			
			return dbManager.getRecords(operator, new String[]{tableName}, false, columns, condition, null,
					request.getParameter(ValidatorDataStructure.ORDERBY), 
					String.valueOf(getPageOffset()),
					String.valueOf(getPageRows()), 
					String.valueOf(getPageTotalRows()));
		}finally{
			transactionManager.close();
		}
		
	}
}
