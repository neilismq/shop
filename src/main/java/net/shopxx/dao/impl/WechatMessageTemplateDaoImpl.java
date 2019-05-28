/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: vIalsXCz250Q5otiqPWcXHW2K3atrwBl
 */
package net.shopxx.dao.impl;

import org.springframework.stereotype.Repository;

import net.shopxx.dao.WechatMessageTemplateDao;
import net.shopxx.entity.MessageConfig.Type;
import net.shopxx.entity.WechatMessageTemplate;

/**
 * Dao - 微信消息模版
 *
 * @author SHOP++ Team
 * @version 6.1
 */
@Repository
public class WechatMessageTemplateDaoImpl extends BaseDaoImpl<WechatMessageTemplate, Long> implements WechatMessageTemplateDao {

	@Override
	public boolean messageConfigTypeExists(Type type) {
		if (type == null) {
			return false;
		}
		String jpql = "select count(*) from WechatMessageTemplate wechatMessageTemplate where wechatMessageTemplate.messageConfig.type = :type";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("type", type).getSingleResult();
		return count > 0;
	}

}