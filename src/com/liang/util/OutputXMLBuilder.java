package com.liang.util;

import java.util.*;

import org.fto.jthink.exception.JThinkException;
import org.fto.jthink.util.XMLHelper;
import org.jdom.Element;
import org.jdom.Attribute;


/**
 * 用于构建EJB的输出参数。
 *
 */

public class OutputXMLBuilder {

  protected OutputXMLBuilder(){}

  /**
   * Build return XML string
   * @param rootElementName
   * @param childEL
   * @return
   */
  public static String buildReturnXML(String rootElementName,
                                           Element childEL) throws JThinkException {
    return XMLHelper.toXMLString(buildReturnElement(rootElementName, childEL));
  }
  /**
   * Build return Element
   * @param rootElementName
   * @param childEL
   * @return
   */
  public static Element buildReturnElement(String rootElementName,
                                           Element childEL){
    Element rootEL = buildReturnElement(rootElementName, (String)null);
    rootEL.addContent(childEL);
    return rootEL;
  }

  /**
   * Build return  XML string
   * @param rootElementName
   * @param childrenELs
   * @return
   */
  public static String buildReturnXML(String rootElementName,
                                           List childrenELs) throws
      JThinkException {
    return XMLHelper.toXMLString(buildReturnElement(rootElementName, childrenELs));
  }

  /**
   * Build return  Element
   * @param rootElementName
   * @param childrenELs
   * @return
   */
  public static Element buildReturnElement(String rootElementName,
                                           List childrenELs) {
    Element rootEL = buildReturnElement(rootElementName, (String)null);
    rootEL.addContent(childrenELs);
    return rootEL;
  }


  /**
   * Build return  XML string
   * @param rootElementName
   * @param objectID
   * @return if objectID == null then not add the attribute
   */
  public static String buildReturnXML(String rootElementName,
                                           String objectID) throws JThinkException {
    return XMLHelper.toXMLString(buildReturnElement(rootElementName, objectID));
  }

  public static Element buildReturnElement(String rootElementName,
                                           String objectID){
    Element rootElement = new Element(rootElementName + "_ReturnInfo");
    if (objectID != null) {
      rootElement.setAttribute(new Attribute(ValidatorDataStructure.OBJECT_ID,
                                             objectID));
    }
    return rootElement;
  }


  /**
   * Build return XML string
   * @param rootElementName
   * @return
   */
  public static String buildReturnXML(String rootElementName) throws
      JThinkException {
    return buildReturnXML(rootElementName, (String)null);
  }

  /**
   * Build return Element
   * @param rootElementName
   * @return
   */
  public static Element buildReturnElement(String rootElementName) {
    return buildReturnElement(rootElementName, (String)null);
  }



}
