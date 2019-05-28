/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: t4XG4Wz0rMOPq8s5nqS4nxuan2ZUQ5EF
 */
package net.shopxx.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.shopxx.plugin.PromotionPlugin;

/**
 * PromotionPlugin - 促销插件Provider
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface PromotionPluginProvider {

	/**
	 * 促销插件
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @return 促销插件
	 */
	PromotionPlugin promotionPlugin(HttpServletRequest request, HttpServletResponse response, Object handler);

}