/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: Yz6ivpk++4WnDvQ9nRhyfXkM4Uk0Z8Oq
 */
package net.shopxx.service;

import net.shopxx.entity.Sn;

/**
 * Service - 序列号
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface SnService {

	/**
	 * 生成序列号
	 * 
	 * @param type
	 *            类型
	 * @return 序列号
	 */
	String generate(Sn.Type type);

}