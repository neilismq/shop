<!DOCTYPE html>
<html>
<head>
	<meta charset="${requestCharset!"utf-8"}">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="SHOP++ Team">
	<meta name="copyright" content="SHOP++">
	<title>${message("shop.payment.prePay")} - Powered By SHOP++</title>
	<link href="${base}/favicon.ico" rel="icon">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/jweixin.miniProgram.js"></script>
	[#noautoesc]
		[#escape x as x?js_string]
			<script>
				$().ready(function() {
                    window.wx.miniProgram.navigateTo({
						url: "/pages/prepay/prepay?" + $.param({
							[#list parameterMap.entrySet() as entry]
								"${entry.key}": "${entry.value}"[#if entry_has_next],[/#if]
							[/#list]
						})
					});
				});
			</script>
		[/#escape]
	[/#noautoesc]
</head>
<body>
</body>
</html>