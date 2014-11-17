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
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.DateTimeHelper;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;
import com.liang.util.JBeanInitialization;
import com.liang.util.Operator;
import com.liang.util.ValidatorDataStructure;

public class ValidateSerailSJB extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ValidateSerailSJB() {
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

		String picPath = "http://pic.fw90.com/";
		String returnStr = "{\"msg\":\"SUCCESS_VALIDATE\",";
		boolean hasErr = false;
		String serail = request.getParameter("serail");
		if (serail == null || serail.indexOf("?") == -1) {
			out.print("{\"msg\":\"InvalidRequest\"}");
			hasErr = true;
			return;
		}
		String serPara = serail.split("\\?")[1];
		if (serPara.length() <= 6) {
			out.print("{\"msg\":\"InvalidRequest\"}");
			hasErr = true;
			return;
		}
		String qrcode = serPara.substring(0, 24);
		String pSerail = serPara.substring(24, serPara.length());
		
		Operator currOperator = new Operator("0");
		currOperator.setDSConnectionId(ValidatorDataStructure.DS_CONN_ID$Validator);
		try {
			transactionManager.begin();
		  
			Condition condition = new Condition();
			condition.add(new ConditionItem("qrcode", "=", qrcode));
			condition.add(new ConditionItem("productSerial", "=", pSerail));

			Iterator<Element> resultIt = dbManager.getRecords(currOperator,
					new String[] { "productIndex" }, false, null, condition, null,
					null, null, null, null).getChildren().iterator();
			
			if (!resultIt.hasNext()) {
				out.print("{\"msg\":\"NotFound\"}");
				hasErr = true;
		  		return;
			}
			
			Element dataInfoEL = (Element) resultIt.next();
			String productId = XMLHelper.getAttributeString(dataInfoEL, "productId");
			String isValidate = XMLHelper.getAttributeString(dataInfoEL, "isValidate");
			String itemId = XMLHelper.getAttributeString(dataInfoEL, "itemId");
			
			if(isValidate.equals("1")){
				String resultStr = "{\"msg\":\"alreadyValidate\"";
				
				Condition logCon = new Condition();
				logCon.add(new ConditionItem("productId", "=", productId));
				logCon.add(new ConditionItem("itemId", "=", itemId));
//				int logCount = dbManager.count(currOperator, "validateLog", logCon);

				logCon.add(new ConditionItem("validateSucc", "=", "1"));
				Iterator<Element> logIt = dbManager.getRecords(currOperator,
						new String[] { "validateLog" }, false, null, logCon, null,
						null, null, null, null).getChildren().iterator();
				
				if (logIt.hasNext()){
					Element lgoEL = (Element) logIt.next();
					String validateTime = DateTimeHelper.formatDateTimetoString(XMLHelper.getAttributeString(lgoEL, "validateTime"), "yyyy-MM-dd HH:mm");
					resultStr += ",\"validateTime\":\"" + validateTime + "\"";
				}
				resultStr += "}";
				
				out.println(resultStr);
				hasErr = true;
				return;
			}
			
			condition = new Condition();
			condition.add(new ConditionItem("productId", "=", productId));
			Element productEL = dbManager.getRecordInfo(currOperator, "productInfo", null, condition);
			
			returnStr += "\"productId\":\""+productId+"\",";
			returnStr += "\"productName\":\""+XMLHelper.getAttributeString(productEL, "productName")+"\",";
			returnStr += "\"productAds\":\""+XMLHelper.getAttributeString(productEL, "productAds")+"\",";
			returnStr += "\"productAddr\":\""+XMLHelper.getAttributeString(productEL, "productAddr")+"\",";
			returnStr += "\"productCreator\":\""+XMLHelper.getAttributeString(productEL, "productCreator")+"\",";
			returnStr += "\"productDesc\":\""+XMLHelper.getAttributeString(productEL, "productDesc")+"\",";
			returnStr += "\"productPhoto\":\""+picPath+XMLHelper.getAttributeString(productEL, "productPhoto")+"\",";
			returnStr += "\"productTime\":\""+XMLHelper.getAttributeString(productEL, "productTime")+"\",";
			returnStr += "\"productPeriod\":\""+XMLHelper.getAttributeString(productEL, "productPeriod")+"\",";
			returnStr += "\"smsPhone\":\"13088078953\"";
			
			returnStr += "}";
			
			transactionManager.commit();
		} catch (Exception e) {
			transactionManager.rollback();
			returnStr ="{\"msg\":\"ERROR\"}";
			hasErr = true;
			e.printStackTrace();
		} finally {
			transactionManager.close();
		}
//		
//		//写日志
//		if(!hasErr){
//			try{
//				transactionManager.begin();
//				HashMap inputHM = new HashMap();
//				inputHM.put("productId", productId);
//				inputHM.put("vlPhone", );
//				inputHM.put("vlSms", );
//				inputHM.put("isValidate", );
//				inputHM.put("validateSucc", );
//				inputHM.put("validateTime", );
//				
//				dbManager.createRecord(currOperator, "validateLog", inputHM);
//				
//				transactionManager.commit();
//			} catch (Exception e) {
//				transactionManager.rollback();
//				returnStr ="{msg:\"ERROR\"}";
//				e.printStackTrace();
//			} finally {
//				transactionManager.close();
//			}
//		}
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
