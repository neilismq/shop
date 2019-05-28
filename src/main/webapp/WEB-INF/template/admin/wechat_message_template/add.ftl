<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="SHOP++ Team">
	<meta name="copyright" content="SHOP++">
	<title>${message("admin.wechatMessageTemplate.add")} - Powered By SHOP++</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
	<link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/common/css/bootstrap-select.css" rel="stylesheet">
	<link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<link href="${base}/resources/common/css/base.css" rel="stylesheet">
	<link href="${base}/resources/admin/css/base.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/bootstrap-growl.js"></script>
	<script src="${base}/resources/common/js/bootstrap-select.js"></script>
	<script src="${base}/resources/common/js/jquery.nicescroll.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.js"></script>
	<script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
	<script src="${base}/resources/common/js/jquery.form.js"></script>
	<script src="${base}/resources/common/js/jquery.cookie.js"></script>
	<script src="${base}/resources/common/js/lodash.js"></script>
	<script src="${base}/resources/common/js/URI.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/admin/js/base.js"></script>
	<script id="optionTemplate" type="text/template">
		<%if (data.templateContent != null) {%>
			<div class="form-group">
				<label class="col-xs-3 col-sm-2 control-label">${message("admin.wechatMessageTemplate.content")}:</label>
				<div class="col-xs-9 col-sm-4">
					<textarea class="form-control" rows="5" readonly><%-data.templateContent%></textarea>
				</div>
			</div>
		<%}%>
		<%if (data.templateExample != null) {%>
			<div class="form-group">
				<label class="col-xs-3 col-sm-2 control-label">${message("admin.wechatMessageTemplate.example")}:</label>
				<div class="col-xs-9 col-sm-4">
					<textarea class="form-control" rows="5" readonly><%-data.templateExample%></textarea>
				</div>
			</div>
		<%}%>
		<%_.each(data.templateParameters, function(templateParameter, i) {%>
			<div class="form-group">
				<div class="col-xs-9 col-sm-4 col-xs-offset-3 col-sm-offset-2">
					<div class="input-group">
						<input name="wechatMessageTemplateParameters[<%-i%>].name" type="hidden" value="<%-templateParameter.name%>">
						<input class="tag-type" name="wechatMessageTemplateParameters[<%-i%>].type" type="hidden" value="TEXT">
						<span class="input-group-addon"><%-templateParameter.name%></span>
						<div class="tag-content">
							<input name="wechatMessageTemplateParameters[<%-i%>].value" class="form-control" type="text" maxlength="200">
						</div>
						<div class="input-group-btn">
							<button class="btn btn-default" type="button" data-toggle="dropdown">
								<span class="caret"></span>
							</button>
							<ul class="dropdown-menu">
								<%_.each(wechatMessageTemplateParameterTypes, function(wechatMessageTemplateParameterType, j) {%>
									<li class="<%-wechatMessageTemplateParameterType.value == 'TEXT' ? 'active' : ''%>">
										<a class="dropdown-item" href="javascript:;" data-tag="<%-wechatMessageTemplateParameterType.value%>" data-index="<%-i%>"><%-wechatMessageTemplateParameterType.name%></a>
									</li>
								<%});%>
							</ul>
						</div>
					</div>
				</div>
			</div>
		<%});%>
	</script>
	<script id="selectTemplate" type="text/template">
		<select name="wechatMessageTemplateParameters[<%-index%>].value" class="selectpicker form-control" data-size="10">
			<option value="">${message("common.choose")}</option>
			<%_.each(tagOptions, function(tagOption, i) {%>
				<option value="<%-tagOption.value%>"><%-tagOption.name%></option>
			<%});%>
		</select>
	</script>
	<script id="inputTemplate" type="text/template">
		<input name="wechatMessageTemplateParameters[<%-index%>].value" class="form-control" type="text" maxlength="200">
	</script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {
			
				var $wechatMessageTemplateForm = $("#wechatMessageTemplateForm");
				var $templateId = $("#templateId");
				var $option = $("#option");
				var optionTemplate = _.template($("#optionTemplate").html());
				var selectTemplate = _.template($("#selectTemplate").html());
				var inputTemplate = _.template($("#inputTemplate").html());
				
				var tagOptions = [
					[#list memberAttributes as memberAttribute]
						{
							name: "${message("WechatMessageTemplate.MemberAttribute." + memberAttribute)}",
							value: "${memberAttribute.tagName}"
						},
					[/#list]
					[#list orderShippingAttributes as orderShippingAttribute]
						{
							name: "${message("WechatMessageTemplate.OrderShippingAttribute." + orderShippingAttribute)}",
							value: "${orderShippingAttribute.tagName}"
						},
					[/#list]
					[#list orderRefundsAttributes as orderRefundsAttribute]
						{
							name: "${message("WechatMessageTemplate.OrderRefundsAttribute." + orderRefundsAttribute)}",
							value: "${orderRefundsAttribute.tagName}"
						},
					[/#list]
					[#list orderAttributes as orderAttribute]
						{
							name: "${message("WechatMessageTemplate.OrderAttribute." + orderAttribute)}",
							value: "${orderAttribute.tagName}"
						}
						[#if orderAttribute_has_next],[/#if]
					[/#list]
				];
				
				var wechatMessageTemplateParameterTypes = [
					[#list wechatMessageTemplateParameterTypes as wechatMessageTemplateParameterType]
						{
							name: "${message("WechatMessageTemplateParameter.Type." + wechatMessageTemplateParameterType)}",
							value: "${wechatMessageTemplateParameterType}"
						}
						[#if wechatMessageTemplateParameterType_has_next],[/#if]
					[/#list]
				];
				
				// 加载模版信息
				function loadTemplateInformation() {
					$.get("${base}/admin/wechat_message_template/load_template_information", {
						templateId: $templateId.val() === "" ? "${wechatMessageTemplate.templateId}" : $templateId.val()
					}).done(function(data) {
						$option.html(optionTemplate({
							data: data,
							tagOptions: tagOptions,
							wechatMessageTemplateParameterTypes: wechatMessageTemplateParameterTypes
						}));
						$(".selectpicker").selectpicker({liveSearch: true});
					});
				}
				
				loadTemplateInformation();
				
				$templateId.change(function() {
					loadTemplateInformation();
				});
				
				// 选择标签
				$option.on("click", "a.dropdown-item", function() {
					var $element = $(this);
					var $inputGroup = $element.closest("div.input-group");
					var tag = $element.data("tag");
					var index = $element.data("index");
					
					$element.closest("li").addClass("active").siblings().removeClass("active");
					$inputGroup.find(".tag-type").val(tag);
					if (tag === "SELECT") {
						$inputGroup.find(".tag-content").html(selectTemplate({
							tagOptions: tagOptions,
							index: index
						}));
						$(".selectpicker").selectpicker({liveSearch: true});
					} else {
						$inputGroup.find(".tag-content").html(inputTemplate({
							index: index
						}))
					}
				});
				
				// 表单验证
				$wechatMessageTemplateForm.validate({
					rules: {
						title: "required",
						type: "required",
						templateId: "required"
					}
				});
				
			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body class="admin">
	[#include "/admin/include/main_header.ftl" /]
	[#include "/admin/include/main_sidebar.ftl" /]
	<main>
		<div class="container-fluid">
			<ol class="breadcrumb">
				<li>
					<a href="${base}/admin/index">
						<i class="iconfont icon-homefill"></i>
						${message("common.breadcrumb.index")}
					</a>
				</li>
				<li class="active">${message("admin.wechatMessageTemplate.add")}</li>
			</ol>
			<form id="wechatMessageTemplateForm" class="ajax-form form-horizontal" action="${base}/admin/wechat_message_template/save" method="post">
				<div class="panel panel-default">
					<div class="panel-heading">${message("admin.wechatMessageTemplate.add")}</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required" for="title">${message("WechatMessageTemplate.title")}:</label>
							<div class="col-xs-9 col-sm-4">
								<input id="title" name="title" class="form-control" type="text" value="${messageTemplate.title}" maxlength="200">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("WechatMessageTemplate.messageConfig")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select name="type" class="selectpicker form-control" data-size="10">
									[#list types as type]
										<option value="${type}">${message("MessageConfig.Type." + type)}</option>
									[/#list]
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 col-sm-2 control-label item-required">${message("admin.wechatMessageTemplate.wechatTemplate")}:</label>
							<div class="col-xs-9 col-sm-4">
								<select id="templateId" name="templateId" class="selectpicker form-control" data-size="10">
									[#list templates as template]
										<option value="${template.template_id}">${template.title}</option>
									[/#list]
								</select>
							</div>
						</div>
						<div id="option"></div>
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