package com.fpt.ojt.securities.oauth2;

import com.fpt.ojt.securities.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Value("${app.oauth2.authorized-redirect-uris}")
    private String authorizedRedirectUri;

    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request,
                                        @NotNull HttpServletResponse response,
                                        @NotNull Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @NotNull
    protected String determineTargetUrl(@NotNull HttpServletRequest request,
                                        @NotNull HttpServletResponse response,
                                        Authentication authentication) {

        String targetUrl = authorizedRedirectUri;

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("No authentication provided");
        }

        // 1. Ép kiểu về Wrapper Class UserPrincipal
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // 2. Lấy UUID và Role từ Wrapper (Dữ liệu này lấy từ memory, không query DB)
        UUID userId = principal.getId();

        // Lấy role từ authorities
        String userRole = principal.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("CUSTOMER");

        // 3. Tạo Token
        String accessToken = tokenProvider.generateAccessTokenByUserId(userId, userRole);
        String refreshToken = tokenProvider.generateRefreshTokenByUserId(userId, userRole, false);

        log.info("OAuth2 login successful for user: {}, role: {}", userId, userRole);
        log.debug("Generated access token: {}", accessToken);
        log.debug("Generated refresh token: {}", refreshToken);

        String finalUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();

        log.info("Redirecting to: {}", finalUrl);

        return finalUrl;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}