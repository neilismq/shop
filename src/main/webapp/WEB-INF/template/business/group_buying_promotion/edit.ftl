<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, max-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="SHOP++ Team">
	<meta name="copyright" content="SHOP++">
	<title>${message("business.groupBuyingPromotionPlugin.edit")} - Powered By SHOP++</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/ajax-bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-datetimepicker.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-fileinput.css" rel="stylesheet">
	<link href="${base}/resources/common/css/summernote.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/business/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/ajax-bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/moment.js"></script>
	<script src="${base}/resources/common/js/bootstrap-datetimepicker.js"></script>
	<script src="${base}/resources/common/js/bootstrap-fileinput.js"></script>
	<script src="${base}/resources/common/js/summernote.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/lodash.js"></script>
	<script src="${base}/resources/common/js/URI.js"></script>
	<script src="${base}/resources/common/js/velocity.js"></script>
	<script src="${base}/resources/common/js/velocity.ui.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/business/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
				
				var $groupBuyingPromotionForm = $("#groupBuyingPromotionForm");
				
				// 表单验证
				$groupBuyingPromotionForm.validate({
					rules: {
						name: "required",
						purchasingLimit: "digits",
						order: "digits"
					}
				});
			
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="business">
	[#include "/business/include/main_header.ftl" /]
	[#include "/business/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<ol class="breadcrumb">
				<li>
					<a href="${base}/business/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("business.groupBuyingPromotionPlugin.edit")}</li>
			</ol>
			<form id="groupBuyingPromotionForm" class="ajax-form form-horizontal" action="${base}/business/group_buying_promotion/update" method="post">
				<input name="promotionId" type="hidden" value="${promotion.id}">
				<input name="groupBuyingAttributeId" type="hidden" value="${groupBuyingAttribute.id}">
				<input name="groupBuyingId" type="hidden" value="${groupBuyingAttribute.groupBuying.id}">
				<div class="panel panel-default">
					<div class="panel-body">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#base" data-toggle="tab">${message("business.giftPromotionPlugin.base")}</a>
							</li>
							<li>
								<a href="#introduction" data-toggle="tab">${message("Promotion.introduction")}</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="base" class="tab-pane active">
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="name">${message("Promotion.name")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="name" name="name" class="form-control" type="text" value="${promotion.name}" maxlength="200">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Promotion.image")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input name="image" type="hidden" data-provide="fileinput" value="${promotion.image}" data-file-type="IMAGE">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="beginDate">${message("common.dateRange")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="input-group" data-provide="datetimerangepicker" data-date-format="YYYY-MM-DD HH:mm:ss">
											<input id="beginDate" name="beginDate" class="form-control" type="text" value="[#if promotion.beginDate??]${promotion.beginDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]" disabled>
											<span class="input-group-addon">-</span>
											<input name="endDate" class="form-control" type="text" value="[#if promotion.endDate??]${promotion.endDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]" disabled>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("GroupBuyingPromotionPlugin.discountType")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select id="discountType" name="discountType" class="selectpicker form-control" data-size="10" disabled>
											[#list discountTypes as discountType]
												<option value="${discountType}"[#if discountType == groupBuyingAttribute.discountType] selected[/#if]>${message("GroupBuyingPromotionPlugin.DiscountType." + discountType)}</option>
											[/#list]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="value">${message("GroupBuyingPromotionPlugin.discounValue")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="discounValue" name="discounValue" class="form-control" type="text" value="${groupBuyingAttribute.discounValue}" maxlength="16" disabled>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required" for="purchasingLimit">${message("GroupBuying.groupSize")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="groupSize" name="groupSize" class="form-control" type="text" value="${groupBuyingAttribute.groupBuying.groupSize}" maxlength="9" disabled>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="purchasingLimit">${message("GroupBuying.purchasingLimit")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="purchasingLimit" name="purchasingLimit" class="form-control" type="text" value="${groupBuyingAttribute.groupBuying.purchasingLimit}" maxlength="9">
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label item-required">${message("Promotion.products")}:</label>
									<div class="col-xs-9 col-sm-4">
										<select id="productId" name="productId" class="form-control" title="${message("Promotion.products")}" disabled>
											[#if groupBuyingAttribute.promotion.products?has_content]
												[#list groupBuyingAttribute.promotion.products as product]
													<option value="${product.id}" selected>${product.name}</option>
													<input type="hidden" name="productId" value="${product.id}" />
												[/#list]
											[/#if]
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("Promotion.memberRanks")}:</label>
									<div class="col-xs-9 col-sm-10">
										[#list memberRanks as memberRank]
											<div class="checkbox checkbox-inline">
												<input id="memberRank_${memberRank.id}" name="memberRankIds" type="checkbox" value="${memberRank.id}"[#if promotion.memberRanks?seq_contains(memberRank)] checked[/#if]>
												<label for="memberRank_${memberRank.id}">${memberRank.name}</label>
											</div>
										[/#list]
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label">${message("common.setting")}:</label>
									<div class="col-xs-9 col-sm-4">
										<div class="checkbox checkbox-inline">
											<input name="_isCouponAllowed" type="hidden" value="false">
											<input id="isCouponAllowed" name="isCouponAllowed" type="checkbox" value="true"[#if promotion.isCouponAllowed] checked[/#if]>
											<label for="isCouponAllowed">${message("Promotion.isCouponAllowed")}</label>
										</div>
										<div class="checkbox checkbox-inline">
											<input name="_isEnabled" type="hidden" value="false">
											<input id="isEnabled" name="isEnabled" type="checkbox" value="true"[#if promotion.isEnabled] checked[/#if]>
											<label for="isEnabled">${message("Promotion.isEnabled")}</label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 col-sm-2 control-label" for="order">${message("common.order")}:</label>
									<div class="col-xs-9 col-sm-4">
										<input id="order" name="order" class="form-control" type="text" value="${promotion.order}" maxlength="9">
									</div>
								</div>
							</div>
							<div id="introduction" class="tab-pane">
								<textarea name="introduction" data-provide="editor">${promotion.introduction}</textarea>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<div class="row">
							<div class="col-xs-9 col-sm-10 col-xs-offset-3 col-sm-offset-2">
								<button class="btn btn-primary" type="submit">${message("common.submit")}</button>
								<button class="btn btn-default" type="button" data-action="back">${message("common.back")}</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</main>
</body>
</html>