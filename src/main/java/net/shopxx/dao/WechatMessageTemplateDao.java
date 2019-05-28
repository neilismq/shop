/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: eeMLxEd5WKM93SdQoMZ4trMSnRRQrO/F
 */
package net.shopxx.dao;

import net.shopxx.entity.MessageConfig;
import net.shopxx.entity.WechatMessageTemplate;

/**
 * Dao - 微信消息模版
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface WechatMessageTemplateDao extends BaseDao<WechatMessageTemplate, Long> {

	/**
	 * 根据消息配置类型判断微信消息模版是否存在
	 * 
	 * @param type
	 *            消息配置类型
	 * @return 微信消息模版是否存在
	 */
	boolean messageConfigTypeExists(MessageConfig.Type type);

}