/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: askwRhLhbr2WuH6Qc+sCYpbg97Akx4rX
 */
package net.shopxx.service;

import java.util.List;
import java.util.Map;

import net.shopxx.entity.Member;
import net.shopxx.entity.MessageConfig;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderRefunds;
import net.shopxx.entity.OrderShipping;

/**
 * Service - 微信消息
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface WechatMessageService {

	/**
	 * 获取微信AccessToken
	 * 
	 * @return 微信AccessToken
	 */
	String getAccessToken();

	/**
	 * 获取模版列表
	 * 
	 * @return 模版列表，若不存在则返回空集合
	 */
	List<Map<String, String>> getTemplateList();

	/**
	 * 根据模版ID获取模版
	 * 
	 * @param templateId
	 *            模版ID
	 * @return 模版
	 */
	Map<String, String> getTemplateByTemplateId(String templateId);

	/**
	 * 微信模版消息发送
	 * 
	 * @param openId
	 *            接收者openid
	 * @param templateId
	 *            模板ID
	 * @param url
	 *            模板跳转链接
	 * @param pagepath
	 *            链接小程序的页面路径，支持带参数
	 * @param data
	 *            模版数据
	 * @param color
	 *            模板内容字体颜色，不填默认为黑色
	 * @param async
	 *            是否异步
	 */
	void send(String openId, String templateId, String url, Map<String, Object> data, String pagepath, String color, boolean async);

	/**
	 * 微信模版消息发送(异步)
	 * 
	 * @param type
	 *            消息配置类型
	 * @param order
	 *            订单
	 * @param member
	 *            会员
	 * @param orderShipping
	 *            订单发货
	 * @param orderRefunds
	 *            订单退款
	 * @param pagepath
	 *            链接小程序的页面路径，支持带参数
	 */
	void sendWechatMessage(MessageConfig.Type type, Order order, Member member, OrderShipping orderShipping, OrderRefunds orderRefunds, String pagepath);

}