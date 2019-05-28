/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: ERfYM/IhIMUtGjpEzw5zfHjKZhvTmopb
 */
package net.shopxx.dao;

import net.shopxx.entity.Sn;

/**
 * Dao - 序列号
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface SnDao {

	/**
	 * 生成序列号
	 * 
	 * @param type
	 *            类型
	 * @return 序列号
	 */
	String generate(Sn.Type type);

}