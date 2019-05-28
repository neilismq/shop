/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: HcxYxTqXGP1hIeiqK69sr+Sv7KL4/tAY
 */
package net.shopxx.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.MemberRank;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductCategory;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.PromotionDefaultAttribute;
import net.shopxx.entity.Store;
import net.shopxx.plugin.PromotionPlugin;

/**
 * Service - 促销
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface PromotionService extends BaseService<Promotion, Long> {

	/**
	 * 验证价格运算表达式
	 * 
	 * @param priceExpression
	 *            价格运算表达式
	 * @return 验证结果
	 */
	boolean isValidPriceExpression(String priceExpression);

	/**
	 * 通过名称查找促销
	 * 
	 * @param keyword
	 *            关键词
	 * @param excludes
	 *            排除促销
	 * @param count
	 *            数量
	 * @return 促销
	 */
	List<Promotion> search(String keyword, Set<Promotion> excludes, Integer count);

	/**
	 * 查找促销
	 * 
	 * @param promotionPlugin
	 *            促销插件
	 * @param store
	 *            店铺
	 * @param isEnabled
	 *            是否开启
	 * @return 促销
	 */
	List<Promotion> findList(PromotionPlugin promotionPlugin, Store store, Boolean isEnabled);

	/**
	 * 查找促销
	 * 
	 * @param promotionPlugin
	 *            促销插件
	 * @param store
	 *            店铺
	 * @param memberRank
	 *            会员等级
	 * @param productCategory
	 *            商品分类
	 * @param hasBegun
	 *            是否已开始
	 * @param hasEnded
	 *            是否已结束
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 促销
	 */
	List<Promotion> findList(PromotionPlugin promotionPlugin, Store store, MemberRank memberRank, ProductCategory productCategory, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找促销
	 * 
	 * @param promotionPlugin
	 *            促销插件
	 * @param storeId
	 *            店铺ID
	 * @param memberRankId
	 *            会员等级ID
	 * @param productCategoryId
	 *            商品分类ID
	 * @param hasBegun
	 *            是否已开始
	 * @param hasEnded
	 *            是否已结束
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 促销
	 */
	List<Promotion> findList(PromotionPlugin promotionPlugin, Long storeId, Long memberRankId, Long productCategoryId, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找促销
	 * 
	 * @param promotionPlugin
	 *            促销插件
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 促销分页
	 */
	Page<Promotion> findPage(PromotionPlugin promotionPlugin, Store store, Pageable pageable);

	/**
	 * 根据促销插件关闭促销
	 * 
	 * @param promotionPlugin
	 *            促销插件
	 */
	void shutDownPromotion(PromotionPlugin promotionPlugin);

	/**
	 * 判断促销是否存在
	 * 
	 * @param promotionPluginId
	 *            促销插件ID
	 * @param product
	 *            商品
	 * @param excludes
	 *            排除促销
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return 判断促销是否存在
	 */
	boolean exists(String promotionPluginId, Product product, Collection<Promotion> excludes, Date beginDate, Date endDate);

	/**
	 * 创建
	 * 
	 * @param promotion
	 *            促销
	 * @param promotionDefaultAttribute
	 *            促销默认属性
	 * @return 促销
	 */
	Promotion create(Promotion promotion, PromotionDefaultAttribute promotionDefaultAttribute);

	/**
	 * 修改
	 * 
	 * @param promotion
	 *            促销
	 * @param promotionDefaultAttribute
	 *            促销默认属性
	 */
	void modify(Promotion promotion, PromotionDefaultAttribute promotionDefaultAttribute);
}