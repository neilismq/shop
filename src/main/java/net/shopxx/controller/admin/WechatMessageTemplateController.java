/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: GMXac5EMoDJzP+lxxWleeRnd7sT/v1Hf
 */
package net.shopxx.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.shopxx.Pageable;
import net.shopxx.Results;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.MessageConfig;
import net.shopxx.entity.WechatMessageTemplate;
import net.shopxx.entity.WechatMessageTemplateParameter;
import net.shopxx.service.MessageConfigService;
import net.shopxx.service.WechatMessageService;
import net.shopxx.service.WechatMessageTemplateService;

/**
 * Controller - 微信消息模版
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Controller("adminWechatMessageTemplateController")
@RequestMapping("/admin/wechat_message_template")
public class WechatMessageTemplateController extends BaseController {

	@Inject
	private WechatMessageService wechatMessageService;
	@Inject
	private MessageConfigService messageConfigService;
	@Inject
	private WechatMessageTemplateService wechatMessageTemplateService;

	/**
	 * 加载模版信息
	 */
	@GetMapping("/load_template_information")
	public ResponseEntity<?> loadTemplateInformation(Long id, String templateId) {
		WechatMessageTemplate wechatMessageTemplate = wechatMessageTemplateService.find(id);
		if (id != null) {
			if (wechatMessageTemplate == null) {
				return Results.unprocessableEntity("admin.wechatMessageTemplate.loadError");
			}
			Map<String, String> templateMap = wechatMessageService.getTemplateByTemplateId(wechatMessageTemplate.getTemplateId());
			if (MapUtils.isEmpty(templateMap)) {
				return Results.unprocessableEntity("admin.wechatMessageTemplate.loadWechatTemplateDataException");
			}
		}
		Map<String, Object> data = new HashMap<>();
		Map<String, String> contentMap = wechatMessageService.getTemplateByTemplateId(templateId);
		if (MapUtils.isEmpty(contentMap) || StringUtils.isEmpty(contentMap.get("content"))) {
			return Results.unprocessableEntity("admin.wechatMessageTemplate.loadWechatTemplateDataException");
		}

		List<Map<String, Object>> templateParameters = new ArrayList<>();
		List<String> parameters = wechatMessageTemplateService.resolveParameters(contentMap.get("content"));
		for (String parameter : parameters) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", parameter);
			if (wechatMessageTemplate != null) {
				map.put("value", wechatMessageTemplate.getWechatMessageTemplateParameterValue(parameter));
				map.put("type", wechatMessageTemplate.getWechatMessageTemplateParameterType(parameter));
			}
			templateParameters.add(map);
		}
		data.put("templateParameters", templateParameters);
		data.put("templateContent", contentMap.get("content"));
		data.put("templateExample", contentMap.get("example"));
		return ResponseEntity.ok(data);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("types", messageConfigService.getActiveMessageConfigType(true));
		model.addAttribute("memberAttributes", WechatMessageTemplate.MemberAttribute.values());
		model.addAttribute("orderShippingAttributes", WechatMessageTemplate.OrderShippingAttribute.values());
		model.addAttribute("orderAttributes", WechatMessageTemplate.OrderAttribute.values());
		model.addAttribute("orderRefundsAttributes", WechatMessageTemplate.OrderRefundsAttribute.values());
		model.addAttribute("wechatMessageTemplateParameterTypes", WechatMessageTemplateParameter.Type.values());
		model.addAttribute("templates", wechatMessageService.getTemplateList());
		return "admin/wechat_message_template/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(WechatMessageTemplate wechatMessageTemplate, MessageConfig.Type type) {
		MessageConfig messageConfig = messageConfigService.find(type);
		wechatMessageTemplateService.filter(wechatMessageTemplate.getWechatMessageTemplateParameters());
		if (messageConfig == null || messageConfig.getWechatMessageTemplate() != null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		wechatMessageTemplate.setMessageConfig(messageConfig);
		if (!isValid(wechatMessageTemplate, BaseEntity.Save.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		wechatMessageTemplateService.save(wechatMessageTemplate);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("templates", wechatMessageService.getTemplateList());
		model.addAttribute("wechatMessageTemplate", wechatMessageTemplateService.find(id));
		model.addAttribute("memberAttributes", WechatMessageTemplate.MemberAttribute.values());
		model.addAttribute("orderShippingAttributes", WechatMessageTemplate.OrderShippingAttribute.values());
		model.addAttribute("orderAttributes", WechatMessageTemplate.OrderAttribute.values());
		model.addAttribute("orderRefundsAttributes", WechatMessageTemplate.OrderRefundsAttribute.values());
		model.addAttribute("wechatMessageTemplateParameterTypes", WechatMessageTemplateParameter.Type.values());
		return "admin/wechat_message_template/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(WechatMessageTemplate wechatMessageTemplate) {
		wechatMessageTemplateService.filter(wechatMessageTemplate.getWechatMessageTemplateParameters());
		if (!isValid(wechatMessageTemplate)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		wechatMessageTemplateService.update(wechatMessageTemplate, "messageConfig");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", wechatMessageTemplateService.findPage(pageable));
		return "admin/wechat_message_template/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		wechatMessageTemplateService.delete(ids);
		return Results.OK;
	}

}