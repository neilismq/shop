/*
 * Copyright 2008-2019 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: OoBVpWJ5RUhTLwlN5gOgaRYa+fRuq8pa
 */
package net.shopxx.plugin;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Plugin - 微信登录(APP登录)
 *
 * @author SHOP++ Team
 * @version 6.1
 */
@Component("weixinAppLoginPlugin")
public class WeixinAppLoginPlugin extends LoginPlugin {

	/**
	 * openId请求URL
	 */
	private static final String OPEN_ID_REQUEST_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

	@Override
	public String getName() {
		return "微信登录(APP登录)";
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
		return "/admin/plugin/weixin_app_login/install";
	}

	@Override
	public String getUninstallUrl() {
		return "/admin/plugin/weixin_app_login/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "/admin/plugin/weixin_app_login/setting";
	}

	@Override
	public boolean supports(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT");
		return StringUtils.containsIgnoreCase(userAgent, "html5plus");
	}

	@Override
	public void preSignInHandle(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		modelAndView.addObject("postSignInUrl", getPostSignInUrl(loginPlugin, null));
		HttpSession session = request.getSession();
		String redirectUrl = (String) session.getAttribute("redirectUrl");
		if (StringUtils.isEmpty(redirectUrl)) {
			SavedRequest savedRequest = org.apache.shiro.web.util.WebUtils.getAndClearSavedRequest(request);
			if (savedRequest != null) {
				redirectUrl = savedRequest.getRequestUrl();
			}
		}
		modelAndView.addObject("redirectUrl", redirectUrl);
		modelAndView.setViewName("/shop/plugin/weixin_app_login/pre_sign_in");
	}

	@Override
	public void signInHandle(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public boolean isSignInSuccess(LoginPlugin loginPlugin, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		if (StringUtils.isEmpty(code)) {
			return false;
		}

		try {
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			TokenRequestBuilder tokenRequestBuilder = OAuthClientRequest.tokenLocation(OPEN_ID_REQUEST_URL);
			tokenRequestBuilder.setParameter("appid", getAppId());
			tokenRequestBuilder.setParameter("secret", getAppSecret());
			tokenRequestBuilder.setCode(code);
			tokenRequestBuilder.setGrantType(GrantType.AUTHORIZATION_CODE);
			OAuthClientRequest accessTokenRequest = tokenRequestBuilder.buildQueryMessage();
			OAuthJSONAccessTokenResponse authJSONAccessTokenResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.GET);
			String unionid = authJSONAccessTokenResponse.getParam("unionid");
			if (StringUtils.isNotEmpty(unionid)) {
				request.setAttribute("unionid", unionid);
				return true;
			}
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (OAuthProblemException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public String getUniqueId(HttpServletRequest request) {
		return (String) request.getAttribute("unionid");
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

}