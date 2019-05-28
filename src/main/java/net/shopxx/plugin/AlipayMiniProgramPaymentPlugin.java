/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: TiCPubKmlyQCc38ilymx/butUHoOY1bf
 */
package net.shopxx.plugin;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import net.shopxx.entity.PaymentTransaction;
import net.shopxx.util.SpringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * Plugin - 支付宝(小程序支付)
 *
 * @author SHOP++ Team
 * @version 6.1
 */
@Component("alipayMiniProgramPaymentPlugin")
public class AlipayMiniProgramPaymentPlugin extends PaymentPlugin {

	/**
	 * 网关URL
	 */
	private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

	@Override
	public String getName() {
		return "支付宝(小程序支付)";
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
		return "/admin/plugin/alipay_mini_program_payment/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/alipay_mini_program_payment/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/alipay_mini_program_payment/setting";
	}

	@Override
	public boolean supports(HttpServletRequest request) {
		Device device = DeviceUtils.getCurrentDevice(request);
		String userAgent = request.getHeader("USER-AGENT");
		return device != null && device.isMobile() && StringUtils.containsIgnoreCase(userAgent, "AlipayClient") && StringUtils.containsIgnoreCase(userAgent, "miniprogram");
	}

	@Override
	public void prePayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		modelAndView.addObject("payUrl", getPayUrl(paymentPlugin, paymentTransaction, extra));
		modelAndView.addObject("paymentTransactionSn", paymentTransaction.getSn());
		modelAndView.setViewName("shop/plugin/alipay_mini_program_payment/pre_pay");
	}

	@Override
	public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		String code = request.getParameter("code");
		if (StringUtils.isEmpty(code)) {
			modelAndView.setViewName("common/error/unprocessable_entity");
			return;
		}
		String buyerId = StringUtils.EMPTY;
		AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
		alipaySystemOauthTokenRequest.setCode(code);
		alipaySystemOauthTokenRequest.setGrantType("authorization_code");
		try {
			AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = getAlipayClient().execute(alipaySystemOauthTokenRequest);
			String userId = alipaySystemOauthTokenResponse.getUserId();
			if (StringUtils.isNotEmpty(userId)) {
				buyerId = userId;
			}
		} catch (AlipayApiException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		AlipayTradeCreateModel alipayTradeCreateModel = new AlipayTradeCreateModel();
		alipayTradeCreateModel.setOutTradeNo(paymentTransaction.getSn());
		alipayTradeCreateModel.setBuyerId(buyerId);
		alipayTradeCreateModel.setTotalAmount(String.valueOf(paymentTransaction.getAmount().setScale(2)));
		alipayTradeCreateModel.setSubject(StringUtils.abbreviate(paymentDescription.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 60));

		AlipayTradeCreateRequest alipayTradeCreateRequest = new AlipayTradeCreateRequest();
		alipayTradeCreateRequest.setReturnUrl(getPostPayUrl(paymentPlugin, paymentTransaction));
		alipayTradeCreateRequest.setNotifyUrl(getPostPayUrl(paymentPlugin, paymentTransaction));
		alipayTradeCreateRequest.setBizModel(alipayTradeCreateModel);
		try {
			modelAndView.addObject("postPayUrl", getPostPayUrl(paymentPlugin, paymentTransaction));
			modelAndView.addObject("tradeNo", getAlipayClient().execute(alipayTradeCreateRequest).getTradeNo());
			modelAndView.setViewName("shop/plugin/alipay_mini_program_payment/pay");
		} catch (AlipayApiException e) {
			modelAndView.addObject("errorMessage", SpringUtils.getMessage("admin.plugin.alipayMiniProgramPayment.paymentConfigurationError", getName()));
			modelAndView.setViewName("common/error/unprocessable_entity");
		}
	}

	@Override
	public void postPayHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, boolean isPaySuccess, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		if (StringUtils.equals(request.getParameter("notify_type"), "trade_status_sync")) {
			OutputStream outputStream = response.getOutputStream();
			IOUtils.write("success", outputStream, "UTF-8");
			outputStream.flush();
		} else {
			super.postPayHandle(paymentPlugin, paymentTransaction, paymentDescription, extra, isPaySuccess, request, response, modelAndView);
		}
	}

	@Override
	public boolean isPaySuccess(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		AlipayTradeQueryModel alipayTradeQueryModel = new AlipayTradeQueryModel();
		alipayTradeQueryModel.setOutTradeNo(paymentTransaction.getSn());
		alipayTradeQueryModel.setTradeNo(request.getParameter("trade_no"));

		AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
		alipayTradeQueryRequest.setBizModel(alipayTradeQueryModel);
		try {
			AlipayTradeQueryResponse alipayTradeQueryResponse = getAlipayClient().execute(alipayTradeQueryRequest);
			return alipayTradeQueryResponse.isSuccess() && (StringUtils.equalsIgnoreCase(alipayTradeQueryResponse.getTradeStatus(), "TRADE_SUCCESS") || StringUtils.equalsIgnoreCase(alipayTradeQueryResponse.getTradeStatus(), "TRADE_FINISHED"))
					&& paymentTransaction.getAmount().compareTo(new BigDecimal(alipayTradeQueryResponse.getTotalAmount())) == 0;
		} catch (AlipayApiException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
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
	 * 获取开发者应用私钥
	 *
	 * @return 开发者应用私钥
	 */
	private String getAppPrivateKey() {
		return getAttribute("appPrivateKey");
	}

	/**
	 * 获取支付宝公钥
	 *
	 * @return 支付宝公钥
	 */
	private String getAlipayPublicKey() {
		return getAttribute("alipayPublicKey");
	}

	/**
	 * 获取AlipayClient
	 *
	 * @return AlipayClient
	 */
	private AlipayClient getAlipayClient() {
		return new DefaultAlipayClient(SERVER_URL, getAppId(), getAppPrivateKey(), "json", "UTF-8", getAlipayPublicKey(), "RSA2");
	}

}