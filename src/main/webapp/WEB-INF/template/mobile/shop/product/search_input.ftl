<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="SHOP++ Team">
	<meta name="copyright" content="SHOP++">
	[@seo type = "PRODUCT_SEARCH"]
		[#if seo.resolveKeywords()?has_content]
			<meta name="keywords" content="${seo.resolveKeywords()}">
		[/#if]
		[#if seo.resolveDescription()?has_content]
			<meta name="description" content="${seo.resolveDescription()}">
		[/#if]
		<title>${seo.resolveTitle()}[#if showPowered] - Powered By SHOP++[/#if]</title>
	[/@seo]
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/mobile/shop/css/base.css" rel="stylesheet">
	<link href="${base}/resources/mobile/shop/css/product.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/jquery.scrolltofixed.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/jquery.base64.js"></script>
	<script src="${base}/resources/common/js/lodash.js"></script>
	<script src="${base}/resources/common/js/URI.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/mobile/shop/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $searchForm = $("#searchForm");
				var $keyword = $("#searchForm [name='keyword']");
				
				// 搜索
				$searchForm.submit(function() {
					if ($.trim($keyword.val()) === "") {
						return false;
					}
				});

                // TitleNView搜索
                $.setCurrentWebviewStyle("titleNView.searchInput.placeholder", "${message("shop.product.search")}");

                if ($.isHtml5Plus()) {
                    function plusReady() {
                        var currentWebview = window.plus.webview.currentWebview();

                        // TitleNView搜索
						if (currentWebview != null) {
                            currentWebview.addEventListener("titleNViewSearchInputConfirmed", function(e) {
                                if ($.trim(e.text) === "") {
                                    return;
                                }
								currentWebview.setTitleNViewSearchInputFocus(false);
                                window.location = URI("${base}/product/search").addSearch("keyword", e.text).toString();
                            }, false);
						}
                    }

                    if (window.plus) {
                        plusReady();
                    } else {
                        document.addEventListener("plusready", plusReady, false);
                    }
                }

			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="shop product-search-input">
	<header class="header-default html5plus-hidden" data-spy="scrollToFixed">
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-1">
					<a href="javascript:;" data-action="back">
						<i class="iconfont icon-back"></i>
					</a>
				</div>
				<div class="col-xs-11">
					<form id="searchForm" action="${base}/product/search" method="get">
						<div class="input-group">
							<input name="keyword" class="form-control" type="text" placeholder="${message("shop.product.search")}" x-webkit-speech="x-webkit-speech" x-webkit-grammar="builtin:search">
							<div class="input-group-btn">
								<button class="btn btn-default" type="submit">
									<i class="iconfont icon-search"></i>
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</header>
	<main>
		[#if setting.hotSearches?has_content]
			<dl class="hot-search clearfix">
				<dt>${message("shop.index.hotSearch")}</dt>
				[#list setting.hotSearches as hotSearch]
					<dd>
						<a href="${base}/product/search?keyword=${hotSearch?url}">${hotSearch}</a>
					</dd>
				[/#list]
			</dl>
		[/#if]
	</main>
</body>
</html>