package com.liang.util;

import javax.servlet.http.HttpServletRequest;

import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.j2ee.web.HttpSession;
import org.fto.jthink.resource.ResourceManager;

/**
 * @author Allen
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class SessionTimeoutChecker {

	public SessionTimeoutChecker(HttpServletRequest req) throws JThinkException {
		ResourceManager resManager = (ResourceManager) req
				.getAttribute(ResourceManager.class.getName());

		String isErrorPage = (String) req.getAttribute("IS_ERROR_PAGE");
		if (isErrorPage == null) {
			String isLoginPage = (String) req.getAttribute("IS_LOGIN_PAGE");
			Operator operator = (Operator) resManager.getResource(
					HttpSession.class.getName(), Operator.class.getName());
			if (isLoginPage == null) {
				if (operator == null) {
					throw new JThinkException(0,
							"因长时间未使用，为保护数据安全，系统已断开连接，请重新登录!");
				}
			}
		}
	}

}
