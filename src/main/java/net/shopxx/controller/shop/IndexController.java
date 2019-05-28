/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: CEQ9e/V7s1ty1iuhY0kVyKX852g55ERA
 */
package net.shopxx.controller.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 首页
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@Controller("shopIndexController")
@RequestMapping("/")
public class IndexController extends BaseController {

	/**
	 * 首页
	 */
	@GetMapping
	public String index(ModelMap model) {
		return "shop/index";
	}

}