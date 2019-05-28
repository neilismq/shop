/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: 4CBNfOWEnP9JK+6ZZxvIlumeVCuUysOh
 */
package net.shopxx.entity;

import net.shopxx.BaseAttributeConverter;
import net.shopxx.Setting;
import net.shopxx.util.SystemUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity - 微信消息模版
 *
 * @author SHOP++ Team
 * @version 6.1
 */
@Entity
public class WechatMessageTemplate extends BaseEntity<Long> {

	private static final long serialVersionUID = -4610811438298528888L;

	/**
	 * 属性标签名称
	 */
	private static final String ATTRIBUTE_TAG_NMAE = "%s";

	/**
	 * 最大条目数量
	 */
	public static final int MAX_ENTRY_SIZE = 100;

	/**
	 * 订单属性
	 */
	public enum OrderAttribute {

		/**
		 * 订单编号
		 */
		ORDER_SN("sn"),

		/**
		 * 订单创建日期
		 */
		ORDER_CREATED_DATE("createdDate"),

		/**
		 * 订单修改日期
		 */
		ORDER_LAST_MODIFIED_DATE("lastModifiedDate"),

		/**
		 * 订单状态
		 */
		ORDER_STATUS("status"),

		/**
		 * 订单金额
		 */
		ORDER_AMOUNT("amount"),

		/**
		 * 订单收货人
		 */
		ORDER_CONSIGNEE("consignee"),

		/**
		 * 订单收货地址
		 */
		ORDER_ADDRESS("address"),

		/**
		 * 订单收货电话
		 */
		ORDER_PHONE("phone"),

		/**
		 * 订单商品名称
		 */
		ORDER_PRODUCT_NAME("productName"),

		/**
		 * 订单完成日期
		 */
		ORDER_COMPLETE_DATE("completeDate");

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 构造方法
		 *
		 * @param name
		 * 		名称
		 */
		OrderAttribute(String name) {
			this.name = name;
		}

		/**
		 * 获取标签名称
		 *
		 * @return 标签名称
		 */
		public String getTagName() {
			return String.format(WechatMessageTemplate.ATTRIBUTE_TAG_NMAE, toString());
		}

		/**
		 * 获取值
		 *
		 * @param order
		 * 		订单
		 * @return 值
		 */
		public String getValue(Order order) {
			if (order == null) {
				return null;
			}

			try {
				Class<?> type = PropertyUtils.getPropertyType(order, name);
				Object value = PropertyUtils.getProperty(order, name);
				return handleObjectToString(value, type);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	/**
	 * 会员属性
	 */
	public enum MemberAttribute {

		/**
		 * 会员用户名
		 */
		MEMBER_USERNAME("username"),

		/**
		 * 会员手机
		 */
		MEMBER_MOBILE("mobile"),

		/**
		 * 会员Email
		 */
		MEMBER_EMAIL("email");

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 构造方法
		 *
		 * @param name
		 * 		名称
		 */
		MemberAttribute(String name) {
			this.name = name;
		}

		/**
		 * 获取标签名称
		 *
		 * @return 标签名称
		 */
		public String getTagName() {
			return String.format(WechatMessageTemplate.ATTRIBUTE_TAG_NMAE, toString());
		}

		/**
		 * 获取值
		 *
		 * @param member
		 * 		会员
		 * @return 值
		 */
		public String getValue(Member member) {
			if (member == null) {
				return null;
			}

			try {
				Class<?> type = PropertyUtils.getPropertyType(member, name);
				Object value = PropertyUtils.getProperty(member, name);
				return handleObjectToString(value, type);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	/**
	 * 发货单属性
	 */
	public enum OrderShippingAttribute {

		/**
		 * 发货单运单号
		 */
		ORDER_SHIPPING_TRACKINGNO("trackingNo"),

		/**
		 * 发货单物流公司
		 */
		ORDER_SHIPPING_DELIVERY_CORP("deliveryCorp"),

		/**
		 * 发货单发货时间
		 */
		ORDER_SHIPPING_LAST_MODIFIED_DATE("lastModifiedDate"),

		/**
		 * 发货单收货人
		 */
		ORDER_SHIPPING_CONSIGNEE("consignee"),

		/**
		 * 发货单收货地区
		 */
		ORDER_SHIPPING_AREA("area"),

		/**
		 * 发货单收货地址
		 */
		ORDER_SHIPPING_ADDRESS("address");

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 构造方法
		 *
		 * @param name
		 * 		名称
		 */
		OrderShippingAttribute(String name) {
			this.name = name;
		}

		/**
		 * 获取标签名称
		 *
		 * @return 标签名称
		 */
		public String getTagName() {
			return String.format(WechatMessageTemplate.ATTRIBUTE_TAG_NMAE, toString());
		}

		/**
		 * 获取值
		 *
		 * @param orderShipping
		 * 		发货单
		 * @return 值
		 */
		public String getValue(OrderShipping orderShipping) {
			if (orderShipping == null) {
				return null;
			}

			try {
				Class<?> type = PropertyUtils.getPropertyType(orderShipping, name);
				Object value = PropertyUtils.getProperty(orderShipping, name);
				return handleObjectToString(value, type);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	/**
	 * 订单退款属性
	 */
	public enum OrderRefundsAttribute {

		/**
		 * 订单退款方式
		 */
		ORDER_REFUNDS_METHOD("method"),

		/**
		 * 订单退款金额
		 */
		ORDER_REFUNDS_AMOUNT("amount"),

		/**
		 * 订单退款创建时间
		 */
		ORDER_REFUNDS_CREATED_DATE("createdDate");

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 构造方法
		 *
		 * @param name
		 * 		名称
		 */
		OrderRefundsAttribute(String name) {
			this.name = name;
		}

		/**
		 * 获取标签名称
		 *
		 * @return 标签名称
		 */
		public String getTagName() {
			return String.format(WechatMessageTemplate.ATTRIBUTE_TAG_NMAE, toString());
		}

		/**
		 * 获取值
		 *
		 * @param orderRefunds
		 * 		订单退款
		 * @return 值
		 */
		public String getValue(OrderRefunds orderRefunds) {
			if (orderRefunds == null) {
				return null;
			}

			try {
				Class<?> type = PropertyUtils.getPropertyType(orderRefunds, name);
				Object value = PropertyUtils.getProperty(orderRefunds, name);
				return handleObjectToString(value, type);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	/**
	 * 标题
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String title;

	/**
	 * 模版Id
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String templateId;

	/**
	 * 微信消息模版参数
	 */
	@Valid
	@Size(max = MAX_ENTRY_SIZE)
	@Column(name = "templateParameters", length = 4000)
	@Convert(converter = WechatMessageTemplateParameterConverter.class)
	private List<WechatMessageTemplateParameter> wechatMessageTemplateParameters = new ArrayList<>();

	/**
	 * 消息配置
	 */
	@NotNull(groups = Save.class)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private MessageConfig messageConfig;

	/**
	 * 获取标题
	 *
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 * 		标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取模版Id
	 *
	 * @return 模版Id
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * 设置模版Id
	 *
	 * @param templateId
	 * 		模版Id
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * 获取微信消息模版参数
	 *
	 * @return 微信消息模版参数
	 */
	public List<WechatMessageTemplateParameter> getWechatMessageTemplateParameters() {
		return wechatMessageTemplateParameters;
	}

	/**
	 * 设置微信消息模版参数
	 *
	 * @param wechatMessageTemplateParameters
	 * 		微信消息模版参数
	 */
	public void setWechatMessageTemplateParameters(List<WechatMessageTemplateParameter> wechatMessageTemplateParameters) {
		this.wechatMessageTemplateParameters = wechatMessageTemplateParameters;
	}

	/**
	 * 获取消息配置
	 *
	 * @return 消息配置
	 */
	public MessageConfig getMessageConfig() {
		return messageConfig;
	}

	/**
	 * 设置消息配置
	 *
	 * @param messageConfig
	 * 		消息配置
	 */
	public void setMessageConfig(MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}

	/**
	 * 获取微信模版参数值
	 *
	 * @param parameterName
	 * 		模版参数名
	 * @return 微信模版参数值
	 */
	@Transient
	public String getWechatMessageTemplateParameterValue(final String parameterName) {
		WechatMessageTemplateParameter wechatMessageTemplateParameter = (WechatMessageTemplateParameter) CollectionUtils.find(getWechatMessageTemplateParameters(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				WechatMessageTemplateParameter messageTemplateParameter = (WechatMessageTemplateParameter) object;
				return messageTemplateParameter != null && StringUtils.equalsIgnoreCase(messageTemplateParameter.getName(), parameterName);
			}
		});
		return wechatMessageTemplateParameter != null ? wechatMessageTemplateParameter.getValue() : StringUtils.EMPTY;
	}

	/**
	 * 获取微信模版参数类型
	 *
	 * @param parameterName
	 * 		模版参数名
	 * @return 微信模版参数类型
	 */
	@Transient
	public String getWechatMessageTemplateParameterType(final String parameterName) {
		WechatMessageTemplateParameter wechatMessageTemplateParameter = (WechatMessageTemplateParameter) CollectionUtils.find(getWechatMessageTemplateParameters(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				WechatMessageTemplateParameter messageTemplateParameter = (WechatMessageTemplateParameter) object;
				return messageTemplateParameter != null && StringUtils.equalsIgnoreCase(messageTemplateParameter.getName(), parameterName);
			}
		});
		return wechatMessageTemplateParameter != null ? wechatMessageTemplateParameter.getType().toString() : StringUtils.EMPTY;
	}

	/**
	 * 处理object对象，并转换为字符串
	 *
	 * @param object
	 * 		object对象
	 * @param type
	 * 		类型
	 * @return 字符串
	 */
	public static String handleObjectToString(Object object, Class<?> type) {
		String value = StringUtils.EMPTY;
		if (object == null) {
			return value;
		}

		Setting setting = SystemUtils.getSetting();
		if (BigDecimal.class.isAssignableFrom(type)) {
			value = String.valueOf(setting.setScale(new BigDecimal(String.valueOf(object))));
		} else if (Date.class.isAssignableFrom(type)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			value = String.valueOf(simpleDateFormat.format((Date) object));
		} else {
			value = String.valueOf(object);
		}
		return value;
	}


	/**
	 * 类型转换 - 微信消息模版参数
	 *
	 * @author SHOP++ Team
	 * @version 6.1
	 */
	@Converter
	public static class WechatMessageTemplateParameterConverter extends BaseAttributeConverter<ArrayList<WechatMessageTemplateParameter>> {
	}

}