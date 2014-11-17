package com.liang.util;

import org.fto.jthink.exception.JThinkErrorCode;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ValidatorErrorCode{

//  public final static int HPY_INIT_JAVABEAN_FAILURE = 140001; //初始化JavaBean失败
//  public final static int HPY_BEGIN_TRANSACTION_FAILURE = 140002; //开始事物失败
//
//  public final static int HPY_USER_SESSION_TIMEOVER = 140003;//会话超时
//  public final static int HPY_COMMIT_TRANSACTION_FAILURE = 140004; //事物提交失败
//  public final static int HPY_ROLLBACK_TRANSACTION_FAILURE = 140005; //事物回退失败
//  public final static int HPY_NOT_COMMIT_TRANSACTION = 140006; //不能执行事物提交命令
//  public final static int HPY_NOT_ROLLBACK_TRANSACTION = 140007; //不能执行事物回退命令
//  public final static int HPY_CFG_FILE_NOT_EXIST = 140008;//系统配制文件不存在

  
  public final static int HPY_OBJECT_NOT_EXIST = 150000; //记录对象不存在

  
  public final static int HPY_USER_SESSION_TIMEOVER = 0; //用户会话超时!

  /* 用户管理 */
  public static final int   EJBD_OBJECT_ALL_EMPLOYEE_NOT_EXIST = 200002; //用户不存在

  
  public static final int   JSPD_ADJUST_CODE_IS_INCORRECT = 0; //验证码不正确

  public static final int   JSPD_THE_USERNAME_IS_ALREADY_EXIST = 0; //该用户名已经存在

  public static final int   JSPD_THE_OLDPWD_IS_ERROR = 0; //你输入的原密码不匹配

  public final static int JSPD_FILE_UPLOAD_FAILURE = 0; //文件上传失败
}



