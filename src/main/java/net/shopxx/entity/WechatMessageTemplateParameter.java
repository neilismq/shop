/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: LQg74m7aVoqoKMdmqoOsA/T9bZEB80wW
 */
package net.shopxx.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

/**
 * Entity - 微信消息模版参数
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public class WechatMessageTemplateParameter implements Serializable {

	private static final long serialVersionUID = -5157327274189523583L;

	/**
	 * 类型
	 */
	public enum Type {

		/**
		 * 文本
		 */
		TEXT,

		/**
		 * 下拉框
		 */
		SELECT
	}

	/**
	 * 类型
	 */
	@NotNull
	private WechatMessageTemplateParameter.Type type;

	/**
	 * 名称
	 */
	@Length(max = 200)
	private String name;

	/**
	 * 值
	 */
	@Length(max = 200)
	private String value;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public WechatMessageTemplateParameter.Type getType() {
		return type;
	}

	/**
	 * 类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(WechatMessageTemplateParameter.Type type) {
		this.type = type;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取值
	 * 
	 * @return 值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value
	 *            值
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}