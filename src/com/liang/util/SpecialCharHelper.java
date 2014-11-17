package com.liang.util;

public class SpecialCharHelper {
	
	public SpecialCharHelper(){}
	
	/**
	* 将JSON字符串处理成javascript可以识别的
	* @param ors
	* @return
	*/
	public static String dealString4JSON(StringBuilder builder) {
		///在替换的时候不要使用 String.replaceAll("\\","\\\\"); 
		///这样不会达到替换的效果 因为这些符号有特殊的意义,在正则     ///表达式里要用到
		int i = 0;
		while (i < builder.length()) {
			if (builder.charAt(i) == '\\') {
				builder.insert(i, '\\');
				i += 2;
			} else {
				i++;
			}
		}
		return builder.toString().replaceAll("\r", "").replaceAll("\n", "");
	}

}
