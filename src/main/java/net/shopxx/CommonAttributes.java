/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: DwBvmAPoD5c79flZTryM/JnuWHRkRNxA
 */
package net.shopxx;

/**
 * 公共参数
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public final class CommonAttributes {

	/**
	 * "微信小程序"Header参数名称
	 */
	public static final String WEIXIN_MINI_PROGARM_HEADER_PARAMETER_NAME = "X-Weixin-Mini-Progarm";

	/**
	 * 日期格式配比
	 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/**
	 * shopxx.xml文件路径
	 */
	public static final String SHOPXX_XML_PATH = "classpath:shopxx.xml";

	/**
	 * shopxx.properties文件路径
	 */
	public static final String SHOPXX_PROPERTIES_PATH = "classpath:shopxx.properties";

	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}

}