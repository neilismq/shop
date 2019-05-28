/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: GS9KozV+xceuNxiMXLFjt3/6d9Z0w1xZ
 */
package net.shopxx.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopxx.dao.MessageConfigDao;
import net.shopxx.dao.WechatMessageTemplateDao;
import net.shopxx.entity.MessageConfig;
import net.shopxx.service.MessageConfigService;

/**
 * Service - 消息配置
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Service
public class MessageConfigServiceImpl extends BaseServiceImpl<MessageConfig, Long> implements MessageConfigService {

	@Inject
	private MessageConfigDao messageConfigDao;
	@Inject
	private WechatMessageTemplateDao wechatMessageTemplateDao;

	@Override
	@Transactional(readOnly = true)
	public MessageConfig find(MessageConfig.Type type) {
		return messageConfigDao.find("type", type);
	}

	@Override
	@Transactional
	public List<MessageConfig.Type> getActiveMessageConfigType(final boolean isSupportWechatMessage) {
		List<MessageConfig.Type> types = new ArrayList<>();
		for (MessageConfig.Type type : MessageConfig.Type.values()) {
			if (type.getIsSupportWechatMessage() == isSupportWechatMessage && !wechatMessageTemplateDao.messageConfigTypeExists(type)) {
				types.add(type);
			}
		}
		return types;
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public MessageConfig save(MessageConfig messageConfig) {
		return super.save(messageConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public MessageConfig update(MessageConfig messageConfig) {
		return super.update(messageConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public MessageConfig update(MessageConfig messageConfig, String... ignoreProperties) {
		return super.update(messageConfig, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(MessageConfig messageConfig) {
		super.delete(messageConfig);
	}

}