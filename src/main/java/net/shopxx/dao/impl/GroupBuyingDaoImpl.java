/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: bXBIOpkiFhossvVGt10PWpABfQi7qiVI
 */
package net.shopxx.dao.impl;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.GroupBuyingDao;
import net.shopxx.entity.GroupBuying;
import net.shopxx.entity.Store;

/**
 * Dao - 团购
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Repository
public class GroupBuyingDaoImpl extends BaseDaoImpl<GroupBuying, Long> implements GroupBuyingDao {

	@Override
	public Page<GroupBuying> findPage(Store store, Boolean hasBegan, Boolean hasEnded, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GroupBuying> criteriaQuery = criteriaBuilder.createQuery(GroupBuying.class);
		Root<GroupBuying> root = criteriaQuery.from(GroupBuying.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (store != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("store"), store));
		}
		if (hasBegan != null) {
			Date now = new Date();
			if (hasBegan) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("beginDate"), now));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThan(root.<Date>get("beginDate"), now));
			}
		}
		if (hasEnded != null) {
			Date now = new Date();
			if (hasEnded) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThan(root.<Date>get("endDate"), now));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("endDate"), now));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}