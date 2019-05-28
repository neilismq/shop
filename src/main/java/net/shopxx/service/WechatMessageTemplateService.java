/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: Jx5QyVmECT9fgQjgmWdTarX1b5dECCWZ
 */
package net.shopxx.service;

import java.util.List;
import java.util.Map;

import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderRefunds;
import net.shopxx.entity.OrderShipping;
import net.shopxx.entity.WechatMessageTemplate;
import net.shopxx.entity.WechatMessageTemplateParameter;

/**
 * Service - 微信消息模版
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface WechatMessageTemplateService extends BaseService<WechatMessageTemplate, Long> {

	/**
	 * 根据模版ID查找微信消息模版
	 *
	 * @param templateId
	 *            模版ID(忽略大小写)
	 * @return 微信消息模版，若不存在则返回null
	 */
	WechatMessageTemplate findByTemplateId(String templateId);

	/**
	 * 微信消息模版参数过滤
	 * 
	 * @param wechatMessageTemplateParameters
	 *            微信消息模版参数
	 */
	void filter(List<WechatMessageTemplateParameter> wechatMessageTemplateParameters);

	/**
	 * 解析参数名
	 * 
	 * @param value
	 *            值
	 * @return 微信模版消息中关键字.DATA前面的变量名
	 */
	String resolveParameter(String value);

	/**
	 * 解析参数名
	 * 
	 * @param content
	 *            内容
	 * @return 微信模版消息content中所有的关键字.DATA前面的变量名
	 */
	List<String> resolveParameters(String content);

	/**
	 * 解析内容
	 * 
	 * @param order
	 *            订单
	 * @param member
	 *            会员
	 * @param orderShipping
	 *            订单发货
	 * @param orderRefunds
	 *            订单退款
	 * @return 内容
	 */
	Map<String, String> resolveContent(Order order, Member member, OrderShipping orderShipping, OrderRefunds orderRefunds);

	/**
	 * 解析模版数据
	 * 
	 * @param wechatMessageTemplate
	 *            微信消息模版
	 * @param order
	 *            订单
	 * @param member
	 *            会员
	 * @param orderShipping
	 *            订单发货
	 * @param orderRefunds
	 *            订单退款
	 * @return 模版数据
	 */
	Map<String, Object> resolveTemplateData(WechatMessageTemplate wechatMessageTemplate, Order order, Member member, OrderShipping orderShipping, OrderRefunds orderRefunds);

}