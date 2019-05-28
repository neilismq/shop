/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: 4nhtWW3h1hKxRZ9RLE0tDmfAWnqCJRtZ
 */
package net.shopxx.service;

import net.shopxx.entity.GroupBuying;
import net.shopxx.entity.Member;
import net.shopxx.entity.Promotion;

/**
 * Service - 团购
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface GroupBuyingService extends BaseService<GroupBuying, Long> {

	/**
	 * 查询已购买数量
	 * 
	 * @param groupBuying
	 *            团购
	 * @param member
	 *            会员
	 * @return 允许购买数量
	 */
	Integer grantedPurchasedQuantity(GroupBuying groupBuying, Member member);

	/**
	 * 获取已参团人数
	 * 
	 * @param groupBuyingId
	 *            团购ID
	 * @return 已参团人数
	 */
	Long getParticipants(Long groupBuyingId);

	/**
	 * 添加已参团人数
	 * 
	 * @param groupBuying
	 *            团购
	 * @param member
	 *            会员
	 */
	void addParticipants(GroupBuying groupBuying, Member member);

	/**
	 * 减少已参团人数
	 * 
	 * @param groupBuying
	 *            团购
	 * @param member
	 *            会员
	 */
	void subtractParticipants(GroupBuying groupBuying, Member member);

	/**
	 * 结束
	 * 
	 * @param promotion
	 *            促销
	 */
	void end(Promotion promotion);

}