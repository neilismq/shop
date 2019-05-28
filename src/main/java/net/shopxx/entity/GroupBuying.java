/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: lbErHqr90rQW3LH3KanOdOLainy/eeav
 */
package net.shopxx.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;

import net.shopxx.exception.ResourceNotFoundException;
import net.shopxx.plugin.GroupBuyingPromotionPlugin.GroupBuyingAttribute;

/**
 * Entity - 团购
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Entity
public class GroupBuying extends BaseEntity<Long> {

	private static final long serialVersionUID = 6511155336985661478L;

	/**
	 * "参团数"缓存名称
	 */
	public static final String PARTICIPANTS_CACHE_NAME = "participants";

	/**
	 * 有效订单状态
	 */
	public static final List<Order.Status> ACTIVE_ORDER_STATUSES = Arrays.asList(Order.Status.PENDING_REVIEW, Order.Status.PENDING_SHIPMENT, Order.Status.SHIPPED, Order.Status.RECEIVED, Order.Status.COMPLETED, Order.Status.FAILED);

	/**
	 * 状态
	 */
	public enum Status {

		/**
		 * 等待成团
		 */
		PENDING_GROUP_SUCCESS,

		/**
		 * 已成团
		 */
		GROUP_SUCCESSED

	}

	/**
	 * 状态
	 */
	@JsonView(BaseView.class)
	@Column(nullable = false)
	private GroupBuying.Status status;

	/**
	 * 起始日期
	 */
	@JsonView(BaseView.class)
	@NotNull
	private Date beginDate;

	/**
	 * 结束日期
	 */
	@JsonView(BaseView.class)
	@NotNull
	private Date endDate;

	/**
	 * 成团人数
	 */
	@Min(2)
	private Integer groupSize;

	/**
	 * 限购
	 */
	@Min(0)
	private Integer purchasingLimit;

	/**
	 * 团购属性
	 */
	@OneToOne(mappedBy = "groupBuying", fetch = FetchType.LAZY)
	private GroupBuyingAttribute groupBuyingAttribute;

	/**
	 * 订单
	 */
	@OneToMany(mappedBy = "groupBuying", fetch = FetchType.LAZY)
	private Set<Order> orders = new HashSet<>();

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public GroupBuying.Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(GroupBuying.Status status) {
		this.status = status;
	}

	/**
	 * 获取起始日期
	 * 
	 * @return 起始日期
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * 设置起始日期
	 * 
	 * @param beginDate
	 *            起始日期
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * 获取结束日期
	 * 
	 * @return 结束日期
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置结束日期
	 * 
	 * @param endDate
	 *            结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取成团人数
	 * 
	 * @return 成团人数
	 */
	public Integer getGroupSize() {
		return groupSize;
	}

	/**
	 * 设置成团人数
	 * 
	 * @param groupSize
	 *            成团人数
	 */
	public void setGroupSize(Integer groupSize) {
		this.groupSize = groupSize;
	}

	/**
	 * 获取限购
	 * 
	 * @return 限购
	 */
	public Integer getPurchasingLimit() {
		return purchasingLimit;
	}

	/**
	 * 设置限购
	 * 
	 * @param purchasingLimit
	 *            限购
	 */
	public void setPurchasingLimit(Integer purchasingLimit) {
		this.purchasingLimit = purchasingLimit;
	}

	/**
	 * 获取团购属性
	 * 
	 * @return 团购属性
	 */
	public GroupBuyingAttribute getGroupBuyingAttribute() {
		return groupBuyingAttribute;
	}

	/**
	 * 设置团购属性
	 * 
	 * @param groupBuyingAttribute
	 *            团购属性
	 */
	public void setGroupBuyingAttribute(GroupBuyingAttribute groupBuyingAttribute) {
		this.groupBuyingAttribute = groupBuyingAttribute;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * 
	 * @param orders
	 *            订单
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 获取促销
	 * 
	 * @return 促销
	 */
	@Transient
	public Promotion getPromotion() {
		if (getGroupBuyingAttribute() != null) {
			return getGroupBuyingAttribute().getPromotion();
		}

		return null;
	}

	/**
	 * 判断是否已结束
	 * 
	 * @return 是否已结束
	 */
	@JsonView(BaseView.class)
	@Transient
	public boolean hasEnded() {
		return getEndDate() != null && getEndDate().before(new Date());
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
		if (getGroupBuyingAttribute() == null) {
			return originalPrice;
		}

		return getGroupBuyingAttribute().getDiscounPrice(originalPrice);
	}

	/**
	 * 获取团购结束
	 * 
	 * @return 团购结束
	 */
	@JsonView(BaseView.class)
	@Transient
	public boolean isGroupEnded() {
		return hasEnded() && GroupBuying.Status.GROUP_SUCCESSED.equals(getStatus());
	}

	/**
	 * 获取成团失败
	 * 
	 * @return 成团失败
	 */
	@JsonView(BaseView.class)
	@Transient
	public boolean isGroupFailure() {
		return hasEnded() && GroupBuying.Status.PENDING_GROUP_SUCCESS.equals(getStatus());
	}

	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		setStatus(GroupBuying.Status.PENDING_GROUP_SUCCESS);
	}

	/**
	 * Inner - 团购购物车
	 * 
	 * @author SHOP++ Team
	 * @version 6.1
	 */
	public static class GroupBuyingCart extends Cart {

		private static final long serialVersionUID = -5692934142059482709L;

		/**
		 * 团购
		 */
		private final GroupBuying groupBuying;

		public GroupBuyingCart(GroupBuying groupBuying) {
			if (groupBuying == null) {
				throw new ResourceNotFoundException();
			}

			this.groupBuying = groupBuying;
		}

		@Override
		public GroupBuying getGroupBuying() {
			return this.groupBuying;
		}

		@Override
		public GroupBuying getGroupBuying(Store store) {
			return this.groupBuying.getPromotion().getStore().equals(store) ? this.groupBuying : null;
		}

	}

}