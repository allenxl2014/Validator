package com.liang.validator.sjb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.DateTimeHelper;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;
import com.liang.util.JBeanInitialization;
import com.liang.util.Operator;
import com.liang.util.ValidatorDataStructure;

public class ValidateSmsSJB extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ValidateSmsSJB() {
		super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "text/html; charset=utf-8");

		PrintWriter out = resp.getWriter();

//		out.print("非法访问！:GetUserListSJB-1");
		run(req,resp);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		run(req, resp);
	}

	public void run(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/* 初始化JavaBean，返回资源管理器 */
		ResourceManager resManager = new JBeanInitialization()
				.initialize(req);

		/* 返回客户请求 */
		HttpRequest request = (HttpRequest) resManager
				.getResource(HttpRequest.class.getName());
		
		/*返回上下文配置*/
		WEBApplicationContext webContext = (WEBApplicationContext)resManager
				.getResource(WEBApplicationContext.class.getName());

		/* 返回事务管理器 */
		TransactionManager transactionManager = (TransactionManager) resManager
				.getResource(TransactionManager.class.getName());

		/* 返回JDBC事务 */
		JDBCTransaction transaction = (JDBCTransaction) transactionManager
				.getTransaction(JDBCTransaction.class.getName());

		DBBusinessAssembler dbManager = new DBBusinessAssembler(resManager);

		resp.setHeader("Content-Type", "text/html; charset=utf-8");

		PrintWriter out = resp.getWriter();

		String returnStr = "{\"msg\":\"SUCCESS_VALIDATE\"}";
		String msgTxt = request.getParameter("msgTxt");
		String senderNumber = request.getParameter("senderNumber");
		String receiveTime = request.getParameter("receiveTime");
		String validdateSucc = "0";
		String productId = "0";
		String itemId = "0";
		String productSeq = "";
		String productNo = "0";
		String productName = "";
		String productType = "";
		String parentId = "";
		String lat = "";
		String lng = "";
		String nowTime = DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate(), "yyyy-MM-dd HH:mm:ss");
		
		Operator currOperator = new Operator("0");
		currOperator.setDSConnectionId(ValidatorDataStructure.DS_CONN_ID$Validator);
		try {
			transactionManager.begin();
			if(msgTxt.length() > 52 && msgTxt.indexOf(",") == 52){
				String[] msgTxts = msgTxt.split(",");
				msgTxt = msgTxts[0];
				lat = msgTxts[1].split(":")[1];
				lng = msgTxts[2].split(":")[1];
			}
			
			String qrcode = msgTxt.substring(0, 24);
			String pSerail = msgTxt.substring(24, msgTxt.length());
		  
			Condition condition = new Condition();
			condition.add(new ConditionItem("qrcode", "=", qrcode));
			condition.add(new ConditionItem("productSerial", "=", pSerail));
			
			String sqlStr = "SELECT productName FROM productInfo WHERE productInfo.productId = productIndex.productId";
			Column[] columns = new Column[]{
					new Column("*"),
					new Column("productName", sqlStr)
			};
			
			Iterator<Element> resultIt = dbManager.getRecords(currOperator,
					new String[] { "productIndex" }, false, columns, condition, null,
					null, null, null, null).getChildren().iterator();
			
			
			Condition cityCon = new Condition();
			cityCon.add(new ConditionItem("mobileNumber", "=", senderNumber.substring(3, 10)));
			Column[] cityCols = new Column[]{
					new Column("cityId"),
					new Column("cityName")
			};
			Element cityEL = dbManager.getRecordInfo(currOperator, "mobileArea", cityCols, cityCon);
			String cityId = XMLHelper.getAttributeString(cityEL, "cityId");
			String cityName = XMLHelper.getAttributeString(cityEL, "cityName");
			
			if (!resultIt.hasNext()) {
				returnStr = "{\"msg\":\"NotFound\"}";
				validdateSucc = "0";
				
				String errStr = "SELECT productId FROM productIndex where left(productSerial, 9) = ? LIMIT 1";
				SQL errSql = new SQL(SQL.SELECT, errStr, new String[]{pSerail.substring(0, 9)});
				Iterator<Element> errIt = dbManager.executeSQL(currOperator, errSql).iterator();
				if(errIt.hasNext()){
					productId = errIt.next().getAttributeValue("productId");
				}
				
			}else{
				Element dataInfoEL = (Element) resultIt.next();
				productId = XMLHelper.getAttributeString(dataInfoEL, "productId");
				itemId = XMLHelper.getAttributeString(dataInfoEL, "itemId");
				productNo = XMLHelper.getAttributeString(dataInfoEL, "productNo");
				productName = XMLHelper.getAttributeString(dataInfoEL, "productName");
				productType = XMLHelper.getAttributeString(dataInfoEL, "productType");
				parentId = XMLHelper.getAttributeString(dataInfoEL, "parentId");
				String isValidate = XMLHelper.getAttributeString(dataInfoEL, "isValidate");
				
				if(isValidate.equals("1")){
					returnStr = "{\"msg\":\"alreadyValidate\"";
					
					Condition logCon = new Condition();
					logCon.add(new ConditionItem("productId", "=", productId));
					logCon.add(new ConditionItem("itemId", "=", itemId));
					int logCount = dbManager.count(currOperator, "validateLog", logCon);
					returnStr += ",\"logCount\":\"" + logCount + "\"";
					
					logCon.add(new ConditionItem("validateSucc", "=", "1"));
					Iterator<Element> logIt = dbManager.getRecords(currOperator,
							new String[] { "validateLog" }, false, null, logCon, null,
							null, null, null, null).getChildren().iterator();
					
					if (logIt.hasNext()){
						Element lgoEL = (Element) logIt.next();
						String validateTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(lgoEL, "validateTime"), "yyyy-MM-dd HH:mm");
						returnStr += ",\"validateTime\":\"" + validateTime + "\"";
					}
					returnStr += "}";
					
					validdateSucc = "2";
				}else{
					
					//排除白名单
					Condition whiteCon = new Condition();
					whiteCon.add(new ConditionItem("phoneNum", "=", senderNumber));
					whiteCon.add(Condition.OR, new ConditionItem("phoneNum", "=", senderNumber.substring(3, senderNumber.length())));
					int whiteCount = dbManager.count(currOperator, "whitePhone", whiteCon);
					Iterator<Element> detectIt = dbManager.getRecords(currOperator, new String[]{"detectPhone"}, 
							false, null, whiteCon, null, null, null, null, null).getChildren().iterator();
					
					if(whiteCount == 0 && !detectIt.hasNext()){
						//更新产品索引表
						HashMap inputHM = new HashMap();
						inputHM.put("isValidate", "1");
						inputHM.put("validateTime", nowTime);
						dbManager.updateRecord(currOperator, "productIndex", inputHM, condition);
						
						//更新具体产品表
						Condition itemCon = new Condition();
						itemCon.add(new ConditionItem("itemId", "=", itemId));
						HashMap itemHM = new HashMap();
						itemHM.put("itemStatus", "3");
						itemHM.put("isValidate", "1");
						itemHM.put("validateTime", nowTime);
						itemHM.put("vlCityId", cityId);
						dbManager.updateRecord(currOperator, "productItem", itemHM, itemCon);
						
						validdateSucc = "1";
					}else if(detectIt.hasNext()){
						String compId = detectIt.next().getAttributeValue("compId");
						if(!itemId.equals("")){
							String detectStr = "insert into detectInfo(compId,itemId,productSeq,distId,productId," +
									"shipmentId,cityId,vlPhone,vlCityId,positionX,positionY,detectResult,detectTime) " +
									"select productitem.compId, ?, productSeq, distributor.distId, ?, productitem.shipmentId, distCity, ?, ?, ?, ?, ?, NOW() " +
									"from productitem " +
									"left join shipment on productitem.shipmentId = shipment.shipmentId " +
									"left join distributor on shipment.distId = distributor.distId " +
									"where productitem.itemId = ?";
							SQL detectSql = new SQL(SQL.UPDATE, detectStr, new String[]{itemId, productId, senderNumber, cityId, lat, lng, "1", itemId});
							dbManager.executeSQL(currOperator, detectSql);
						}else{
							
							String detectStr = "insert into detectInfo(compId,itemId,productSeq,distId,productId," +
									"shipmentId,cityId,vlPhone,vlCityId,positionX,positionY,detectResult,detectTime) " +
									"values (?, 0, '', 0, 0, 0, 0, ?, ?, ?, ?, ?, now())";
							SQL detectSql = new SQL(SQL.UPDATE, detectStr, new String[]{compId, senderNumber, cityId, lat, lng, "0",});
							dbManager.executeSQL(currOperator, detectSql);
						}
						validdateSucc = "4";
					}else{
						validdateSucc = "3";
					}
					returnStr = "{\"msg\":\"SUCCESS_VALIDATE\",\"productNo\":\""+productNo+"\",\"productName\":\""+productName+"\",\"productType\":\""+productType+"\",\"parentId\":\""+parentId+"\"}";
				}
			}
			
			HashMap logHM = new HashMap();
			logHM.put("productId", productId);
			logHM.put("itemId", itemId);
			logHM.put("vlPhone", senderNumber);
			logHM.put("vlSms", msgTxt);
			logHM.put("validateSucc", validdateSucc);
			logHM.put("vlCityId", cityId);
			logHM.put("vlCity", cityName);
			logHM.put("positionX", lat);
			logHM.put("positionY", lng);
			logHM.put("validateTime", nowTime);
			
			dbManager.createRecord(currOperator, "validateLog", logHM);

			transactionManager.commit();
		} catch (Exception e) {
			transactionManager.rollback();
			returnStr ="{\"msg\":\"ERROR\"}";
			e.printStackTrace();
		} finally {
			transactionManager.close();
		}
		out.print(returnStr);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}


}
