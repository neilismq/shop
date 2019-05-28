/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: sVBGimHc3SyL3dmJA9LlhJAkojSPAzjp
 */
package net.shopxx.service;

import java.util.List;

import net.shopxx.entity.MessageConfig;

/**
 * Service - 消息配置
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface MessageConfigService extends BaseService<MessageConfig, Long> {

	/**
	 * 查找消息配置
	 * 
	 * @param type
	 *            类型
	 * @return 消息配置
	 */
	MessageConfig find(MessageConfig.Type type);

	/**
	 * 获取有效的消息配置类型
	 * 
	 * @param isSupportWechatMessage
	 *            是否支持微信消息
	 * @return 有效的消息配置类型
	 */
	List<MessageConfig.Type> getActiveMessageConfigType(boolean isSupportWechatMessage);

}