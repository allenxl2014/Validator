package com.liang.ujb;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.jdbc.SQLExecutorEvent;
import org.fto.jthink.jdbc.SQLExecutorListener;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import com.liang.util.ValidatorBeanInitialization;
import com.liang.util.JBeanInitialization;
import com.liang.util.Operator;

/**
 * 
 * 代码检查:
 * 1. 事务处理已经更证
 */

public class UserInfoUJBean {

	HashMap userInfosHT = null;
	HashMap provinceInfosHT = null;	//存放省份
	HashMap cityInfosHT = null;	//存放城市


	//存放当前入住企业用户信息的Map
	HashMap userInfosMap = null;
	
	/* 资源管理器 */
	private ResourceManager resManager;
	/* JThink中定义的Http请求 */
	private HttpRequest request;
	/* 事务管理器 */
	private TransactionManager transactionManager;

	/* 用于执行SQL语句 */
	public SQLExecutor sqlExecutor;
	/* 用于构建SQL语句 */
	public SQLBuilder sqlBuilder;
	/* 在fto-jthink.xml中定义的数据源连接ID */
	public static final String connId = "SampleDataSource_mysql";
	/* 返回日志处理接口 */
	public static final Logger logger = LogManager.getLogger(JBeanInitialization.class);

	Operator operator = null;

//	HashMap ddInfosMap = null;//存放当前入住企业DD信息的Map

	public UserInfoUJBean(){}
	
	public UserInfoUJBean(HttpServletRequest req) throws Exception {
		initialize(req);
	}

  public UserInfoUJBean(HttpServletRequest req,  Operator operator) throws Exception {
    initialize(req, operator);
  }

  
  public void initialize(HttpServletRequest req) throws Exception {
    initialize(req, null);
  }
  
  /**
   *  Initialization
   */
  public void initialize(HttpServletRequest req, Operator operator) throws Exception { 

    /* 初始化JavaBean，返回资源管理器 */
    resManager = new ValidatorBeanInitialization().initialize(req);

    if(operator==null){
      this.operator = (Operator)resManager.getResource(HttpSession.class.getName(), Operator.class.getName());
    }else{
      this.operator = operator;      
    }
		
		/* 返回客户请求 */
		request = (HttpRequest)resManager.getResource(HttpRequest.class.getName());
		
		/* 返回事务管理器 */
		transactionManager = (TransactionManager)resManager.getResource(TransactionManager.class.getName());
		/* 返回JDBC事务 */
		JDBCTransaction transaction = (JDBCTransaction)transactionManager.getTransaction(JDBCTransaction.class.getName());
		
		
		/* 创建SQLExecutor */
		sqlExecutor = transaction.getSQLExecutorFactory(connId).create();
		
		/* 设置SQLExecutor监听器 */
		sqlExecutor.addSQLExecutorListener(new SQLExecutorListener(){
			/* 监听器的事件方法，当在执行SQL语句时调用此方法 */
			public void executeSQLCommand(SQLExecutorEvent evt) {
				logger.debug(evt.getSQL().getSQLString());
			}
			
		});
		
		/* 创建SQLBuilder */
		sqlBuilder = transaction.getSQLBuilderFactory(connId).create("");

		//idGenerator = new IDGeneratorEJBCaller(resManager);
		
		String userContextKey = UserInfoUJBean.class.getName();

		userInfosMap = (HashMap)resManager.getResource(WEBApplicationContext.class.getName(), userContextKey);
		if(userInfosMap==null){
			userInfosMap = new HashMap();
			resManager.setResource(WEBApplicationContext.class.getName(), userContextKey, userInfosMap);
		}
		
		userInfosHT = (HashMap) userInfosMap.get("_USER_INFOS");
		if (userInfosHT == null) {
			userInfosHT = new HashMap();
			userInfosMap.put("_USER_INFOS", userInfosHT);
		}
		
		provinceInfosHT = (HashMap) userInfosMap.get("_PROVINCE_INFOS");
		if (provinceInfosHT == null) {
			provinceInfosHT = new HashMap();
			provinceInfosHT.put("_PROVINCE_INFOS", provinceInfosHT);
		}
		
		cityInfosHT = (HashMap) userInfosMap.get("_CITY_INFOS");
		if (cityInfosHT == null) {
			cityInfosHT = new HashMap();
			userInfosMap.put("_CITY_INFOS", cityInfosHT);
		}
		
  }

	/*
	 * refurbish _USER_INFOS
	 * 当在用户管理里编辑或删除了用户信息，请调用此方法
	 */
	public void refurbishUserInfo() {
		userInfosHT.clear();
//		_deptEmpInfoHT.clear();
	}
	public void refurbishUserInfo(String userId) {
		userInfosHT.remove(userId);
//		_deptEmpInfoHT.clear();
	}
  


	/**
	 * 返回当前会话用户ID(EMP_ID)
	 */
	public Operator getOperator() {
		return (Operator)resManager.getResource(HttpSession.class.getName(), Operator.class.getName());
	}

    /**
     * 返回用户ID,根据用户名称,可以只给出用户名称的一部份
     */
    public String[] getUserIdsByUserName(String userName) throws Exception {
        
        Condition condition = new Condition();
        condition.add(new ConditionItem("userName", "LIKE", "%"+userName+"%"));
        
        Column[] colums = new Column[]{
            new Column("userId")
        };
        
        SQL sql = sqlBuilder.constructSQLForSelect("users", colums, condition);
        
        List emps = (List)sqlExecutor.execute(sql);

        String[] userIds = new String[emps.size()];

        for(int i=0;i<emps.size();i++){
            Element empEL = (Element)emps.get(i);
            userIds[i] = empEL.getAttributeValue("userId");
        }
        return userIds;
    }

	
	
	/**
	 *  将Element中的员工ID换成员工姓名
	 */
	public void toUserName(Element infoEl, String attrName) throws Exception {
		String user_id = infoEl.getAttributeValue(attrName);
		if (user_id != null) {
			String user_name = getUserName(user_id);
			XMLHelper.setAttribute(infoEl, attrName, user_name);
		}
	}

	/**
	 *  将Element中的员工ID换成员工姓名
	 */
	public void toUserName(Element infoEl, String attrName, String targetAttrName) throws Exception {
		String user_id = infoEl.getAttributeValue(attrName);
		if (user_id != null) {
			String user_name = getUserName(user_id);
			XMLHelper.setAttribute(infoEl, targetAttrName, user_name);
		}
	}


	/**
	 * Get login user userName by emp id
	 */
	public String getUserName(String userId) throws Exception {
		String userName = "";
		Element empEL = getUserInfo(userId);
		if (empEL != null) {
			userName = empEL.getAttributeValue("userName");
		}
		return userName;
	}
	
	
	/**
	 * 返回用户信息
	 * Return null, if employee not exist; else return employee info, is element
	 */
	public Element getUserInfo(String userId) throws Exception {
		Element empEL = (Element) userInfosHT.get(userId);
		if (empEL == null) {
			empEL = getUserInfoA(userId);
			if (empEL != null) {
				userInfosHT.put(userId, empEL);
			}
		}
		return empEL;
	}

	private Element getUserInfoA(String userId) throws Exception {
		try {
			transactionManager.begin();

			Element employee = null;

			Condition condition = new Condition();
			condition.add(new ConditionItem("userId", "=", userId));
			
			SQL sql = sqlBuilder.constructSQLForSelect("users", null, condition);
			List empInfos = (List)sqlExecutor.execute(sql);
			
			if (empInfos.size() > 0) {
				employee = (Element) empInfos.iterator().next();
			}
			return employee;

		} finally {
			transactionManager.close();
		}
	}
	
	/**
	 * 获取省份名称
	 * @param cityId
	 * @return String
	 * @throws Exception
	 * @author 谢亮
	 */
	public String getProvinceName(String provinceId) throws Exception {
		String provinceName = (String)provinceInfosHT.get(provinceId);
		if(provinceName == null){
			List provinces = getProvinces();
			for(int i=0; i<provinces.size(); i++){
				Element provinceEL = (Element)provinces.get(i);
				provinceInfosHT.put(XMLHelper.getAttributeString(provinceEL, "provinceId"), XMLHelper.getAttributeString(provinceEL, "provinceName"));
			}
			provinceInfosHT.put("_PROVINCE_ELEMENT", new Element("dataInfosEL").addContent(provinces));
		}
		return (String)provinceInfosHT.get(provinceId);
	}
	
	/**
	 * 获取省份列表
	 * @param cityId
	 * @return Element
	 * @throws Exception
	 * @author 谢亮
	 */
	public Element getProvinceList() throws Exception {
		Element provincesEL = (Element)provinceInfosHT.get("_PROVINCE_ELEMENT");
		if(provincesEL == null){
			List provinces = getProvinces();
			for(int i=0; i<provinces.size(); i++){
				Element provinceEL = (Element)provinces.get(i);
				provinceInfosHT.put(XMLHelper.getAttributeString(provinceEL, "provinceId"), XMLHelper.getAttributeString(provinceEL, "provinceName"));
			}
			provinceInfosHT.put("_PROVINCE_ELEMENT", new Element("dataInfosEL").addContent(provinces));
		}
		return (Element)provinceInfosHT.get("_PROVINCE_ELEMENT");
	}
	
	//获得省份列表
	private List getProvinces() throws Exception {
		try {
			transactionManager.begin();
			SQL sql = sqlBuilder.constructSQLForSelect("province", null, null);
			return (List)sqlExecutor.execute(sql);
			
		} finally {
			transactionManager.close();
		}
	}
	
	/**
	 * 获取城市名称
	 * @param cityId
	 * @return String
	 * @throws Exception
	 * @author 谢亮
	 */
	public String getCityName(String cityId) throws Exception {
		String cityName = (String)cityInfosHT.get(cityId);
		if(cityName == null){
			List citys = getCitys(null);
			for(int i=0; i<citys.size(); i++){
				Element cityEL = (Element)citys.get(i);
				cityInfosHT.put(XMLHelper.getAttributeString(cityEL, "cityId"), XMLHelper.getAttributeString(cityEL, "cityName"));
			}
			cityInfosHT.put("_CITY_ELEMENT", new Element("dataInfosEL").addContent(citys));
		}
		return (String)provinceInfosHT.get(cityId);
	}
	
	/**
	 * 获取城市列表
	 * @param cityId
	 * @return Element
	 * @throws Exception
	 * @author 谢亮
	 */
	public Element getCityList(String provinceId) throws Exception {
		provinceId = provinceId == null ? "" : provinceId;
		Element citysEL = (Element)cityInfosHT.get(provinceId + "_CITY_ELEMENT");
		if(citysEL == null){
			List citys = getCitys(provinceId);
			for(int i=0; i<citys.size(); i++){
				Element cityEL = (Element)citys.get(i);
				String cityId = XMLHelper.getAttributeString(cityEL, "cityId");
				String cityName = XMLHelper.getAttributeString(cityEL, "cityName");
				cityInfosHT.put(cityId, cityName);
			}
			cityInfosHT.put(provinceId + "_CITY_ELEMENT", new Element("dataInfosEL").addContent(citys));
		}
		return (Element)cityInfosHT.get(provinceId + "_CITY_ELEMENT");
	}
	
	//获取城市列表
	private List getCitys(String provinceId) throws Exception {
		try {
			transactionManager.begin();
			
			Condition condition = new Condition();
			condition.add(new ConditionItem("1", "=", "1"));
			if(provinceId != null){
				condition.add(new ConditionItem("provinceId", "=", provinceId));
			}
			
			SQL sql = sqlBuilder.constructSQLForSelect("city", null, condition);
			return (List)sqlExecutor.execute(sql);
			
		} finally {
			transactionManager.close();
		}
	}
	
}

