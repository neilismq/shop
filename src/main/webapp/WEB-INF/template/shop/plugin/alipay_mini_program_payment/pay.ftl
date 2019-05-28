<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="SHOP++ Team">
	<meta name="copyright" content="SHOP++">
	<title>${message("shop.payment.pay")} - Powered By SHOP++</title>
	<link href="${base}/favicon.ico" rel="icon">
	<!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/common/js/jquery.js"></script>
	<script src="${base}/resources/common/js/bootstrap.js"></script>
	<script src="${base}/resources/common/js/base.js"></script>
	<script src="${base}/resources/shop/js/base.js"></script>
</head>
[#noautoesc]
	[#escape x as x?js_string]
		<script>
			my.tradePay({
				tradeNO: "${tradeNo}",
				success: function(res) {
					if (res.resultCode === "9000") {
						location.href = "${postPayUrl}";
					} else {
						location.href = "${base}/";
					}
				},
				fail: function(res) {
					location.href = "${base}/";
				},
			});
		</script>
	[/#escape]
[/#noautoesc]
</html>