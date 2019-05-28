/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: g/j3GryPGB+SDhsfLMd/OnEahYM7WxL2
 */
package net.shopxx.audit;

/**
 * Audit - 审计者Provider
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface AuditorProvider<T> {

	/**
	 * 获取当前审计者
	 * 
	 * @return 当前审计者
	 */
	T getCurrentAuditor();

}