package com.liang.session;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;
import com.liang.util.JBeanApplication;
import com.liang.util.Operator;
import com.liang.util.ValidatorDataStructure;


public class SessionJBean extends JBeanApplication {
	DBBusinessAssembler dbManager;

	public SessionJBean() {
	}

	public SessionJBean(HttpServletRequest req) throws Exception {
		initialize(req);
	}

	public void initialize(HttpServletRequest req) throws Exception {
		super.initialize(req);
		dbManager = new DBBusinessAssembler(resManager);

	}

	public void login() throws Exception{
		try{
			transactionManager.begin();
			String name = request.getParameter("loginuser");
			String pwd = request.getParameter("loginpwd");
			
			//md5加密
			com.liang.util.KeyBean keyBean = new com.liang.util.KeyBean();
			pwd = keyBean.getkeyBeanofStr(pwd);

			Operator currOperator = new Operator("0");
			currOperator.setDSConnectionId(ValidatorDataStructure.DS_CONN_ID$Validator);
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("loginUid", "=", name));
			condition.add(new ConditionItem("loginPwd", "=", pwd));
			condition.add(new ConditionItem("writeOff", "=", "0"));
			
			Column[] columns = new Column[]{
					new Column("userId"),
					new Column("compId"),
					new Column("userName"),
					new Column("userSex"),
			};
			
			Iterator userIT = dbManager.getRecords(currOperator,
					new String[] { "users" }, false, columns, condition, null,
					null, null, null, null).getChildren().iterator();
			
			if(!userIT.hasNext()){
				throw new JThinkException("用户名或密码错误!");
			}
			
			Element userInfoEL = (Element)userIT.next();
			String userId = XMLHelper.getAttributeString(userInfoEL, "userId");
			String userName = XMLHelper.getAttributeString(userInfoEL, "userName");
			String userSex = XMLHelper.getAttributeString(userInfoEL, "userSex");
			String compId = XMLHelper.getAttributeString(userInfoEL, "compId");
			
			Operator operator = new Operator(userId);
			operator.setUserName(userName);
			operator.setUserSex(userSex);
			operator.setLoginId(name);
			operator.setPassword(pwd);
			operator.setCompId(compId);
			operator.setDSConnectionId(ValidatorDataStructure.DS_CONN_ID$Validator);
			
			Condition compRelaCon = new Condition();
			compRelaCon.add(new ConditionItem("compId", "=", compId));
			
			Column[] compRelaCols = new Column[]{
					new Column("creatorComp"),
					new Column("fatherComp"),
			};
			
			Iterator relaIt = dbManager.getRecords(currOperator,
					new String[] { "compRelation" }, false, compRelaCols, compRelaCon, null,
					null, null, null, null).getChildren().iterator();
			if(relaIt.hasNext()){
				Element relaEl = (Element)relaIt.next();
				operator.setAttribute("creatorComp", XMLHelper.getAttributeString(relaEl, "creatorComp"));
				operator.setAttribute("fatherComp", XMLHelper.getAttributeString(relaEl, "fatherComp"));
			}else{
				operator.setAttribute("creatorComp", compId);
				operator.setAttribute("fatherComp", compId);
			}
			
			resManager.removeResource(HttpSession.class.getName(), Operator.class.getName());
			resManager.setResource(HttpSession.class.getName(), Operator.class.getName(), operator);
			
		}finally{
			transactionManager.close();
		}
	}
}
