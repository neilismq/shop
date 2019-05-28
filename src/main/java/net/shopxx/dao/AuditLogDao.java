/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: /MUVx0ORLkwfCzVr/6KQAMdX4rHR2M2b
 */
package net.shopxx.dao;

import net.shopxx.entity.AuditLog;

/**
 * Dao - 审计日志
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
public interface AuditLogDao extends BaseDao<AuditLog, Long> {

	/**
	 * 删除所有
	 */
	void removeAll();

}