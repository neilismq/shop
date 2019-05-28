/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: HtPbPagjfsxqy5re9ZmBldBSezrUkGCj
 */
package net.shopxx.plugin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.shopxx.entity.Cart;
import net.shopxx.entity.GroupBuying;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.PromotionDefaultAttribute;

/**
 * Plugin - 团购
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Component("groupBuyingPromotionPlugin")
public class GroupBuyingPromotionPlugin extends PromotionPlugin {

	/**
	 * 固定价格表达式
	 */
	public static final String FIX_PRICE_EXPRESSION = "price-((price/quantity-%s)*quantity)";

	@Override
	public String getName() {
		return "团购";
	}

	@Override
	public String getInstallUrl() {
		return "/admin/plugin/group_buying_promotion/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/group_buying_promotion/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/group_buying_promotion/setting";
	}

	@Override
	public String getListUrl() {
		return "/business/group_buying_promotion/list";
	}

	@Override
	public String getAddUrl() {
		return "/business/group_buying_promotion/add";
	}

	@Override
	public String getEditUrl() {
		return "/business/group_buying_promotion/edit";
	}

	@Override
	public boolean isConditionPassing(Cart cart, Promotion promotion, BigDecimal price, int quantity) {
		GroupBuyingAttribute groupBuyingAttribute = (GroupBuyingAttribute) promotion.getPromotionDefaultAttribute();
		return super.isConditionPassing(cart, promotion, price, quantity) && cart != null && cart.getGroupBuying(promotion.getStore()) != null && groupBuyingAttribute != null && cart.getGroupBuying(promotion.getStore()) == groupBuyingAttribute.getGroupBuying();
	}

	@Override
	public BigDecimal computeAdjustmentValue(Promotion promotion, BigDecimal price, int quantity) {
		GroupBuyingAttribute groupBuyingAttribute = (GroupBuyingAttribute) promotion.getPromotionDefaultAttribute();
		return groupBuyingAttribute.getDiscounPrice(price, quantity);
	}

	/**
	 * 团购属性
	 * 
	 * @author SHOP++ Team
	 * @version 6.1
	 */
	@Entity(name = "GroupBuyingAttribute")
	public static class GroupBuyingAttribute extends PromotionDefaultAttribute {

		private static final long serialVersionUID = 5606875479526482333L;

		/**
		 * 减免类型
		 */
		public enum DiscountType {

			/**
			 * 固定价格
			 */
			FIX_PRICE,

			/**
			 * 百分比减免
			 */
			PERCENT_OFF

		}

		/**
		 * 减免类型
		 */
		@NotNull
		private GroupBuyingAttribute.DiscountType discountType;

		/**
		 * 折扣值
		 */
		@NotNull
		@Min(0)
		@Digits(integer = 12, fraction = 3)
		private BigDecimal discounValue;

		/**
		 * 团购
		 */
		@OneToOne(fetch = FetchType.LAZY)
		private GroupBuying groupBuying;

		/**
		 * 获取减免类型
		 * 
		 * @return 减免类型
		 */
		public GroupBuyingAttribute.DiscountType getDiscountType() {
			return discountType;
		}

		/**
		 * 设置减免类型
		 * 
		 * @param discountType
		 *            减免类型
		 */
		public void setDiscountType(GroupBuyingAttribute.DiscountType discountType) {
			this.discountType = discountType;
		}

		/**
		 * 获取折扣值
		 * 
		 * @return 折扣值
		 */
		public BigDecimal getDiscounValue() {
			return discounValue;
		}

		/**
		 * 设置折扣值
		 * 
		 * @param discounValue
		 *            折扣值
		 */
		public void setDiscounValue(BigDecimal discounValue) {
			this.discounValue = discounValue;
		}

		/**
		 * 获取团购
		 * 
		 * @return 团购
		 */
		public GroupBuying getGroupBuying() {
			return groupBuying;
		}

		/**
		 * 设置团购
		 * 
		 * @param groupBuying
		 *            团购
		 */
		public void setGroupBuying(GroupBuying groupBuying) {
			this.groupBuying = groupBuying;
		}

		/**
		 * 获取折扣价格
		 * 
		 * @param originalPrice
		 *            原价
		 * @return 折扣价格
		 */
		@Transient
		public BigDecimal getDiscounPrice(BigDecimal originalPrice) {
			return getDiscounPrice(originalPrice, 1);
		}

		/**
		 * 获取折扣价格
		 * 
		 * @param originalPrice
		 *            原价
		 * @param quantity
		 *            数量
		 * @return 折扣价格
		 */
		@Transient
		public BigDecimal getDiscounPrice(BigDecimal originalPrice, int quantity) {
			if (originalPrice == null || quantity <= 0) {
				return null;
			}

			BigDecimal result = BigDecimal.ZERO;
			switch (getDiscountType()) {
			case FIX_PRICE:
				try {
					Binding binding = new Binding();
					binding.setVariable("price", originalPrice);
					binding.setVariable("quantity", quantity);
					GroovyShell groovyShell = new GroovyShell(binding);
					result = new BigDecimal(String.valueOf(groovyShell.evaluate(String.format(GroupBuyingPromotionPlugin.FIX_PRICE_EXPRESSION, discounValue))), MathContext.DECIMAL32);
				} catch (Exception e) {
					return originalPrice;
				}
				break;
			case PERCENT_OFF:
				result = getDiscounValue().multiply(originalPrice);
				break;
			}

			return result.compareTo(BigDecimal.ZERO) > 0 ? result.compareTo(originalPrice) > 0 ? originalPrice : result : BigDecimal.ZERO;
		}

		/**
		 * 删除前处理
		 */
		@PreRemove
		public void preRemove() {
			GroupBuying groupBuying = getGroupBuying();
			if (groupBuying != null) {
				groupBuying.setGroupBuyingAttribute(null);
				groupBuying.setEndDate(new Date());
			}
		}
	}

}