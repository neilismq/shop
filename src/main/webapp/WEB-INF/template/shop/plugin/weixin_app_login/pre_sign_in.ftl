<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="SHOP++ Team">
	<meta name="copyright" content="SHOP++">
	<title>${message("shop.login.signIn")} - Powered By SHOP++</title>
	<link href="${base}/favicon.ico" rel="icon">
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/lodash.js"></script>
	<script src="${base}/resources/common/js/URI.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
</head>
[#noautoesc]
	[#escape x as x?js_string]
		<script>
		$().ready(function() {
			var $document = $(document);

			function loginAuthorizeEventListener() {
				plus.oauth.getServices(function(options) {
					var loginService = _.find(options, {id: "weixin"});
					if (loginService == null) {
						plus.nativeUI.toast("${message('shop.login.configurationErrorLoginPlugin')}");
						$document.trigger("loggedFail.shopxx.user");
						setInterval(function() {
							$.redirect("${base}${redirectUrl}");
						}, 1000);
					}
					loginService.authorize(function(successOptions) {
						location.href = URI("${postSignInUrl}").setSearch(successOptions).setSearch("redirectUrl", "${redirectUrl}").toString();
					}, function(failOptions) {
                        $document.trigger("loggedFail.shopxx.user");
						if (failOptions.code === -2) {
                            $.redirect("${base}${redirectUrl}");
						} else {
							plus.nativeUI.toast("${message('shop.login.authorizationFailure')}");
							setInterval(function() {
								$.redirect("${base}${redirectUrl}");
							}, 1000);
						}
					});
				});
			}

			if (window.plus) {
				loginAuthorizeEventListener();
			} else {
				document.addEventListener("plusready", loginAuthorizeEventListener, false);
			}
		});
		</script>
	[/#escape]
[/#noautoesc]
<body>
</body>
</html>