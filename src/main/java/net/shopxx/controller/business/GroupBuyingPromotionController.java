/*

 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: IyyVahLk3rm8rtory8ySThLdo3w7Ixc5
 */
package net.shopxx.controller.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopxx.Pageable;
import net.shopxx.Results;
import net.shopxx.entity.GroupBuying;
import net.shopxx.entity.Product;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.Store;
import net.shopxx.exception.UnauthorizedException;
import net.shopxx.plugin.GroupBuyingPromotionPlugin;
import net.shopxx.plugin.PromotionPlugin;
import net.shopxx.security.CurrentStore;
import net.shopxx.service.GroupBuyingService;
import net.shopxx.service.MemberRankService;
import net.shopxx.service.PluginService;
import net.shopxx.service.ProductService;
import net.shopxx.service.PromotionDefaultAttributeService;
import net.shopxx.service.PromotionService;

/**
 * Controller - 团购促销
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Controller("businessGroupBuyingPromotionController")
@RequestMapping("/business/group_buying_promotion")
public class GroupBuyingPromotionController extends BaseController {

	@Inject
	private PromotionService promotionService;
	@Inject
	private ProductService productService;
	@Inject
	private GroupBuyingService groupBuyingService;
	@Inject
	private MemberRankService memberRankService;
	@Inject
	private PromotionDefaultAttributeService promotionDefaultAttributeService;
	@Inject
	private PluginService pluginService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long promotionId, Long groupBuyingAttributeId, Long productId, @CurrentStore Store currentStore, ModelMap model) {
		Promotion promotion = promotionService.find(promotionId);
		if (promotion != null && !currentStore.equals(promotion.getStore())) {
			throw new UnauthorizedException();
		}
		GroupBuyingPromotionPlugin.GroupBuyingAttribute groupBuyingAttribute = (GroupBuyingPromotionPlugin.GroupBuyingAttribute) promotionDefaultAttributeService.find(groupBuyingAttributeId);
		if (groupBuyingAttribute != null && !groupBuyingAttribute.equals(promotion.getPromotionDefaultAttribute())) {
			throw new UnauthorizedException();
		}
		Product product = productService.find(productId);
		if (product != null && !currentStore.equals(product.getStore())) {
			throw new UnauthorizedException();
		}

		model.addAttribute("promotion", promotion);
		model.addAttribute("groupBuyingAttribute", groupBuyingAttribute);
		model.addAttribute("product", product);
	}

	/**
	 * 商品选择
	 */
	@GetMapping("/product_select")
	public @ResponseBody List<Map<String, Object>> productSelect(String keyword, @CurrentStore Store currentStore) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (StringUtils.isEmpty(keyword)) {
			return data;
		}

		List<Product> products = productService.search(keyword, Product.Type.GENERAL, null, currentStore, null, null, null, null, null, new Pageable()).getContent();
		for (Product product : products) {
			Map<String, Object> item = new HashMap<>();
			item.put("id", product.getId());
			item.put("name", product.getName());
			data.add(item);
		}

		return data;
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(String promotionPluginId, @CurrentStore Store currentStore, ModelMap model) {
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("promotionPluginId", promotionPluginId);
		model.addAttribute("discountTypes", GroupBuyingPromotionPlugin.GroupBuyingAttribute.DiscountType.values());
		return "business/group_buying_promotion/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(@ModelAttribute("promotionForm") Promotion promotionForm, @ModelAttribute("groupBuyingAttributeForm") GroupBuyingPromotionPlugin.GroupBuyingAttribute groupBuyingAttributeForm, @ModelAttribute(binding = false) Product product,
			@ModelAttribute("groupBuyingForm") GroupBuying groupBuyingForm, Long[] memberRankIds, @CurrentStore Store currentStore) {
		if (product == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (!isValid(groupBuyingForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		GroupBuying groupBuying = groupBuyingService.save(groupBuyingForm);
		groupBuyingAttributeForm.setMinPrice(null);
		groupBuyingAttributeForm.setMaxPrice(null);
		groupBuyingAttributeForm.setMinQuantity(null);
		groupBuyingAttributeForm.setMaxQuantity(null);
		groupBuyingAttributeForm.setGroupBuying(groupBuying);
		promotionForm.setStore(currentStore);
		promotionForm.setMemberRanks(new HashSet<>(memberRankService.findList(memberRankIds)));
		if (!isValid(promotionForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (!isValid(groupBuyingAttributeForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (promotionForm.getBeginDate() == null || promotionForm.getEndDate() == null || promotionForm.getBeginDate().after(promotionForm.getEndDate())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (promotionService.exists("groupBuyingPromotionPlugin", product, null, promotionForm.getBeginDate(), promotionForm.getEndDate())) {
			return Results.unprocessableEntity("business.groupBuyingPromotionPlugin.productInTheSamePeriodNotAllowMultipleGroupBuying");
		}
		promotionForm.setProducts(Collections.singleton(product));
		promotionForm.setProductCategories(null);
		promotionForm.setPromotionDefaultAttribute(null);
		promotionService.create(promotionForm, groupBuyingAttributeForm);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@ModelAttribute(binding = false) Promotion promotion, @CurrentStore Store currentStore, ModelMap model) {
		if (promotion == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		model.addAttribute("promotion", promotion);
		model.addAttribute("memberRanks", memberRankService.findAll());
		GroupBuyingPromotionPlugin.GroupBuyingAttribute groupBuyingAttribute = (GroupBuyingPromotionPlugin.GroupBuyingAttribute) promotion.getPromotionDefaultAttribute();
		if (groupBuyingAttribute == null || !promotion.getPromotionDefaultAttribute().equals(groupBuyingAttribute)) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		model.addAttribute("groupBuyingAttribute", groupBuyingAttribute);
		model.addAttribute("discountTypes", GroupBuyingPromotionPlugin.GroupBuyingAttribute.DiscountType.values());
		return "business/group_buying_promotion/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(@ModelAttribute("promotionForm") Promotion promotionForm, @ModelAttribute(binding = false) Promotion promotion, @ModelAttribute("groupBuyingAttributeForm") GroupBuyingPromotionPlugin.GroupBuyingAttribute groupBuyingAttributeForm,
			@ModelAttribute(binding = false) GroupBuyingPromotionPlugin.GroupBuyingAttribute groupBuyingAttribute, @ModelAttribute(binding = false) Product product, @ModelAttribute("groupBuyingForm") GroupBuying groupBuyingForm, Long[] memberRankIds, Long groupBuyingId) {
		if (promotion == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (groupBuyingAttribute == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		GroupBuying groupBuying = groupBuyingService.find(groupBuyingId);
		if (groupBuying == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		promotionForm.setMemberRanks(new HashSet<>(memberRankService.findList(memberRankIds)));
		promotionForm.setId(promotion.getId());
		promotionForm.setBeginDate(promotion.getBeginDate());
		promotionForm.setEndDate(promotion.getEndDate());
		promotionForm.setProducts(promotion.getProducts());
		groupBuyingAttributeForm.setId(groupBuyingAttribute.getId());
		groupBuyingAttributeForm.setMinPrice(null);
		groupBuyingAttributeForm.setMaxPrice(null);
		groupBuyingAttributeForm.setMinQuantity(null);
		groupBuyingAttributeForm.setMaxQuantity(null);
		groupBuyingAttributeForm.setDiscountType(groupBuyingAttribute.getDiscountType());
		groupBuyingAttributeForm.setDiscounValue(groupBuyingAttribute.getDiscounValue());
		if (!isValid(groupBuyingAttributeForm)) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		BeanUtils.copyProperties(groupBuyingForm, groupBuying, "id", "status", "beginDate", "endDate", "groupSize", "groupBuyingAttribute", "orders");
		groupBuyingAttributeForm.setGroupBuying(groupBuyingService.update(groupBuying));
		promotionService.modify(promotionForm, groupBuyingAttributeForm);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(String promotionPluginId, Pageable pageable, @CurrentStore Store currentStore, ModelMap model) {
		PromotionPlugin promotionPlugin = pluginService.getPromotionPlugin(promotionPluginId);
		model.addAttribute("promotionPlugin", promotionPlugin);
		model.addAttribute("page", promotionService.findPage(promotionPlugin, currentStore, pageable));
		return "business/group_buying_promotion/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids, @CurrentStore Store currentStore) {
		for (Long id : ids) {
			Promotion promotion = promotionService.find(id);
			if (promotion == null) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			if (!currentStore.equals(promotion.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		promotionService.delete(ids);
		return Results.OK;
	}

	/**
	 * 结束
	 */
	@PostMapping("/end")
	public ResponseEntity<?> end(Long promotionId, @CurrentStore Store currentStore) {
		Promotion promotion = promotionService.find(promotionId);
		if (promotion == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (promotion.hasEnded()) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (!currentStore.equals(promotion.getStore())) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		groupBuyingService.end(promotion);
		return Results.OK;
	}

}