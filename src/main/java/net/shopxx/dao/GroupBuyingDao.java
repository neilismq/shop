/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: Nk0e8Er4ho7eDNbNZJ/GV0hDQivKJ4vM
 */
package net.shopxx.dao;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.GroupBuying;
import net.shopxx.entity.Store;

/**
 * Dao - 团购
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface GroupBuyingDao extends BaseDao<GroupBuying, Long> {

	/**
	 * 查找团购分页
	 * 
	 * @param store
	 *            店铺
	 * @param hasBegan
	 *            已开始
	 * @param hasEnded
	 *            已结束
	 * @param pageable
	 *            分页信息
	 * @return 团购分页
	 */
	Page<GroupBuying> findPage(Store store, Boolean hasBegan, Boolean hasEnded, Pageable pageable);

}