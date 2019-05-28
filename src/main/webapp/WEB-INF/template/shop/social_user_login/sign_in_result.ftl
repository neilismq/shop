<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="SHOP++ Team">
	<meta name="copyright" content="SHOP++">
	<title>${message("shop.socialUserLogin.signInResult")}[#if showPowered] - Powered By SHOP++[/#if]</title>
	<link href="${base}/favicon.ico" rel="icon">
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/URI.js"></script>
	<script src="${base}/resources/common/js/jweixin.miniProgram.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
			$().ready(function() {

				var $document = $(document);

				[#if socialUser??]
					[#if socialUser.user??]
						function loggedInHandle() {
							$document.trigger("loggedIn.shopxx.user", [{
								type: "member",
								username: "${socialUser.user.username?js_string}"
							}]);
							$.redirect("${base}${redirectUrl!memberLoginSuccessUrl}");
						}

						if (!$.isHtml5Plus() || window.plus) {
							loggedInHandle();
						} else {
							document.addEventListener("plusready", function () {
								loggedInHandle();
							});
						}
					[#else]
						$.redirect(URI("${base}${memberLoginUrl}").setSearch({
							socialUserId: "${socialUser.id}",
							uniqueId: "${socialUser.uniqueId}",
							redirectUrl: "${redirectUrl}"
						}).toString());
					[/#if]
				[/#if]

			});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body>
	[#if !socialUser??]
		<p>${message("shop.socialUserLogin.failure")}</p>
	[/#if]
</body>
</html>