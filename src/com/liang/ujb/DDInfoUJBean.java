package com.liang.ujb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.StringHelper;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;
import com.liang.util.Operator;
import com.liang.util.ValidatorBeanInitialization;
import com.liang.util.Selector;
import com.liang.util.ValidatorDataStructure;

/**
 * @author Allen
 *
 * 
 * 代码检查:
 * 1. 事务处理已经更证
 */
public class DDInfoUJBean {

  private DBBusinessAssembler dbManager = null;
  private Hashtable ddsHT = null;
  private String language = "C";

  /* 资源管理器 */
  private ResourceManager resManager;
  /* JThink中定义的Http请求 */
  private HttpRequest request;
  /* 事务管理器 */
  private TransactionManager transactionManager;

  Operator operator = null;

  HashMap ddInfosMap = null;//存放当前入住企业DD信息的Map

  public DDInfoUJBean(){}
  public DDInfoUJBean(HttpServletRequest req) throws Exception {
    initialize(req, null);
  }
  
  public DDInfoUJBean(HttpServletRequest req,  Operator operator) throws Exception {
    initialize(req, operator);
  }  

  public void initialize(HttpServletRequest req) throws Exception {
    initialize(req, null);
  }
  
  /**
   *  Initialization
   */
  public void initialize(HttpServletRequest req,  Operator operator) throws Exception {

    /* 初始化JavaBean，返回资源管理器 */
    resManager = new ValidatorBeanInitialization().initialize(req);

    if(operator==null){
      this.operator = (Operator)resManager.getResource(HttpSession.class.getName(), Operator.class.getName());
      if(this.operator == null){
    	  this.operator = new Operator("0");
    	  this.operator.setDSConnectionId(ValidatorDataStructure.DS_CONN_ID$Validator);
      }
    }else{
      this.operator = operator;
    }
    
    /* 返回客户请求 */
    request = (HttpRequest)resManager.getResource(HttpRequest.class.getName());
    
    /* 返回事务管理器 */
    transactionManager = (TransactionManager)resManager.getResource(TransactionManager.class.getName());
    
    /* 创建dbManager */
    dbManager = new DBBusinessAssembler(resManager);

    String ddContextKey = DDInfoUJBean.class.getName();
    
    ddInfosMap = (HashMap)resManager.getResource(WEBApplicationContext.class.getName(), ddContextKey);
    if(ddInfosMap==null){
      ddInfosMap = new HashMap();
      resManager.setResource(WEBApplicationContext.class.getName(), ddContextKey, ddInfosMap);
    }

    ddsHT = (Hashtable)ddInfosMap.get("_DDS_HT");
    if(ddsHT==null){
      ddsHT = new Hashtable();
      ddInfosMap.put("_DDS_HT", ddsHT);
    }

  }


/**
 * 刷新数据字典在webContent中的数据，在对数据字典进行编辑，删除操作后要调用此方法
 */
   public void refurbish_DD_Info(String dicType) throws Exception{
      remove_DD_Info(dicType);
      getDDList(dicType);
   }

/**
 * 删除数据字典在webContent中的数据
 */
   public void remove_DD_Info(String dicType){
    ddsHT.remove(dicType);
   }


/**
 *  返回数据字典的中文描述
 */
  public String getMSGValue(String dicType,String ddValue) throws Exception{
    return this.getMSGValue(getDDList(dicType),ddValue);
  }
  public String getMSGValue(Element ddListInfo,String ddValue){
    String enDisplay = ddValue;
    List ddListInfos = ddListInfo.getChildren();
    int index = XMLHelper.getIndex(ddListInfos,DIC_VALUE ,ddValue);
    if(index!=-1){
      Element dd = (Element)ddListInfos.get(index);
      enDisplay = XMLHelper.getAttributeString(dd, getMSGName());
    }
    return enDisplay;
  }

/**
 * 返回数据字典值， value1
 */ 
  public String getDDValue(String dicType,String ddValue,String ddValueName) throws Exception{
    return this.getDDValue(getDDList(dicType),ddValue,ddValueName);
  }
  
  public String getDDValue(Element ddListInfo, String ddValue, String ddValueName){
    String ddValueN = ddValue;
    List ddListInfos = ddListInfo.getChildren();
    int index = XMLHelper.getIndex(ddListInfos,DIC_VALUE ,ddValue);
    if(index!=-1){
      Element dd = (Element)ddListInfos.get(index);
      ddValueN = XMLHelper.getAttributeString(dd, ddValueName);
    }
    return ddValueN;
  }

  
  /**
   * 构建数据字典的列表选择器
   */
  public String buildDDSelector(String selectorName, String dicType, String selectedValue, String firstOption, String otherAttrbutes) throws Exception{
    
    Element ddsEL = getDDListByDicType(dicType);
    return new Selector(selectorName, ddsEL, firstOption, getMSGName(), 
            "dicValue", selectedValue, otherAttrbutes).toString();
  }

  public String buildDDSelector(String selectorName, String dicType, String selectedValue, String[] removedValues, String firstOption, String otherAttrbutes) throws Exception{
    
    Element ddsEL = (Element)getDDListByDicType(dicType).clone();
    
    Hashtable conditionHT = new Hashtable();
    for(int i=0;i<removedValues.length;i++){
      conditionHT.put(DIC_VALUE, "S,!=,"+removedValues[i]);
    }
    XMLHelper.filter(ddsEL, conditionHT);
    return new Selector(selectorName, ddsEL, firstOption, getMSGName(), 
            "dicValue", selectedValue, otherAttrbutes).toString();
  }
  
  
  /**
   * 返回DD列表信息，条件是DIC_TYPE
   */
  public Element getDDListByDicType(String dicType) throws Exception {
    return this.getDDList(dicType);
  } 
  

  final static String DIC_ID = "dicId";
  final static String DIC_TYPE = "dicType";
  final static String SEQ_NO = "seqNo";
  final static String DIC_VALUE = "dicValue";
  final static String DIC_VALUE_1 = "dicValue1";
  final static String CN_MSG = "dicCNMsg";

/*
 * Get MSG Name
 */
  private String getMSGName(){
      return CN_MSG;
  }
  
/*
  get dd item list
*/
  private Element getDDList(String dicType) throws Exception{
    Element ddListInfo = null;
    Object[] group = null;
    Element ddListEL = (Element)ddsHT.get(dicType);
    if(ddListEL==null){
      ddListEL = getDDList_By(dicType);
      ddsHT.put(dicType, ddListEL);
    }
    return ddListEL;
  }

  private Element getDDList_By(String dicType)throws Exception{
    try {
      transactionManager.begin();
      
      Condition condition = new Condition();
      condition.add(new ConditionItem(DIC_TYPE,"=",dicType));
      
      return dbManager.
            getRecords(operator, new String[]{"Dictionary"}, false, null, condition, null, "seqNo ASC", null, null, null);

    } finally{
      transactionManager.close();
    }
  }

/*
 * getLanguage
 */
   private String getLanguage(){
    return this.language;
   }

}
