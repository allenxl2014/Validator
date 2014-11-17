package com.liang.util;

import java.io.BufferedReader;
import java.io.FileReader;

import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ValidatorConfiguration {

	private Element xytConfig;

	public ValidatorConfiguration(Element xytConfig){
		this.xytConfig = xytConfig;
	}
	
	public ValidatorConfiguration(String configfile){
		xytConfig = XMLHelper.load(configfile);
	}
	
	public Element getConfig(){
		return xytConfig;
	}

  /**
   * Get exception message info
   */
  public Element getExceptionMessage() {
    String file = xytConfig.getChild("ExceptionMessage").getChild("File").getText();
    return XMLHelper.load(file);
  }

}
