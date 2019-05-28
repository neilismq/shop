/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: jODgsmJNalWGAT/9LqSt+dPT2fvH4fQW
 */
package net.shopxx.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopxx.CommonAttributes;
import net.shopxx.Setting;
import net.shopxx.entity.PaymentTransaction;
import net.shopxx.util.JsonUtils;
import net.shopxx.util.SystemUtils;
import net.shopxx.util.WebUtils;
import net.shopxx.util.XmlUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Plugin - 微信支付(小程序支付)
 *
 * @author SHOP++ Team
 * @version 6.1
 */
@Component("weixinMiniProgramPaymentPlugin")
public class WeixinMiniProgramPaymentPlugin extends PaymentPlugin {

	/**
	 * 微信小程序支付插件ID
	 */
	public static final String WEIXIN_MINI_PROGRAM_PAYMENT_PLUGIN_ID = "weixinMiniProgramPaymentPlugin";

	/**
	 * openId请求URL
	 */
	private static final String OPEN_ID_REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session";

	/**
	 * prepay_id请求URL
	 */
	private static final String PREPAY_ID_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 查询订单请求URL
	 */
	private static final String ORDER_QUERY_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

	@Override
	public String getName() {
		return "微信支付(小程序支付)";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "SHOP++";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.shopxx.net";
	}

	@Override
	public String getInstallUrl() {
		return "/admin/plugin/weixin_mini_program_payment/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/weixin_mini_program_payment/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/weixin_mini_program_payment/setting";
	}

	@Override
	public boolean supports(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT");
		String weixinMiniProgarm = request.getHeader(CommonAttributes.WEIXIN_MINI_PROGARM_HEADER_PARAMETER_NAME);
		return StringUtils.equalsIgnoreCase(weixinMiniProgarm, "true");
	}

	@Override
	public void prePayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("payUrl", getPayUrl(paymentPlugin, paymentTransaction, extra));
		parameterMap.put("appid", getAppId());
		parameterMap.put("secret", getAppSecret());
		modelAndView.addObject("parameterMap", parameterMap);
		modelAndView.setViewName("shop/plugin/weixin_mini_program_payment/pre_pay");
	}

	@Override
	public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		String code = request.getParameter("code");
		if (StringUtils.isEmpty(code)) {
			modelAndView.setViewName("common/error/unprocessable_entity");
			return;
		}

		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("appid", getAppId());
		parameterMap.put("mch_id", getMchId());
		parameterMap.put("nonce_str", DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		parameterMap.put("body", StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 600));
		parameterMap.put("out_trade_no", paymentTransaction.getSn());
		parameterMap.put("total_fee", String.valueOf(paymentTransaction.getAmount().multiply(new BigDecimal(100)).setScale(0)));
		parameterMap.put("spbill_create_ip", getRealIp(request));
		parameterMap.put("notify_url", getPostPayUrl(paymentPlugin, paymentTransaction));
		parameterMap.put("trade_type", "JSAPI");
		parameterMap.put("openid", getOpenId(code));
		parameterMap.put("sign", generateSign(parameterMap));

		String result = WebUtils.post(PREPAY_ID_REQUEST_URL, XmlUtils.toXml(parameterMap));
		Map<String, String> resultMap = XmlUtils.toObject(result, new TypeReference<Map<String, String>>() {
		});

		String prepayId = resultMap.get("prepay_id");
		String tradeType = resultMap.get("trade_type");
		String resultCode = resultMap.get("result_code");

		if (StringUtils.equals(tradeType, "JSAPI") && StringUtils.equals(resultCode, "SUCCESS")) {
			Map<String, Object> modelMap = new TreeMap<>();
			modelMap.put("appId", getAppId());
			modelMap.put("nonceStr", DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
			modelMap.put("package", "prepay_id=" + prepayId);
			modelMap.put("signType", "MD5");
			modelMap.put("timeStamp", new Date().getTime());
			modelMap.put("paySign", generateSign(modelMap));
			modelMap.put("postPayUrl", getPostPayUrl(paymentPlugin, paymentTransaction));
			modelMap.put("prepayId", prepayId);
			Setting setting = SystemUtils.getSetting();
			String rePayUrl = paymentTransaction.getRePayUrl();
			modelMap.put("return_url", StringUtils.isNotEmpty(rePayUrl) ? setting.getSiteUrl() + paymentTransaction.getRePayUrl() : setting.getSiteUrl());
			modelAndView.addObject("modelMap", modelMap);
			modelAndView.setViewName("shop/plugin/weixin_mini_program_payment/pay");
		} else {
			modelAndView.setViewName("common/error/unprocessable_entity");
			return;
		}
	}

	@Override
	public void postPayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, boolean isPaySuccess, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		String xml = IOUtils.toString(request.getInputStream(), "UTF-8");
		if (StringUtils.isNotEmpty(xml)) {
			Map<String, String> resultMap = XmlUtils.toObject(xml, new TypeReference<Map<String, String>>() {
			});
			if (StringUtils.equals(resultMap.get("return_code"), "SUCCESS")) {
				OutputStream outputStream = response.getOutputStream();
				IOUtils.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>", outputStream, "UTF-8");
				outputStream.flush();
			} else {
				super.postPayHandle(paymentPlugin, paymentTransaction, paymentDescription, extra, isPaySuccess, request, response, modelAndView);
			}
		} else {
			super.postPayHandle(paymentPlugin, paymentTransaction, paymentDescription, extra, isPaySuccess, request, response, modelAndView);
		}
	}

	@Override
	public boolean isPaySuccess(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("appid", getAppId());
		parameterMap.put("mch_id", getMchId());
		parameterMap.put("out_trade_no", paymentTransaction.getSn());
		parameterMap.put("nonce_str", DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		parameterMap.put("sign", generateSign(parameterMap));
		String result = WebUtils.post(ORDER_QUERY_REQUEST_URL, XmlUtils.toXml(parameterMap));
		Map<String, String> resultMap = XmlUtils.toObject(result, new TypeReference<Map<String, String>>() {
		});
		return StringUtils.equals(resultMap.get("return_code"), "SUCCESS") && StringUtils.equals(resultMap.get("result_code"), "SUCCESS") && StringUtils.equals(resultMap.get("trade_state"), "SUCCESS")
				&& paymentTransaction.getAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(resultMap.get("total_fee"))) == 0;
	}

	/**
	 * 获取AppID
	 *
	 * @return AppID
	 */
	private String getAppId() {
		return getAttribute("appId");
	}

	/**
	 * 获取AppSecret
	 *
	 * @return AppSecret
	 */
	private String getAppSecret() {
		return getAttribute("appSecret");
	}

	/**
	 * 获取商户号
	 *
	 * @return 商户号
	 */
	private String getMchId() {
		return getAttribute("mchId");
	}

	/**
	 * 获取API密钥
	 *
	 * @return API密钥
	 */
	private String getApiKey() {
		return getAttribute("apiKey");
	}

	/**
	 * 获取OpenID
	 *
	 * @param code
	 * 		code值
	 * @return OpenID
	 */
	private String getOpenId(String code) {
		Map<String, Object> parameterMap = new TreeMap<>();
		parameterMap.put("appid", getAppId());
		parameterMap.put("secret", getAppSecret());
		parameterMap.put("js_code", code);
		parameterMap.put("grant_type", "authorization_code");
		String result = WebUtils.post(OPEN_ID_REQUEST_URL, parameterMap);
		Map<String, String> resultMap = JsonUtils.toObject(result, new TypeReference<Map<String, String>>() {
		});
		String openId = resultMap.get("openid");
		return StringUtils.isNotEmpty(openId) ? openId : StringUtils.EMPTY;
	}

	/**
	 * 生成签名
	 *
	 * @param parameterMap
	 * 		参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
		return StringUtils.upperCase(DigestUtils.md5Hex(joinKeyValue(new TreeMap<>(parameterMap), null, "&key=" + getApiKey(), "&", true)));
	}

	/**
	 * 获取客户端真实IP
	 *
	 * @param request
	 * 		HttpServletRequest
	 * @return 客户端真实IP
	 */
	private String getRealIp(HttpServletRequest request) {
		String realIp = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(realIp) && !StringUtils.equalsIgnoreCase("unknown", realIp)) {
			String[] realIps = StringUtils.split(realIp, ",");
			if (ArrayUtils.isNotEmpty(realIps)) {
				return realIps[0];
			} else {
				return realIp;
			}
		}
		realIp = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(realIp) && !StringUtils.equalsIgnoreCase("unknown", realIp)) {
			return realIp;
		}
		return request.getRemoteAddr();
	}

}