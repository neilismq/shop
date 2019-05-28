/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: McXaBJMysNa5Z+XYgzbDiBtPxHV76Mdb
 */
package net.shopxx.dao;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.DistributionCommission;
import net.shopxx.entity.Distributor;

/**
 * Dao - 分销佣金
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface DistributionCommissionDao extends BaseDao<DistributionCommission, Long> {

	/**
	 * 查找分销佣金记录分页
	 * 
	 * @param distributor
	 *            分销员
	 * @param pageable
	 *            分页信息
	 * @return 分销佣金记录分页
	 */
	Page<DistributionCommission> findPage(Distributor distributor, Pageable pageable);

}