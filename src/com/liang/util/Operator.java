package com.liang.util;

import java.util.Hashtable;

/**
 * 会话操作者。
 *
 * <p>Copyright (c) 2006
 * @author Allen
 * @version 1.00
 */

public class Operator implements java.io.Serializable{
  private Hashtable operatorHT = new Hashtable();
  public Operator(String operator) {
    setAttribute("OPERATOR", operator);
  }

  public Object getAttribute(String name) {
    return operatorHT.get(name);
  }

  public void setAttribute(String name, Object value) {
    operatorHT.put(name, value);
  }

  public void removeAttribute(String name) {
    operatorHT.remove(name);
  }

  public String getOperator(){
    return (String)getAttribute("OPERATOR");
  }

  public String toString(){
    return (String)getAttribute("OPERATOR");
  }

	public String getUserName(){
		return (String)getAttribute("userName");
	}

	public void setUserName(String userName){
		setAttribute("userName", userName);
	}

	public String getUserSex(){
		return (String)getAttribute("userSex");
	}

	public void setUserSex(String userSex){
		setAttribute("userSex", userSex);
	}

	public String getLoginId(){
		return (String)getAttribute("loginUid");
	}

	public void setLoginId(String loginId){
		setAttribute("loginUid", loginId);
	}

	public String getPassword(){
		return (String)getAttribute("loginPwd");		
	}

	public void setPassword(String pwd){
		setAttribute("loginPwd", pwd);
	}

	public String getCompId(){
		return (String)getAttribute("compId");		
	}

	public void setCompId(String compId){
		setAttribute("compId", compId);
	}

	public String getDSConnectionId() {
		return (String) getAttribute(ValidatorDataStructure.DS_CONNECTION_ID);
	}

	public void setDSConnectionId(String connId) {
		setAttribute(ValidatorDataStructure.DS_CONNECTION_ID, connId);
	}
	
	public void clear(){
		operatorHT.clear();
	}
}
