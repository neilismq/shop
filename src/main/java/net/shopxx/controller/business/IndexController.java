/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: KDNrbI1waxGIDnak/lYEvYOec27yA3Z/
 */
package net.shopxx.controller.business;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 商家中心
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Controller("businessIndexController")
@RequestMapping("/business/index")
public class IndexController extends BaseController {

	/**
	 * 首页
	 */
	@GetMapping
	public String index() {
		return "/business/index";
	}

}