/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: K3qJBTNQKHVqPu8QQJg69t1Op3asvF0X
 */
package net.shopxx.job;

import javax.inject.Inject;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.shopxx.service.OrderService;

/**
 * Job - 订单
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Lazy(false)
@Component
public class OrderJob {

	@Inject
	private OrderService orderService;

	/**
	 * 过期订单处理
	 */
	@Scheduled(cron = "${job.order_expired_processing.cron}")
	public void expiredProcessing() {
		orderService.expiredRefundHandle();
		orderService.undoExpiredUseCouponCode();
		orderService.undoExpiredExchangePoint();
		orderService.releaseExpiredAllocatedStock();
	}

	/**
	 * 自动收货
	 */
	@Scheduled(cron = "${job.order_automatic_receive.cron}")
	public void automaticReceive() {
		orderService.automaticReceive();
	}

	/**
	 * 成团失败处理
	 */
	@Scheduled(cron = "${job.order_group_failure.cron}")
	public void groupFailureProcessing() {
		orderService.groupFailure();
	}

}