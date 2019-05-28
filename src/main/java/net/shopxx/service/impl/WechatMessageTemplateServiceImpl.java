/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: ZkLV4/AmderihmS0GT3OMy0ZK7P9hjUR
 */
package net.shopxx.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import freemarker.template.TemplateException;
import net.shopxx.dao.WechatMessageTemplateDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderRefunds;
import net.shopxx.entity.OrderShipping;
import net.shopxx.entity.WechatMessageTemplate;
import net.shopxx.entity.WechatMessageTemplateParameter;
import net.shopxx.service.WechatMessageTemplateService;
import net.shopxx.util.FreeMarkerUtils;

/**
 * Service - 微信消息模版
 *
 * @author SHOP++ Team
 * @version 6.1
 */
@Service
public class WechatMessageTemplateServiceImpl extends BaseServiceImpl<WechatMessageTemplate, Long> implements WechatMessageTemplateService {

	@Inject
	private WechatMessageTemplateDao wechatMessageTemplateDao;

	@Override
	@Transactional(readOnly = true)
	public WechatMessageTemplate findByTemplateId(String templateId) {
		return wechatMessageTemplateDao.find("templateId", StringUtils.lowerCase(templateId));
	}

	@Override
	public void filter(List<WechatMessageTemplateParameter> wechatMessageTemplateParameters) {
		CollectionUtils.filter(wechatMessageTemplateParameters, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				WechatMessageTemplateParameter wechatMessageTemplateParameter = (WechatMessageTemplateParameter) object;
				return wechatMessageTemplateParameter.getType() != null && StringUtils.isNotEmpty(wechatMessageTemplateParameter.getValue());
			}
		});
	}

	@Override
	public String resolveParameter(String value) {
		if (StringUtils.isEmpty(value)) {
			return StringUtils.EMPTY;
		}

		Pattern pattern = Pattern.compile("\\{\\{(?<parameter>\\w+)");
		Matcher matcher = pattern.matcher(value);
		return matcher.find() ? matcher.group("parameter") : StringUtils.EMPTY;
	}

	@Override
	public List<String> resolveParameters(String content) {
		List<String> result = new ArrayList<>();
		if (StringUtils.isEmpty(content)) {
			return result;
		}

		Pattern pattern = Pattern.compile("(?<parameter>\\{\\{\\w+.DATA\\}\\})", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			result.add(resolveParameter(matcher.group("parameter")));
		}
		return result;
	}

	@Override
	public Map<String, Object> resolveTemplateData(WechatMessageTemplate wechatMessageTemplate, Order order, Member member, OrderShipping orderShipping, OrderRefunds orderRefunds) {
		Map<String, Object> data = new HashMap<>();
		if (wechatMessageTemplate == null) {
			return data;
		}

		for (WechatMessageTemplateParameter wechatMessageTemplateParameter : wechatMessageTemplate.getWechatMessageTemplateParameters()) {
			String parameterName = wechatMessageTemplateParameter.getName();
			String parameterValue = wechatMessageTemplateParameter.getValue();
			WechatMessageTemplateParameter.Type parameterType = wechatMessageTemplateParameter.getType();
			Map<String, Object> valueMap = new HashMap<>();
			if (StringUtils.isNotEmpty(parameterName) && StringUtils.isNotEmpty(parameterValue)) {
				switch (parameterType) {
				case TEXT:
					valueMap.put("value", parameterValue);
					break;
				case SELECT:
					Map<String, String> contentMap = resolveContent(order, member, orderShipping, orderRefunds);
					String contentValue = contentMap.get(parameterValue);
					if (StringUtils.isNotEmpty(contentValue)) {
						valueMap.put("value", contentValue);
					}
					break;
				}
				data.put(parameterName, valueMap);
			}
		}
		return data;
	}

	@Override
	public Map<String, String> resolveContent(Order order, Member member, OrderShipping orderShipping, OrderRefunds orderRefunds) {
		Map<String, String> data = new HashMap<>();
		for (WechatMessageTemplate.OrderAttribute orderAttribute : WechatMessageTemplate.OrderAttribute.values()) {
			String tagName = orderAttribute.getTagName();
			String value = orderAttribute.getValue(order);
			if (StringUtils.equalsIgnoreCase(orderAttribute.getTagName(), WechatMessageTemplate.OrderAttribute.ORDER_STATUS.getTagName())) {
				try {
					data.put(tagName, FreeMarkerUtils.process(String.format("${message('Order.Status.%s')}", value)));
				} catch (IOException | TemplateException e) {
					throw new RuntimeException();
				}
			} else {
				data.put(tagName, value);
			}
		}
		for (WechatMessageTemplate.MemberAttribute memberAttribute : WechatMessageTemplate.MemberAttribute.values()) {
			data.put(memberAttribute.getTagName(), memberAttribute.getValue(member));
		}
		for (WechatMessageTemplate.OrderShippingAttribute orderShippingAttribute : WechatMessageTemplate.OrderShippingAttribute.values()) {
			data.put(orderShippingAttribute.getTagName(), orderShippingAttribute.getValue(orderShipping));
		}
		for (WechatMessageTemplate.OrderRefundsAttribute orderRefundsAttribute : WechatMessageTemplate.OrderRefundsAttribute.values()) {
			String tagName = orderRefundsAttribute.getTagName();
			String value = orderRefundsAttribute.getValue(orderRefunds);
			if (StringUtils.equalsIgnoreCase(orderRefundsAttribute.getTagName(), WechatMessageTemplate.OrderRefundsAttribute.ORDER_REFUNDS_METHOD.getTagName())) {
				try {
					data.put(tagName, FreeMarkerUtils.process(String.format("${message('OrderRefunds.Method.%s')}", value)));
				} catch (IOException | TemplateException e) {
					throw new RuntimeException();
				}
			} else {
				data.put(tagName, value);
			}
		}
		return data;
	}

}