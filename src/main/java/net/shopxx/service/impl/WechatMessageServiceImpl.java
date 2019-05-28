/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: xc2pSz33CRr3n6s8MX6eTDiZacPMyrYB
 */
package net.shopxx.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopxx.Setting;
import net.shopxx.entity.Member;
import net.shopxx.entity.MessageConfig;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderRefunds;
import net.shopxx.entity.OrderShipping;
import net.shopxx.entity.SocialUser;
import net.shopxx.entity.WechatMessageTemplate;
import net.shopxx.plugin.LoginPlugin;
import net.shopxx.service.MessageConfigService;
import net.shopxx.service.PluginService;
import net.shopxx.service.SocialUserService;
import net.shopxx.service.WechatMessageService;
import net.shopxx.service.WechatMessageTemplateService;
import net.shopxx.util.JsonUtils;
import net.shopxx.util.SystemUtils;
import net.shopxx.util.WebUtils;

/**
 * Service - 微信消息
 *
 * @author SHOP++ Team
 * @version 6.1
 */
@Service
public class WechatMessageServiceImpl implements WechatMessageService {

	/**
	 * "微信access_token"缓存名称
	 */
	private static final String WECHAT_ACCESS_TOKEN_CACHE_NAME = "wechatAccessToken";

	/**
	 * "element_key"名称
	 */
	private static final String ELEMENT_KEY_NAME = "accessToken";

	@Inject
	private TaskExecutor taskExecutor;
	@Inject
	private CacheManager cacheManager;
	@Inject
	private WechatMessageTemplateService wechatMessageTemplateService;
	@Inject
	private MessageConfigService messageConfigService;
	@Inject
	private SocialUserService socialUserService;
	@Inject
	private PluginService pluginService;

	/**
	 * 添加模版消息发送任务
	 *
	 * @param openId
	 *            接收者openid
	 * @param templateId
	 *            模板ID
	 * @param url
	 *            模板跳转链接
	 * @param data
	 *            模版数据
	 * @param pagepath
	 *            链接小程序的页面路径，支持带参数
	 * @param color
	 *            模板内容字体颜色，不填默认为黑色
	 */
	private void addSendTask(final String openId, final String templateId, final String url, final Map<String, Object> data, final String pagepath, final String color) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				send(openId, templateId, url, data, pagepath, color);
			}
		});
	}

	/**
	 * 模版消息发送
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
	 */
	public void send(String openId, String templateId, String url, Map<String, Object> data, String pagepath, String color) {
		Assert.hasText(openId, "[Assertion failed] - openId must have text; it must not be null, empty, or blank");
		Assert.hasText(templateId, "[Assertion failed] - templateId must have text; it must not be null, empty, or blank");
		Assert.notEmpty(data, "'data' must contain entries");

		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("touser", openId);
		parameterMap.put("template_id", templateId);
		if (StringUtils.isNotEmpty(url)) {
			parameterMap.put("url", url);
		}
		if (StringUtils.isNotEmpty(pagepath)) {
			Setting setting = SystemUtils.getSetting();
			Map<String, Object> miniProgramMap = new TreeMap<>();
			miniProgramMap.put("appid", setting.getMiniProgramAppId());
			miniProgramMap.put("pagepath", pagepath);
			parameterMap.put("miniprogram", miniProgramMap);
		}
		parameterMap.put("data", data);
		if (StringUtils.isNotEmpty(color)) {
			parameterMap.put("color", color);
		}
		String accessToken = getAccessToken();
		if (StringUtils.isEmpty(accessToken)) {
			return;
		}
		WebUtils.post(String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken), JsonUtils.toJson(parameterMap));
	}

	@Override
	public String getAccessToken() {
		Setting setting = SystemUtils.getSetting();
		String serviceAccountAppId = setting.getServiceAccountAppId();
		String serviceAccountAppSecret = setting.getServiceAccountAppSecret();
		if (StringUtils.isEmpty(serviceAccountAppId) || StringUtils.isEmpty(serviceAccountAppSecret)) {
			return StringUtils.EMPTY;
		}

		Ehcache cache = cacheManager.getEhcache(WECHAT_ACCESS_TOKEN_CACHE_NAME);
		Element element;
		if (cache != null && cache.get(ELEMENT_KEY_NAME) != null) {
			element = cache.get(ELEMENT_KEY_NAME);
			String elementValue = String.valueOf(element.getObjectValue());
			if (StringUtils.isNotEmpty(elementValue)) {
				Map<String, String> result = JsonUtils.toObject(elementValue, new TypeReference<Map<String, String>>() {
				});
				String appId = result.get("appid");
				String secret = result.get("secret");
				String accessToken = result.get("access_token");
				if (StringUtils.equalsIgnoreCase(appId, serviceAccountAppId) && StringUtils.equalsIgnoreCase(secret, serviceAccountAppSecret) && StringUtils.isNotEmpty(accessToken)) {
					return accessToken;
				}
			}
		}

		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("grant_type", "client_credential");
		parameterMap.put("appid", serviceAccountAppId);
		parameterMap.put("secret", serviceAccountAppSecret);
		String result = WebUtils.get("https://api.weixin.qq.com/cgi-bin/token", parameterMap);
		if (StringUtils.isEmpty(result)) {
			return StringUtils.EMPTY;
		}
		Map<String, String> resultMap = JsonUtils.toObject(result, new TypeReference<Map<String, String>>() {
		});
		String accessToken = resultMap.get("access_token");
		String expiresIn = resultMap.get("expires_in");
		if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(expiresIn)) {
			return StringUtils.EMPTY;
		}
		Map<String, Object> elementValueMap = new TreeMap<>();
		elementValueMap.put("appid", serviceAccountAppId);
		elementValueMap.put("secret", serviceAccountAppSecret);
		elementValueMap.put("access_token", accessToken);
		element = new Element(ELEMENT_KEY_NAME, JsonUtils.toJson(elementValueMap), Integer.parseInt(expiresIn), Integer.parseInt(expiresIn));
		cache.put(element);
		return accessToken;
	}

	@Override
	public List<Map<String, String>> getTemplateList() {
		List<Map<String, String>> maps = new ArrayList<>();
		try {
			String result = WebUtils.get(String.format("https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s", getAccessToken()), null);
			Map<String, List<Map<String, String>>> resultMap = JsonUtils.toObject(result, new TypeReference<Map<String, List<Map<String, String>>>>() {
			});
			if (CollectionUtils.isNotEmpty(resultMap.get("template_list"))) {
				maps = resultMap.get("template_list");
			}
			return maps;
		} catch (Exception e) {
			return maps;
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, String> getTemplateByTemplateId(final String templateId) {
		return (Map<String, String>) CollectionUtils.find(getTemplateList(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Map<String, String> map = (Map<String, String>) object;
				return MapUtils.isNotEmpty(map) && StringUtils.equalsIgnoreCase(map.get("template_id"), templateId);
			}
		});
	}

	@Override
	public void send(String openId, String templateId, String url, Map<String, Object> data, String pagepath, String color, boolean async) {
		Assert.hasText(openId, "[Assertion failed] - openId must have text; it must not be null, empty, or blank");
		Assert.hasText(templateId, "[Assertion failed] - templateId must have text; it must not be null, empty, or blank");
		Assert.notEmpty(data, "'data' must contain entries");

		if (async) {
			addSendTask(openId, templateId, url, data, pagepath, color);
		} else {
			send(openId, templateId, url, data, pagepath, color);
		}
	}

	@Override
	public void sendWechatMessage(MessageConfig.Type type, Order order, Member member, OrderShipping orderShipping, OrderRefunds orderRefunds, String pagepath) {
		LoginPlugin loginPlugin = pluginService.getSupportWechatMessageLoginPlugin();
		SocialUser socialUser = socialUserService.getSocialUser(order.getMember().getSocialUsers(), loginPlugin != null ? loginPlugin.getId() : null);
		if (socialUser == null || StringUtils.isEmpty(socialUser.getUniqueId())) {
			return;
		}
		MessageConfig messageConfig = messageConfigService.find(type);
		if (messageConfig == null || !messageConfig.getIsWechatMessageEnabled()) {
			return;
		}
		WechatMessageTemplate wechatMessageTemplate = messageConfig.getWechatMessageTemplate();
		if (wechatMessageTemplate == null || StringUtils.isEmpty(wechatMessageTemplate.getTemplateId())) {
			return;
		}
		Map<String, Object> data = wechatMessageTemplateService.resolveTemplateData(wechatMessageTemplate, order, member, orderShipping, orderRefunds);
		if (MapUtils.isEmpty(data)) {
			return;
		}

		send(socialUser.getUniqueId(), wechatMessageTemplate.getTemplateId(), null, data, pagepath, null, true);
	}

}