/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * 
 * JavaScript - Base
 * Version: 6.1
 */

$().ready(function() {

	var $pageNumberItem = $("[data-page-number]");
	
	// 页码
	$pageNumberItem.click(function() {
		var $element = $(this);
		var $form = $element.closest("form");
		var $pageNumber = $form.find("input[name='pageNumber']");
		var pageNumber = $element.data("page-number");
		
		if ($form.length > 0) {
			if ($pageNumber.length > 0) {
				$pageNumber.val(pageNumber);
			} else {
				$form.append('<input name="pageNumber" type="hidden" value="' + pageNumber + '">');
			}
			$form.submit();
			return false;
		}
	});

});