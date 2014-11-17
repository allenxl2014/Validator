package com.liang.mobile.jbean;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;
import com.liang.util.JBeanApplication;
import com.liang.util.Operator;
import com.liang.util.ValidatorDataStructure;

public class PhotoLister extends JBeanApplication{
	/* 返回日志处理接口 */
	public PhotoLister(){}
	private DBBusinessAssembler dbManager;
	
	private Operator tmpOperator;
	
	public PhotoLister(HttpServletRequest req) throws Exception{
		initialize(req);
	}
	public void initialize(HttpServletRequest req) throws Exception{
		super.initialize(req);
	    
		operator = new Operator("1");
		operator.setDSConnectionId(ValidatorDataStructure.DS_CONN_ID$Validator);
		dbManager = new DBBusinessAssembler(resManager);
	}
	
	/**
	 * 获得考勤列表
	 * @return
	 * @throws Exception
	 */
	public Element getPhotoList() throws Exception{
		try{
			transactionManager.begin();
			
			String isValidate = "select isValidate from productIndex where productIndex.searchIndex = productInfo.searchIndex and  productIndex.productId = productInfo.productId";
			Column[] columns = new Column[]{
					new Column("productName"),
					new Column("productPhoto"),
					new Column("isValidate", isValidate)
			};
			
			return dbManager.getRecords(operator, new String[]{"productInfo"}, false, columns, null, 
					null, "productId",
					null,
					null, 
					null);
		
		}finally{
			transactionManager.close();
		}
	}
	
	public static void main(String args[]){
		System.out.println("+8613408553494".substring(3, "+8613408553494".length()));
	}

}
