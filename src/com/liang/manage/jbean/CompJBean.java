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

public class CompJBean extends JBeanApplication {

	private ResourceManager resManager;
	
	private DBBusinessAssembler dbManager;
	private Operator operator;
	private IDGenerator idGenerator;

	public CompJBean() {}

	public CompJBean(HttpServletRequest req) throws Exception {
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
	public Element getCompList() throws Exception{
		try{
			transactionManager.begin();
			String provinceId = getString("provinceId");
			String cityId = getString("cityId");
			String isFather = getString("isFather");
			String isSeller = getString("isSeller");
			String compName = getString("compName");
			Condition condition = new Condition();
			condition.add(new ConditionItem("1", "=", "1"));
			if(!provinceId.trim().equals("")){
				condition.add(new ConditionItem("provinceId", "=", provinceId));
			}
			if(!cityId.trim().equals("")){
				condition.add(new ConditionItem("cityId", "=", cityId));
			}
			if(!isFather.trim().equals("")){
				condition.add(new ConditionItem("isFather", "=", isFather));
			}
			if(!isSeller.trim().equals("")){
				condition.add(new ConditionItem("isSeller", "=", isSeller));
			}
			if(!compName.trim().equals("")){
				condition.add(new ConditionItem("compName", "LIKE", "%"+compName+"%"));
			}
			
			Column[] columns = new Column[]{
					new Column("compInfo.*")
			};
			
			String tableName = "compInfo join compRelation on compInfo.compId = compRelation.compId and compRelation.fatherComp = " + operator.getCompId();
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
	 * 获得有效的公司列表
	 * @return
	 * @throws Exception
	 */
	public Element getComps() throws Exception{
		try{
			transactionManager.begin();
			Condition condition = new Condition();
			condition.add(new ConditionItem("status", "=", "1"));
			
			Column[] columns = new Column[]{
					new Column("compId"),
					new Column("compCname"),
			};
			return dbManager.getRecords(operator, new String[]{"compInfo"}, false, null, condition, null,
					"createTime desc", 
					null,
					null, 
					null);
			
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 添加公司记录
	 * @throws Exception
	 */
	public void addComp() throws Exception{
		HashMap inputHM = new HashMap();
		inputHM.put("provinceId", request.getParameter("provinceId"));
		inputHM.put("cityId", request.getParameter("cityId"));
		inputHM.put("compName", request.getParameter("compName"));
		inputHM.put("compCname", request.getParameter("compCname"));
		inputHM.put("compContact", request.getParameter("compContact"));
		inputHM.put("contactPhone", request.getParameter("contactPhone"));
		inputHM.put("compAddr", request.getParameter("compAddr"));
		inputHM.put("compTele", request.getParameter("compTele"));
		inputHM.put("compFax", request.getParameter("compFax"));
		inputHM.put("compEmail", request.getParameter("compEmail"));
		inputHM.put("compDesc", request.getParameter("compDesc"));
		inputHM.put("status", "1");
		inputHM.put("isCreator", "0");
		inputHM.put("isFather", request.getParameter("isFather"));
		inputHM.put("isSeller", request.getParameter("isSeller"));
		inputHM.put("createUid", operator.getOperator());
		inputHM.put("createTime", DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate()));
		
		HashMap relaHM = new HashMap();
		relaHM.put("fatherComp", operator.getCompId());
		
		try{
			transactionManager.begin();
			
			String compId = idGenerator.generateId("compInfo", "compId");
			inputHM.put("compId", compId);
			dbManager.createRecord(operator, "compInfo", inputHM);
			
			String crId = idGenerator.generateId("compRelation", "crId");
			relaHM.put("crId", crId);
			relaHM.put("compId", compId);
			relaHM.put("creatorComp", operator.getAttribute("creatorComp"));
			dbManager.createRecord(operator, "compRelation", relaHM);
			
			transactionManager.commit();
		}catch(Exception e){
			transactionManager.rollback();
			throw e;
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 获取公司详细信息
	 * @param compId
	 * @return
	 * @throws Exception
	 */
	public Element getCompInfo(String compId) throws Exception{
		try{
			transactionManager.begin();
			Condition condition = new Condition();
			condition.add(new ConditionItem("compId", "=", compId));
			
			return dbManager.getRecordInfo(operator, "compInfo", null, condition);
			
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 编辑公司信息
	 * @param compId
	 * @throws Exception
	 */
	public void editComp(String compId) throws Exception{
		Condition condition = new Condition();
		condition.add(new ConditionItem("compId", "=", compId));
		
		HashMap inputHM = new HashMap();
		inputHM.put("provinceId", request.getParameter("provinceId"));
		inputHM.put("cityId", request.getParameter("cityId"));
		inputHM.put("compName", request.getParameter("compName"));
		inputHM.put("compCname", request.getParameter("compCname"));
		inputHM.put("compContact", request.getParameter("compContact"));
		inputHM.put("contactPhone", request.getParameter("contactPhone"));
		inputHM.put("compAddr", request.getParameter("compAddr"));
		inputHM.put("compTele", request.getParameter("compTele"));
		inputHM.put("compFax", request.getParameter("compFax"));
		inputHM.put("compEmail", request.getParameter("compEmail"));
		inputHM.put("compDesc", request.getParameter("compDesc"));
		inputHM.put("isFather", request.getParameter("isFather"));
		inputHM.put("isSeller", request.getParameter("isSeller"));
		
		try{
			transactionManager.begin();
			dbManager.updateRecord(operator, "compInfo", inputHM, condition);
			transactionManager.commit();
		}catch(Exception e){
			transactionManager.rollback();
			throw e;
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 检测指定公司是否存在子公司
	 * @param compId
	 * @return
	 * @throws Exception
	 */
	public int detectComp(String compId) throws Exception{
		try{
			transactionManager.begin();
			Condition condition = new Condition();
			condition.add(new ConditionItem("fatherComp", "=", compId));
			
			return dbManager.count(operator, "compRelation", condition);
			
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 注销经销商
	 * @param compId
	 * @throws Exception
	 */
	public void writeOffComp(String compId) throws Exception{
		Condition condition = new Condition();
		condition.add(new ConditionItem("compId", "=", compId));
		HashMap inputHM = new HashMap();
		inputHM.put("status", "0");
		try{
			transactionManager.begin();
			
			dbManager.updateRecord(operator, "compInfo", inputHM, condition);
			
			dbManager.deleteRecord(operator, "compRelation", condition);
			transactionManager.commit();
		}catch(Exception e){
			transactionManager.rollback();
			throw e;
		}finally{
			transactionManager.close();
		}
	}
	
	
}
