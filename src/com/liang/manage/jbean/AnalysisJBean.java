package com.liang.manage.jbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.util.DateTimeHelper;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;
import com.liang.ejb.id.IDGenerator;
import com.liang.util.JBeanApplication;
import com.liang.util.Operator;
import com.liang.util.ValidatorBeanInitialization;
import com.liang.util.ValidatorDataStructure;

public class AnalysisJBean extends JBeanApplication {

	private ResourceManager resManager;
	
	private DBBusinessAssembler dbManager;
	private Operator operator;

	public AnalysisJBean() {
	}

	public AnalysisJBean(HttpServletRequest req) throws Exception {
		initialize(req);
	}
	public void initialize(HttpServletRequest req) throws Exception{
		super.initialize(req);
		
	    /* 初始化JavaBean，返回资源管理器 */
	    resManager = new ValidatorBeanInitialization().initialize(req);
	    
		operator = (Operator)resManager.getResource(HttpSession.class.getName(), Operator.class.getName());
		dbManager = new DBBusinessAssembler(resManager);
	}
	
	/**
	 * 出库报表
	 * @return
	 * @throws Exception
	 */
	public Element getOuterList() throws Exception{
		String outTime1 = getString("outTime1");
		String outTime2 = getString("outTime2");
		
		Condition condition = new Condition();
		condition.add(new ConditionItem("shipment.compId", "=", operator.getCompId()));
		if(!outTime1.equals("")){
			condition.add(new ConditionItem("shipment.outTime", ">=", outTime1));
		}
		if(!outTime2.equals("")){
			condition.add(new ConditionItem("shipment.outTime", "<=", DateTimeHelper.formatDateTimetoString(outTime2, "yyyy-MM-dd 23:59:59")));
		}
		
		Column[] columns = new Column[]{
				new Column("shipmentId"),
				new Column("contractNo"),
				new Column("startNo"),
				new Column("endNo"),
				new Column("cityName"),
				new Column("outTime"),
				new Column("outUid"),
				new Column("productName"),
				new Column("distName"),
				new Column("transportNo"),
		};
		
		String tableName = "shipment LEFT JOIN productInfo ON productInfo.productId = shipment.productId" +
				" LEFT JOIN distributor ON distributor.distId = shipment.distId " +
				" LEFT JOIN City ON distributor.distCity = City.cityId ";
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
	
	
	/**
	 * 入库报表
	 * @return
	 * @throws Exception
	 */
	public Element getInnerList() throws Exception{
		String provinceId = getString("provinceId");
		String cityId = getString("cityId");
		String distId = getString("distId");
		String inTime1 = getString("inTime1");
		String inTime2 = getString("inTime2");
		
		Condition condition = new Condition();
		condition.add(new ConditionItem("shipment.compId", "=", operator.getCompId()));
		condition.add(new ConditionItem("shipment.inSale", "=", "1"));
		if(!distId.equals("")){
			condition.add(new ConditionItem("shipment.distId", "=", distId));
		}else if(!cityId.equals("")){
			String childStr = "SELECT distId FROM distributor where distCity = " + cityId;
			condition.add(new ConditionItem("shipment.distId", "IN", new SQL(SQL.SELECT, childStr, null)));
		}else if(!provinceId.equals("")){
			String childStr = "SELECT distId FROM distributor where distProvince = " + provinceId;
			condition.add(new ConditionItem("shipment.distId", "IN", new SQL(SQL.SELECT, childStr, null)));
		}
		if(!inTime1.equals("")){
			condition.add(new ConditionItem("shipment.inTime", ">=", inTime1));
		}
		if(!inTime2.equals("")){
			condition.add(new ConditionItem("shipment.inTime", "<=", DateTimeHelper.formatDateTimetoString(inTime2, "yyyy-MM-dd 23:59:59")));
		}
		
		Column[] columns = new Column[]{
				new Column("shipmentId"),
				new Column("contractNo"),
				new Column("startNo"),
				new Column("endNo"),
				new Column("inTime"),
				new Column("productName"),
		};
		
		String tableName = "shipment LEFT JOIN productInfo ON productInfo.productId = shipment.productId";
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
	
	/**
	 * 获取销售报表
	 * @return
	 * @throws Exception
	 */
	public Element getSalesList() throws Exception{
		String validateTime1 = getString("validateTime1");
		String validateTime2 = getString("validateTime2");
		
		Condition condition = new Condition();
		condition.add(new ConditionItem("productItem.compId", "=", operator.getCompId()));
		condition.add(new ConditionItem("productItem.isValidate", "=", "1"));
		if(!validateTime1.equals("")){
			condition.add(new ConditionItem("validateTime", ">=", validateTime1));
		}
		if(!validateTime2.equals("")){
			condition.add(new ConditionItem("validateTime", "<=", DateTimeHelper.formatDateTimetoString(validateTime2, "yyyy-MM-dd 23:59:59")));
		}
		
		Column[] columns = new Column[]{
				new Column("productName"),
				new Column("productSerial"),
				new Column("productNo"),
				new Column("itemUnit"),
				new Column("createTime"),
				new Column("itemStatus"),
				new Column("isValidate"),
				new Column("validateTime"),
		};
		
		String tableName = "productItem LEFT JOIN productInfo ON productInfo.productId = productItem.productId";
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
	
	/**
	 * 库存明细报表
	 * @return
	 * @throws Exception
	 */
	public Element getStockList() throws Exception{
		String provinceId = getString("provinceId");
		String cityId = getString("cityId");
		String distId = getString("distId");
		String inTime1 = getString("inTime1");
		String inTime2 = getString("inTime2");
		
		Condition condition = new Condition();
		condition.add(new ConditionItem("productItem.compId", "=", operator.getCompId()));
		condition.add(new ConditionItem("productItem.itemStatus", "=", "2"));
		condition.add(new ConditionItem("productItem.isValidate", "=", "0"));
		
		if(!distId.equals("")){
			condition.add(new ConditionItem("shipment.distId", "=", distId));
		}else if(!cityId.equals("")){
			String childStr = "SELECT distId FROM distributor where distCity = " + cityId;
			condition.add(new ConditionItem("shipment.distId", "IN", new SQL(SQL.SELECT, childStr, null)));
		}else if(!provinceId.equals("")){
			String childStr = "SELECT distId FROM distributor where distProvince = " + provinceId;
			condition.add(new ConditionItem("shipment.distId", "IN", new SQL(SQL.SELECT, childStr, null)));
		}
		if(!inTime1.equals("")){
			condition.add(new ConditionItem("shipment.inTime", ">=", inTime1));
		}
		if(!inTime2.equals("")){
			condition.add(new ConditionItem("shipment.inTime", "<=", DateTimeHelper.formatDateTimetoString(inTime2, "yyyy-MM-dd 23:59:59")));
		}
		
		Column[] columns = new Column[]{
				new Column("productName"),
				new Column("productSerial"),
				new Column("productNo"),
				new Column("shipment.inTime"),
		};
		
		String tableName = "productItem LEFT JOIN productInfo ON productInfo.productId = productItem.productId" +
				" LEFT JOIN shipment ON shipment.shipmentId = productItem.shipmentId";
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
	
	/**
	 *  获得市场销售动态统计表
	 * @return
	 * @throws Exception
	 */
	public List getSellCount() throws Exception{
		String provinceId = getString("provinceId");
		String cityId = getString("cityId");
		String distId = getString("distId");
		String validateTime1 = getString("validateTime1");
		String validateTime2 = getString("validateTime2");
		
		Condition condition = new Condition();
		condition.add(new ConditionItem("1", "=", "1"));
		if(!distId.equals("")){
			condition.add(new ConditionItem("productitem.distId", "=", distId));
		}else if(!cityId.equals("")){
			String childStr = "SELECT distId FROM distributor where distCity = " + cityId;
			condition.add(new ConditionItem("productitem.distId", "IN", new SQL(SQL.SELECT, childStr, null)));
		}else if(!provinceId.equals("")){
			String childStr = "SELECT distId FROM distributor where distProvince = " + provinceId;
			condition.add(new ConditionItem("productitem.distId", "IN", new SQL(SQL.SELECT, childStr, null)));
		}
		if(!validateTime1.equals("")){
			condition.add(new ConditionItem("productitem.validateTime", ">=", validateTime1));
		}
		if(!validateTime2.equals("")){
			condition.add(new ConditionItem("productitem.validateTime", "<=", DateTimeHelper.formatDateTimetoString(validateTime2, "yyyy-MM-dd 23:59:59")));
		}
		
		String realStr = "select productId, shipmentId, itemUnit, count(*) as count from productitem " +
				"where compId = " + operator.getCompId() + " and isValidate = 1 and " + condition.getConditionString() + 
						" group by productId, shipmentId, itemUnit";
		
		String cityStr = "select productitem.productId, productitem.shipmentId, productitem.itemUnit, count(*) as count"
				    + " from productitem, shipment, distributor "
					+ " where productitem.compId = " + operator.getCompId() 
					+ " and productitem.isValidate = 1 and productitem.shipmentId = shipment.shipmentId "
					+ " and shipment.distId = distributor.distId and productitem.vlCityId <> distributor.distCity and " 
					+ condition.getConditionString()
					+ " group by productitem.productId, productitem.shipmentId, productitem.itemUnit";
		
		SQL realSql = new SQL(SQL.SELECT, realStr, condition.getValues());
		SQL citySql = new SQL(SQL.SELECT, cityStr, condition.getValues());
		
		try{
			transactionManager.begin();
			List<Element> resultList = new ArrayList<Element>();
			List<Element> real = dbManager.executeSQL(operator, realSql);
			List<Element> reals = this.constructList(real);
			List<Element> city = dbManager.executeSQL(operator, citySql);
			List<Element> citys = this.constructList(city);
			
			for(int i=0; i<reals.size(); i++){
				Element dataInfoEL = new Element("dataInfo");
				Element realEL = reals.get(i);
				String productId = XMLHelper.getAttributeString(realEL, "productId");
				String shipmentId = XMLHelper.getAttributeString(realEL, "shipmentId");
				String countP = XMLHelper.getAttributeString(realEL, "countP");
				String countX = XMLHelper.getAttributeString(realEL, "countX");
				
				dataInfoEL.setAttribute("productId", productId);
				dataInfoEL.setAttribute("shipmentId", shipmentId);
				dataInfoEL.setAttribute("realCountP", countP);
				dataInfoEL.setAttribute("realCountX", countX);
				
				for(int j=0; j<citys.size(); j++){
					Element cityEL = citys.get(j);
					String productId1 = XMLHelper.getAttributeString(cityEL, "productId");
					String shipmentId1 = XMLHelper.getAttributeString(cityEL, "shipmentId");
					String countP1 = XMLHelper.getAttributeString(cityEL, "countP");
					String countX1 = XMLHelper.getAttributeString(cityEL, "countX");
					if(productId.equals(productId1) && shipmentId.equals(shipmentId1)){
						dataInfoEL.setAttribute("cityCountP", countP1);
						dataInfoEL.setAttribute("cityCountX", countX1);
						citys.remove(j);
						j--;
					}
				}
				resultList.add(dataInfoEL);
			}
			
			for(int i=0; i<citys.size(); i++){
				Element dataInfoEL = new Element("dataInfo");
				Element cityEL = citys.get(i);
				String productId = XMLHelper.getAttributeString(cityEL, "productId");
				String shipmentId = XMLHelper.getAttributeString(cityEL, "shipmentId");
				String countP = XMLHelper.getAttributeString(cityEL, "countP");
				String countX = XMLHelper.getAttributeString(cityEL, "countX");
				dataInfoEL.setAttribute("productId", productId);
				dataInfoEL.setAttribute("shipmentId", shipmentId);
				dataInfoEL.setAttribute("cityCountP", countP);
				dataInfoEL.setAttribute("cityCountX", countX);
				resultList.add(dataInfoEL);
			}
			
			HashMap productHM = new HashMap();
			HashMap sellHM = new HashMap();
			HashMap cityHM = new HashMap();
			for(int i=0; i<resultList.size(); i++){
				Element resultEL = resultList.get(i);
				String productId = XMLHelper.getAttributeString(resultEL, "productId");
				String shipmentId = XMLHelper.getAttributeString(resultEL, "shipmentId");
				if(!productHM.containsKey(productId)){
					String productName = this.getProductName(productId);
					resultEL.setAttribute("productName", productName);
					productHM.put(productId, productName);
				}else{
					resultEL.setAttribute("productName", (String)productHM.get(productId));
				}
				if(!sellHM.containsKey(shipmentId)){
					String[] names = this.getdistName(shipmentId);
					resultEL.setAttribute("cityName", names[0]);
					resultEL.setAttribute("distName", names[1]);
					cityHM.put(shipmentId, names[0]);
					sellHM.put(shipmentId, names[1]);
				}else{
					resultEL.setAttribute("cityName", (String)cityHM.get(shipmentId));
					resultEL.setAttribute("distName", (String)sellHM.get(shipmentId));
				}
			}
			return resultList;
			
		}finally{
			transactionManager.close();
		}
	}
	
	//将瓶、箱统计合为一条记录
	public List<Element> constructList(List<Element> oldList){
		List<Element> newList = new ArrayList<Element>();
		HashMap newHM = new HashMap();
		for(int i=0; i< oldList.size(); i++){
			Element dataInfoEL = oldList.get(i);
			String productId = XMLHelper.getAttributeString(dataInfoEL, "productId");
			String shipmentId = XMLHelper.getAttributeString(dataInfoEL, "shipmentId");
			String itemUnit = XMLHelper.getAttributeString(dataInfoEL, "itemUnit");
			String count = XMLHelper.getAttributeString(dataInfoEL, "count");
			newHM.put(productId + "_" + shipmentId + "_" + itemUnit, count);
		}
		
		for(int i=0; i< oldList.size(); i++){
			Element dataInfoEL = oldList.get(i);
			String productId = XMLHelper.getAttributeString(dataInfoEL, "productId");
			String shipmentId = XMLHelper.getAttributeString(dataInfoEL, "shipmentId");
			String itemUnit = XMLHelper.getAttributeString(dataInfoEL, "itemUnit");
			if(newHM.containsKey(productId + "_" + shipmentId + "_0") || newHM.containsKey(productId + "_" + shipmentId + "_1")){
				Element tempEL = new Element("tempEL");
				tempEL.setAttribute("productId", productId);
				tempEL.setAttribute("shipmentId", shipmentId);
				if(newHM.containsKey(productId + "_" + shipmentId + "_0")){
					tempEL.setAttribute("countP", (String)newHM.get(productId + "_" + shipmentId + "_0"));
					newHM.remove(productId + "_" + shipmentId + "_0");
				}else{
					tempEL.setAttribute("countP", "0");
				}
				if(newHM.containsKey(productId + "_" + shipmentId + "_1")){
					tempEL.setAttribute("countX", (String)newHM.get(productId + "_" + shipmentId + "_1"));
					newHM.remove(productId + "_" + shipmentId + "_1");
				}else{
					tempEL.setAttribute("countX", "0");
				}
				newList.add(tempEL);
			}
		}
		return newList;
	}

	//市场监管考核统计表中 将瓶、箱统计合为一条记录
	public List<Element> constructPerformanceList(List<Element> oldList){
		List<Element> newList = new ArrayList<Element>();
		HashMap newHM = new HashMap();
		for(int i=0; i< oldList.size(); i++){
			Element dataInfoEL = oldList.get(i);
			String vlPhone = XMLHelper.getAttributeString(dataInfoEL, "vlPhone");
			String vlCityId = XMLHelper.getAttributeString(dataInfoEL, "vlCityId");
			String itemUnit = XMLHelper.getAttributeString(dataInfoEL, "itemUnit");
			String count = XMLHelper.getAttributeString(dataInfoEL, "count");
			newHM.put(vlPhone + "_" + vlCityId + "_" + itemUnit, count);
		}
		
		for(int i=0; i< oldList.size(); i++){
			Element dataInfoEL = oldList.get(i);
			String vlPhone = XMLHelper.getAttributeString(dataInfoEL, "vlPhone");
			String vlCityId = XMLHelper.getAttributeString(dataInfoEL, "vlCityId");
			String descript = XMLHelper.getAttributeString(dataInfoEL, "descript");
			String cityName = XMLHelper.getAttributeString(dataInfoEL, "cityName");
			if(newHM.containsKey(vlPhone + "_" + vlCityId + "_0") || newHM.containsKey(vlPhone + "_" + vlCityId + "_1")){
				Element tempEL = new Element("tempEL");
				tempEL.setAttribute("vlPhone", vlPhone);
				tempEL.setAttribute("vlCityId", vlCityId);
				tempEL.setAttribute("descript", descript);
				tempEL.setAttribute("cityName", cityName);
				if(newHM.containsKey(vlPhone + "_" + vlCityId + "_0")){
					tempEL.setAttribute("countP", (String)newHM.get(vlPhone + "_" + vlCityId + "_0"));
					newHM.remove(vlPhone + "_" + vlCityId + "_0");
				}else{
					tempEL.setAttribute("countP", "0");
				}
				if(newHM.containsKey(vlPhone + "_" + vlCityId + "_1")){
					tempEL.setAttribute("countX", (String)newHM.get(vlPhone + "_" + vlCityId + "_1"));
					newHM.remove(vlPhone + "_" + vlCityId + "_1");
				}else{
					tempEL.setAttribute("countX", "0");
				}
				newList.add(tempEL);
			}
		}
		return newList;
	}
	
	/**
	 * 获得市场监管统计报表
	 * @throws Exception
	 */
	public List getPerformanceCount() throws Exception{
		String detectTime1 = getString("detectTime1");
		String detectTime2 = getString("detectTime2");
		
		String conditionStr = "";
		
		if(!detectTime1.equals("")){
			conditionStr += " AND detectInfo.detectTime >= '" + detectTime1 + "' "; 
		}
		if(!detectTime2.equals("")){
			conditionStr += " AND detectInfo.detectTime <= '" + DateTimeHelper.formatDateTimetoString(detectTime2, "yyyy-MM-dd 23:59:59") + "' "; 
		}
		
		String realStr = "SELECT vlPhone, descript, detectInfo.vlCityId, cityName, productitem.itemUnit, count(*) as countx FROM detectInfo " +
				"LEFT JOIN detectPhone ON detectInfo.vlPhone = detectphone.phoneNum " +
				"LEFT JOIN city ON detectInfo.vlCityId = city.cityId " +
				"LEFT JOIN productitem ON detectInfo.itemId = productitem.itemId " +
				"WHERE detectPhone.compId = 1 " + conditionStr + 
				"GROUP BY vlPhone, descript, detectInfo.vlCityId, cityName, productitem.itemUnit";
		
		String cityStr = "SELECT vlPhone, descript, detectInfo.vlCityId, cityName, productitem.itemUnit, count(*) as countx FROM detectInfo " +
				"LEFT JOIN detectPhone ON detectInfo.vlPhone = detectphone.phoneNum " +
				"LEFT JOIN city ON detectInfo.vlCityId = city.cityId " +
				"LEFT JOIN productitem ON detectInfo.itemId = productitem.itemId " +
				"WHERE detectPhone.compId = 1  AND detectInfo.cityId <> detectInfo.vlCityId " + conditionStr + 
				"GROUP BY vlPhone, descript, detectInfo.vlCityId, cityName, productitem.itemUnit";
		
		SQL realSql = new SQL(SQL.SELECT, realStr, null);
		SQL citySql = new SQL(SQL.SELECT, cityStr, null);
		
		try{
			transactionManager.begin();
			List<Element> resultList = new ArrayList<Element>();
			List<Element> real = dbManager.executeSQL(operator, realSql);
			List<Element> reals = this.constructPerformanceList(real);
			List<Element> city = dbManager.executeSQL(operator, citySql);
			List<Element> citys = this.constructPerformanceList(city);
			
			for(int i=0; i<reals.size(); i++){
				Element dataInfoEL = new Element("dataInfo");
				Element realEL = reals.get(i);
				String vlPhone = XMLHelper.getAttributeString(realEL, "vlPhone");
				String descript = XMLHelper.getAttributeString(realEL, "descript");
				String vlCityId = XMLHelper.getAttributeString(realEL, "vlCityId");
				String cityName = XMLHelper.getAttributeString(realEL, "cityName");
				String countP = XMLHelper.getAttributeString(realEL, "countP");
				String countX = XMLHelper.getAttributeString(realEL, "countX");
				
				dataInfoEL.setAttribute("vlPhone", vlPhone);
				dataInfoEL.setAttribute("descript", descript);
				dataInfoEL.setAttribute("vlCityId", vlCityId);
				dataInfoEL.setAttribute("cityName", cityName);
				dataInfoEL.setAttribute("realCountP", countP);
				dataInfoEL.setAttribute("realCountX", countX);
				
				for(int j=0; j<citys.size(); j++){
					Element cityEL = citys.get(j);
					String vlPhone1 = XMLHelper.getAttributeString(cityEL, "vlPhone");
					String descript1 = XMLHelper.getAttributeString(cityEL, "descript");
					String vlCityId1 = XMLHelper.getAttributeString(cityEL, "vlCityId");
					String cityName1 = XMLHelper.getAttributeString(cityEL, "cityName");
					String countP1 = XMLHelper.getAttributeString(cityEL, "countP");
					String countX1 = XMLHelper.getAttributeString(cityEL, "countX");
					if(vlPhone.equals(vlPhone1) && vlCityId.equals(vlCityId1)){
						dataInfoEL.setAttribute("cityCountP", countP1);
						dataInfoEL.setAttribute("cityCountX", countX1);
						citys.remove(j);
						j--;
					}
				}
				resultList.add(dataInfoEL);
			}
			
			for(int i=0; i<citys.size(); i++){
				Element dataInfoEL = new Element("dataInfo");
				Element cityEL = citys.get(i);
				String vlPhone = XMLHelper.getAttributeString(cityEL, "vlPhone");
				String descript = XMLHelper.getAttributeString(cityEL, "descript");
				String vlCityId = XMLHelper.getAttributeString(cityEL, "vlCityId");
				String cityName = XMLHelper.getAttributeString(cityEL, "cityName");
				String countP = XMLHelper.getAttributeString(cityEL, "countP");
				String countX = XMLHelper.getAttributeString(cityEL, "countX");
				dataInfoEL.setAttribute("vlPhone", vlPhone);
				dataInfoEL.setAttribute("descript", descript);
				dataInfoEL.setAttribute("vlCityId", vlCityId);
				dataInfoEL.setAttribute("cityName", cityName);
				dataInfoEL.setAttribute("cityCountP", countP);
				dataInfoEL.setAttribute("cityCountX", countX);
				resultList.add(dataInfoEL);
			}
			
			return resultList;
			
		}finally{
			transactionManager.close();
		}
			
	}
	
	private String[] getdistName(String shipmentId) throws Exception{
		try{
			transactionManager.begin();
			
			String sqlStr = "SELECT distName, cityName FROM distributor, shipment, city WHERE distributor.distId = shipment.distId " +
					"and distributor.distCity = city.cityId and shipment.shipmentId = ?";
			SQL sql = new SQL(SQL.SELECT, sqlStr, new String[]{shipmentId});
			Iterator<Element> it = dbManager.executeSQL(operator, sql).iterator();
			if(it.hasNext()){
				Element dataEL = it.next();
				return new String[]{dataEL.getAttributeValue("cityName"), dataEL.getAttributeValue("distName")};
			}
			return new String[]{"", ""};
			
		}finally{
			transactionManager.close();
		}
	}
	
	private String getProductName(String productId) throws Exception{
		try{
			transactionManager.begin();
			Condition condition = new Condition();
			condition.add(new ConditionItem("productId", "=", productId));
			Column[] columns = new Column[]{new Column("productName")};
			return dbManager.getRecordInfo(operator, "productInfo", columns, condition).getAttributeValue("productName");
		}finally{
			transactionManager.close();
		}
	}
	
	public String getUnitName(String itemUnit) {
	  	if(itemUnit.equals("1")){
	  		return "箱 ";
	  	}else{
	  		return "瓶";
	  	}
	}
	
	/**
	 * 获取打更列表
	 * @throws Exception
	 */
	public Element getperformanceList() throws Exception{
		String provinceId = getString("provinceId");
		String cityId = getString("cityId");
		String vlPhone = getString("vlPhone");
		String detectTime1 = getString("detectTime1");
		String detectTime2 = getString("detectTime2");
		
		Condition condition = new Condition();
		condition.add(new ConditionItem("detectInfo.compId", "=", operator.getCompId()));
		
		if(!cityId.equals("")){
			condition.add(new ConditionItem("detectInfo.vlCityId", "=", cityId));
		}else if(!provinceId.equals("")){
			String childStr = "SELECT cityId FROM city where provinceId = " + provinceId;
			condition.add(new ConditionItem("detectInfo.vlCityId", "IN", new SQL(SQL.SELECT, childStr, null)));
		}
		
		if(!vlPhone.equals("")){
			condition.add(new ConditionItem("detectInfo.vlPhone", "=", vlPhone));
		}
		if(!detectTime1.equals("")){
			condition.add(new ConditionItem("detectInfo.detectTime", ">=", detectTime1));
		}
		if(!detectTime2.equals("")){
			condition.add(new ConditionItem("detectInfo.detectTime", "<=", DateTimeHelper.formatDateTimetoString(detectTime2, "yyyy-MM-dd 23:59:59")));
		}
		
		Column[] columns = new Column[]{
				new Column("detectId"),
				new Column("productName"),
				new Column("productSeq"),
				new Column("positionX"),
				new Column("positionY"),
				new Column("detectResult"),
				new Column("detectTime"),
				new Column("descript"),
				new Column("detectInfo.cityId"),
				new Column("vlCityId"),
		};
		
		String tableName = "detectInfo LEFT JOIN productInfo ON detectInfo.productId = productInfo.productId" +
				" LEFT JOIN detectPhone on detectInfo.vlPhone = concat('+86',detectPhone.phoneNum)";
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
	
	/**
	 * 根据城市ID获得城市名
	 * @param cityId
	 * @return
	 * @throws Exception
	 */
	public String getCityName(String cityId) throws Exception{
		if(cityId.equals("")) return "";
		HashMap<String, String> cityHM = (HashMap<String, String>)resManager.getResource(HttpSession.class.getName(), "cityHM");
		if(cityHM != null){
			return cityHM.get(cityId);
		}
		cityHM = new HashMap<String, String>();
		try{
			transactionManager.begin();
			Column[] columns = new Column[]{
					new Column("cityId"),
					new Column("cityName"),
			};
			
			List citys = dbManager. getRecords(operator, new String[]{"city"}, false, columns, null, null,
					null, null, null, null).getChildren();
			
			for(int i=0; i<citys.size(); i++){
				Element cityEL = (Element)citys.get(i);
				cityHM.put(cityEL.getAttributeValue("cityId"), cityEL.getAttributeValue("cityName"));
			}
			resManager.setResource(HttpSession.class.getName(), "cityHM", cityHM);
			return cityHM.get(cityId);
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 获取省份列表
	 * @return
	 * @throws Exception
	 */
	public Element getProvinceList() throws Exception{
		Column[] columns = new Column[]{
				new Column("provinceId"),
				new Column("provinceName"),
		};
		try{
			transactionManager.begin();
			
			return dbManager.getRecords(operator, new String[]{"province"}, false, columns, null, null, null, null, null, null);
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 根据省份获得城市下拉列表
	 * @param provinceId
	 * @return
	 * @throws Exception
	 */
	public Element getCityList(String provinceId) throws Exception{
		if(provinceId == null || provinceId.equals("")) return new Element("dataInfosEL"); 
		Column[] columns = new Column[]{
				new Column("cityId"),
				new Column("cityName"),
		};
		Condition condition = new Condition();
		condition.add(new ConditionItem("provinceId", "=", provinceId));
		try{
			transactionManager.begin();
			
			return dbManager.getRecords(operator, new String[]{"city"}, false, columns, condition, null, null, null, null, null);
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 根据省份城市获得合同单位下拉列表
	 * @param provinceId
	 * @return
	 * @throws Exception
	 */
	public Element getDistList(String provinceId, String cityId) throws Exception{
		if(provinceId == null || provinceId.equals("")) return new Element("dataInfosEL"); 
		Column[] columns = new Column[]{
				new Column("distId"),
				new Column("distName"),
		};
		Condition condition = new Condition();
		condition.add(new ConditionItem("1", "=", "1"));
		if(provinceId != null && !provinceId.equals("")){
			condition.add(new ConditionItem("distProvince", "=", provinceId));
		}
		if(cityId != null && !cityId.equals("")){
			condition.add(new ConditionItem("distCity", "=", cityId));
		}
		try{
			transactionManager.begin();
			
			return dbManager.getRecords(operator, new String[]{"distributor"}, false, columns, condition, null, null, null, null, null);
		}finally{
			transactionManager.close();
		}
	}
	
	/**
	 * 根据省份城市获得合同单位下拉列表
	 * @param provinceId
	 * @return
	 * @throws Exception
	 */
	public Element getDetectUserList() throws Exception{
		Column[] columns = new Column[]{
				new Column("phoneNum"),
				new Column("descript"),
		};

		Condition condition = new Condition();
		condition.add(new ConditionItem("compId", "=", operator.getCompId()));
		try{
			transactionManager.begin();
			
			return dbManager.getRecords(operator, new String[]{"detectPhone"}, false, columns, condition, null, null, null, null, null);
		}finally{
			transactionManager.close();
		}
	}
	
}
