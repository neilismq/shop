/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: HAjv+EhWynmwcMiVNMsqnAWJbm8eDZL+
 */
package net.shopxx.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * Entity - 消息配置
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Entity
public class MessageConfig extends BaseEntity<Long> {

	private static final long serialVersionUID = -5214678967755261831L;

	/**
	 * 类型
	 */
	public enum Type {

		/**
		 * 会员注册
		 */
		REGISTER_MEMBER(false),

		/**
		 * 订单创建
		 */
		CREATE_ORDER(true),

		/**
		 * 订单更新
		 */
		UPDATE_ORDER(true),

		/**
		 * 订单取消
		 */
		CANCEL_ORDER(true),

		/**
		 * 订单审核
		 */
		REVIEW_ORDER(true),

		/**
		 * 订单收款
		 */
		PAYMENT_ORDER(false),

		/**
		 * 订单退款
		 */
		REFUNDS_ORDER(true),

		/**
		 * 订单发货
		 */
		SHIPPING_ORDER(true),

		/**
		 * 订单退货
		 */
		RETURNS_ORDER(true),

		/**
		 * 订单收货
		 */
		RECEIVE_ORDER(true),

		/**
		 * 订单完成
		 */
		COMPLETE_ORDER(true),

		/**
		 * 订单失败
		 */
		FAIL_ORDER(true),

		/**
		 * 商家注册
		 */
		REGISTER_BUSINESS(false),

		/**
		 * 店铺审核成功
		 */
		APPROVAL_STORE(false),

		/**
		 * 店铺审核失败
		 */
		FAIL_STORE(false);

		/**
		 * 支持微信消息
		 */
		private boolean isSupportWechatMessage;

		/**
		 * 构造方法
		 * 
		 * @param isSupportWechatMessage
		 *            支持微信消息
		 */
		Type(boolean isSupportWechatMessage) {
			this.isSupportWechatMessage = isSupportWechatMessage;
		}

		/**
		 * 获取是否支持微信消息
		 * 
		 * @return 是否支持微信消息
		 */
		public boolean getIsSupportWechatMessage() {
			return isSupportWechatMessage;
		}

	}

	/**
	 * 类型
	 */
	@Column(nullable = false, updatable = false, unique = true)
	private MessageConfig.Type type;

	/**
	 * 是否启用邮件
	 */
	@NotNull
	@Column(nullable = false)
	private Boolean isMailEnabled;

	/**
	 * 是否启用短信
	 */
	@NotNull
	@Column(nullable = false)
	private Boolean isSmsEnabled;

	/**
	 * 是否启用微信消息
	 */
	@NotNull
	@Column(nullable = false)
	private Boolean isWechatMessageEnabled;

	/**
	 * 微信消息模版
	 */
	@OneToOne(mappedBy = "messageConfig", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private WechatMessageTemplate wechatMessageTemplate;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public MessageConfig.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(MessageConfig.Type type) {
		this.type = type;
	}

	/**
	 * 获取是否启用邮件
	 * 
	 * @return 是否启用邮件
	 */
	public Boolean getIsMailEnabled() {
		return isMailEnabled;
	}

	/**
	 * 设置是否启用邮件
	 * 
	 * @param isMailEnabled
	 *            是否启用邮件
	 */
	public void setIsMailEnabled(Boolean isMailEnabled) {
		this.isMailEnabled = isMailEnabled;
	}

	/**
	 * 获取是否启用短信
	 * 
	 * @return 是否启用短信
	 */
	public Boolean getIsSmsEnabled() {
		return isSmsEnabled;
	}

	/**
	 * 设置是否启用短信
	 * 
	 * @param isSmsEnabled
	 *            是否启用短信
	 */
	public void setIsSmsEnabled(Boolean isSmsEnabled) {
		this.isSmsEnabled = isSmsEnabled;
	}

	/**
	 * 获取是否启用微信消息
	 * 
	 * @return 是否启用微信消息
	 */
	public Boolean getIsWechatMessageEnabled() {
		return isWechatMessageEnabled;
	}

	/**
	 * 设置是否启用微信消息
	 * 
	 * @param isWechatMessageEnabled
	 *            是否启用微信消息
	 */
	public void setIsWechatMessageEnabled(Boolean isWechatMessageEnabled) {
		this.isWechatMessageEnabled = isWechatMessageEnabled;
	}

	/**
	 * 获取微信消息模版
	 * 
	 * @return 微信消息模版
	 */
	public WechatMessageTemplate getWechatMessageTemplate() {
		return wechatMessageTemplate;
	}

	/**
	 * 设置微信消息模版
	 * 
	 * @param wechatMessageTemplate
	 *            微信消息模版
	 */
	public void setWechatMessageTemplate(WechatMessageTemplate wechatMessageTemplate) {
		this.wechatMessageTemplate = wechatMessageTemplate;
	}

}