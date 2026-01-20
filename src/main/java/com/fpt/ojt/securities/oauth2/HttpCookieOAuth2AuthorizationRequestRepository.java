package com.fpt.ojt.securities.oauth2;

import com.fpt.ojt.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    @Value("${app.oauth2.cookie.auth-request-name}")
    private String authRequestCookieName;

    @Value("${app.oauth2.cookie.redirect-uri-param-name}")
    private String redirectUriParamCookieName;

    @Value("${app.oauth2.cookie.expire-seconds}")
    private int cookieExpireSeconds;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtil.getCookie(request, authRequestCookieName)
                .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }
        CookieUtil.addCookie(response, authRequestCookieName, CookieUtil.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter(redirectUriParamCookieName);
        if (org.springframework.util.StringUtils.hasText(redirectUriAfterLogin)) {
            CookieUtil.addCookie(response, redirectUriParamCookieName, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, authRequestCookieName);
        CookieUtil.deleteCookie(request, response, redirectUriParamCookieName);
    }
}