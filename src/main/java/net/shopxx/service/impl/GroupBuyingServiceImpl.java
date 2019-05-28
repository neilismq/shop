/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: aIPM4SilVsPqmqyAmVvShMi3IkilxKsD
 */
package net.shopxx.service.impl;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopxx.dao.GroupBuyingDao;
import net.shopxx.dao.MemberDao;
import net.shopxx.dao.OrderDao;
import net.shopxx.entity.GroupBuying;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.Product;
import net.shopxx.entity.Promotion;
import net.shopxx.exception.ResourceNotFoundException;
import net.shopxx.plugin.GroupBuyingPromotionPlugin.GroupBuyingAttribute;
import net.shopxx.service.GroupBuyingService;

/**
 * Service - 团购
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Service
public class GroupBuyingServiceImpl extends BaseServiceImpl<GroupBuying, Long> implements GroupBuyingService {

	@Inject
	private CacheManager cacheManager;
	@Inject
	private GroupBuyingDao groupBuyingDao;
	@Inject
	private OrderDao orderDao;
	@Inject
	private MemberDao memberDao;

	@Override
	@Transactional
	public GroupBuying update(GroupBuying groupBuying) {
		Assert.notNull(groupBuying, "[Assertion failed] - groupBuying is required; it must not be null");

		flushGroupBuyingStatusIfNesscessary(getParticipants(groupBuying.getId()), groupBuying);
		return super.update(groupBuying);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer grantedPurchasedQuantity(GroupBuying groupBuying, Member member) {
		Assert.notNull(groupBuying, "[Assertion failed] - groupBuying is required; it must not be null");
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");

		Order.Status[] excludes = new Order.Status[] { Order.Status.FAILED, Order.Status.CANCELED, Order.Status.DENIED };
		return orderDao.grantedPurchasedQuantity(groupBuying, member, Product.Type.GENERAL, false, excludes);
	}

	@Override
	public Long getParticipants(Long groupBuyingId) {
		Assert.notNull(groupBuyingId, "[Assertion failed] - groupBuyingId is required; it must not be null");

		GroupBuying groupBuying = groupBuyingDao.find(groupBuyingId);
		if (groupBuying == null) {
			return 0L;
		}

		Ehcache cache = cacheManager.getEhcache(GroupBuying.PARTICIPANTS_CACHE_NAME);
		cache.acquireWriteLockOnKey(groupBuyingId);
		try {
			Element element = cache.get(groupBuyingId);
			if (element != null) {
				return (Long) element.getObjectValue();
			} else {
				Long count = memberDao.grantedGroupBuyingParticipants(groupBuying, GroupBuying.ACTIVE_ORDER_STATUSES);
				cache.put(new Element(groupBuyingId, count));
				return count;
			}
		} finally {
			cache.releaseWriteLockOnKey(groupBuyingId);
		}
	}

	@Override
	public void addParticipants(GroupBuying groupBuying, Member member) {
		Assert.notNull(groupBuying, "[Assertion failed] - groupBuying is required; it must not be null");

		Ehcache cache = cacheManager.getEhcache(GroupBuying.PARTICIPANTS_CACHE_NAME);
		cache.acquireWriteLockOnKey(groupBuying.getId());
		try {
			if (orderDao.count(groupBuying, member, GroupBuying.ACTIVE_ORDER_STATUSES) == 0) {
				Element element = cache.get(groupBuying.getId());
				Long participants;
				if (element != null) {
					participants = (Long) element.getObjectValue() + 1;
				} else {
					participants = memberDao.grantedGroupBuyingParticipants(groupBuying, GroupBuying.ACTIVE_ORDER_STATUSES) + 1;
				}
				cache.put(new Element(groupBuying.getId(), participants));
				flushGroupBuyingStatusIfNesscessary(participants, groupBuying);
			}
		} finally {
			cache.releaseWriteLockOnKey(groupBuying.getId());
		}
	}

	@Override
	public void subtractParticipants(GroupBuying groupBuying, Member member) {
		Assert.notNull(groupBuying, "[Assertion failed] - groupBuying is required; it must not be null");

		Ehcache cache = cacheManager.getEhcache(GroupBuying.PARTICIPANTS_CACHE_NAME);
		cache.acquireWriteLockOnKey(groupBuying.getId());
		try {
			if (orderDao.count(groupBuying, member, GroupBuying.ACTIVE_ORDER_STATUSES) == 0) {
				Element element = cache.get(groupBuying.getId());
				Long participants;
				if (element != null) {
					participants = (Long) element.getObjectValue() - 1;
				} else {
					participants = memberDao.grantedGroupBuyingParticipants(groupBuying, GroupBuying.ACTIVE_ORDER_STATUSES) - 1;
				}
				cache.put(new Element(groupBuying.getId(), participants));
			}
		} finally {
			cache.releaseWriteLockOnKey(groupBuying.getId());
		}
	}

	@Override
	public void end(Promotion promotion) {
		Assert.notNull(promotion, "[Assertion failed] - promotion is required; it must not be null");
		Assert.state(GroupBuyingAttribute.class.isAssignableFrom(promotion.getPromotionDefaultAttribute().getClass()), "[Assertion failed] - PromotionDefaultAttribute must is assignable from GroupBuyingAttribute");

		if (GroupBuyingAttribute.class.isAssignableFrom(promotion.getPromotionDefaultAttribute().getClass())) {
			GroupBuyingAttribute groupBuyingAttribute = (GroupBuyingAttribute) promotion.getPromotionDefaultAttribute();
			Date now = new Date();
			promotion.setEndDate(now);
			GroupBuying groupBuying = groupBuyingAttribute.getGroupBuying();
			if (groupBuying == null) {
				throw new ResourceNotFoundException();
			}
			groupBuying.setEndDate(now);
		}
	}

	/**
	 * 刷新团购状态
	 * 
	 * @param participants
	 *            已参团人数
	 * @param groupBuying
	 *            团购
	 */
	private void flushGroupBuyingStatusIfNesscessary(Long participants, GroupBuying groupBuying) {
		if (groupBuying != null && !groupBuying.hasEnded() && GroupBuying.Status.PENDING_GROUP_SUCCESS.equals(groupBuying.getStatus())) {
			if (groupBuying.getGroupSize() <= participants) {
				groupBuying.setStatus(GroupBuying.Status.GROUP_SUCCESSED);
			}
		}
	}

}