/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: +5NY8d+XdLcC5IRbfUOf6T5IgKxiSsNd
 */
package net.shopxx.dao;

import net.shopxx.entity.Seo;

/**
 * Dao - SEO设置
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface SeoDao extends BaseDao<Seo, Long> {

	/**
	 * 查找SEO设置
	 * 
	 * @param type
	 *            类型
	 * @return SEO设置
	 */
	Seo find(Seo.Type type);

}