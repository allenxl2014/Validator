package com.liang.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.j2ee.web.WEBApplicationContext;
import org.fto.jthink.j2ee.web.util.HTMLHelper;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.jdbc.JDBCTransaction;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.jdbc.SQLExecutor;
import org.fto.jthink.jdbc.SQLExecutorEvent;
import org.fto.jthink.jdbc.SQLExecutorListener;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.transaction.DefaultTransactionManager;
import org.fto.jthink.transaction.TransactionFactory;
import org.fto.jthink.transaction.TransactionManager;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

import com.liang.ejb.db.DBBusinessAssembler;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JBeanApplication {

	/* 返回日志处理接口 */
	protected static final Logger logger = LogManager
			.getLogger(JBeanApplication.class);

	/* 资源管理器 */
	protected ResourceManager resManager;
	/* JThink中定义的Http请求 */
	protected HttpRequest request;
	/* 事务管理器 */
	protected TransactionManager transactionManager;

	/* 用于执行SQL语句 */
	protected SQLExecutor sqlExecutor;
	/* 用于构建SQL语句 */
	protected SQLBuilder sqlBuilder;
	/* 在fto-jthink.xml中定义的数据源连接ID */
	private static final String connId = "SampleDataSource_mysql";

	protected Operator operator;

	protected int pagePer = 0;
	
	/**
	 * 初始化JavaBean
	 * @param req
	 * @throws Exception
	 */
	protected void initialize(HttpServletRequest req) throws Exception {

		/* 初始化JavaBean，返回资源管理器 */
		resManager = new JBeanInitialization().initialize(req);

		operator = (Operator) resManager.getResource(HttpSession.class
				.getName(), Operator.class.getName());

		/* 返回客户请求 */
		request = (HttpRequest) resManager.getResource(HttpRequest.class
				.getName());

		/* 返回事务管理器 */
		transactionManager = (TransactionManager) resManager
				.getResource(TransactionManager.class.getName());

		/* 返回JDBC事务 */
		JDBCTransaction transaction = (JDBCTransaction) transactionManager
				.getTransaction(JDBCTransaction.class.getName());

		/* 创建SQLExecutor */
		sqlExecutor = transaction.getSQLExecutorFactory(connId).create();

		/* 设置SQLExecutor监听器 */
		sqlExecutor.addSQLExecutorListener(new SQLExecutorListener() {
			/* 监听器的事件方法，当在执行SQL语句时调用此方法 */
			public void executeSQLCommand(SQLExecutorEvent evt) {
				logger.debug(evt.getSQL().getSQLString());
			}

		});
		/* 创建SQLBuilder */
		sqlBuilder = transaction.getSQLBuilderFactory(connId).create("");
	}

	/**
	 * 返回参数，如果参数值为null，返回""
	 */
	public String getParameterString(String name) {
		String value = request.getParameter(name);
		return value == null ? "" : value;
	}

	/**
	 * 返回总行数
	 */
	public int getPageTotalRows() {
		if (request.getParameter("PAGE_ROW_TOTAL") != null) {
			return Integer.parseInt(request.getParameter("PAGE_ROW_TOTAL"));
		} else {
			return 0;
		}
	}

	/**
	 * 返回总行数
	 */
	public int getPageTotalRows(Element datasEL) {
		if (datasEL != null) {
			String pageRowTotal = datasEL.getAttributeValue("PAGE_ROW_TOTAL");
			return pageRowTotal == null ? 0 : Integer.parseInt(pageRowTotal);
		} else {
			return 0;
		}

	}

	/**
	 * 返回当前页偏移
	 */
	public int getPageOffset() {
		return request.getParameter("PAGE_OFFSET") != null ? Integer
				.parseInt(request.getParameter("PAGE_OFFSET")) : 1;
	}

	/**
	 * 返回每页显示的记录行数
	 */
	public int getPageRows() throws Exception {
		if (pagePer == 0) {
			//pagePer = Integer.parseInt((new ParameterUJBean(request.getServletRequest()).getParameterValue("POPUP_RESULT_NUM")));
			throw new JThinkException("没有定义每页记录行数! pagePer=" + pagePer);
		}
		return pagePer;
	}

	/**
	 * 设定每页显示的记录行数
	 */

	public void setPageRows(int pagePer) {
		this.pagePer = pagePer;
	}

	/**
	 * 返回总页数
	 */
	public int getPages(int pageTotalRows) throws Exception {
		return pageTotalRows / getPageRows()
				+ ((pageTotalRows % getPageRows()) > 0 ? 1 : 0);
	}

	/**
	 * 返回记录序号
	 */
	public int getSeqNo(int i) throws Exception {
		return (getPageOffset() - 1) * getPageRows() + i + 1;
	}

	/**
	 * 转换请求数据
	 */
	public String getString(String reqParam) throws Exception {
		String result = request.getParameter(reqParam);
		if (result == null)
			result = "";
		return result;
	}

	/**
	 * 转换输入框中特殊字符串
	 * 
	 */
	public String getInputHTMLString(String str) throws Exception {
		return (str == null || str.equals("")) ? "" : HTMLHelper.toHTMLString(
				str, HTMLHelper.ES_DEF$INPUT);
	}

	/**
	 * 根据指定的长度切取字符串
	 */
	public String format_Str(String source, int width) {
		String result = "";
		if (source != null && !source.equals("")) {
			for (int i = 0, w = 0; i < source.length() && w < width; i++) {
				char charCi = source.charAt(i);
				if (charCi > 255) {
					w += 2;
				} else {
					w++;
				}
				result += charCi;
			}
		} else {
			result = "&nbsp;";
		}
		return HTMLHelper.toHTMLString(result);
	}
	
	/** 验证一个字符串是否为null或空值 - 孔佳 */
	public boolean isNull(String[] params){
		boolean result = false;
		for (int i = 0; i < params.length; i++) {
			if(params[i] == null || "".equals(params[i].trim())){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/** */
	public String getMatchText(String regex, String text){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		String rusult = "";
		if(m.find()){    
			rusult = m.group(1);
		}
		return rusult;
	}
	
	/**
	 * 生成安装密码
	 */
	public final static String createPwd(int strLength) throws Exception{
		//因1与l不容易分清楚，所以去除
		String strChar = "1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,";
		String[] aryChar= strChar.split(",");
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<strLength; i++){
			sb.append(aryChar[(int)(Math.random()*strChar.length()/2)]);
		}
		return sb.toString();
	}

	/**
     * 复制单个文件
     * @param oldPath String
     * @param newPath String
     */
    public static void copyFile( String oldPath, String newPath ){
        try{
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File( oldPath );
            if ( oldfile.exists() ){ //文件存在时

                InputStream inStream = new FileInputStream( oldPath ); //读入原文件

                FileOutputStream fs = new FileOutputStream( newPath );
                byte[] buffer = new byte[ 1444 ];
                int length;
                while ( ( byteread = inStream.read( buffer ) ) != -1 ){
                    bytesum += byteread; //字节数 文件大小
                    fs.write( buffer, 0, byteread );
                }
                inStream.close();
            }
        }catch ( Exception e ){
            e.printStackTrace();
        }
    }

    
	/**
     * 移动单个文件
     * @param oldPath String
     * @param newPath String
     */
    public static boolean moveFile(String srcFile, String destFile) {
		// File (or directory) to be moved
		File file = new File(srcFile);
		String destPath = destFile.substring(0, destFile.lastIndexOf(File.separator));
		String fileName = destFile.substring(destFile.lastIndexOf(File.separator) + 1, destFile.length());
		
		// Destination directory
		File dir = new File(destPath);
		if(!dir.exists()){
			dir.mkdirs();
		}

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, fileName));

		return success;
	}

	  
	  /**判断是否找得到字符*/
	  public static boolean isMatch(String reg, String text){
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(text);
		if(m.find())
			return true;
		else
			return false;
	  }
	  
	  /*获取远程IP*/
	  public String getRemoteIP(HttpServletRequest request) {
		String ipAddr = "";
		if (request.getHeader("X-Forwarded-For") != null) {
			ipAddr = request.getHeader("X-Forwarded-For").split(":")[0];
		} else if (request.getHeader("X-REAL-IP") != null) {
			ipAddr = request.getHeader("X-REAL-IP");
		} else {
			ipAddr = request.getRemoteAddr();
		}
		return ipAddr;
	}
	
	/*获取输入框默认值*/
	public static String getRealAttr(Element el, String attri){
		String value = el.getAttributeValue(attri);
		value = (value == null || value.equals("")) ? "" : HTMLHelper.toHTMLString(value, HTMLHelper.ES_DEF$INPUT);
		return value;
	}

}
