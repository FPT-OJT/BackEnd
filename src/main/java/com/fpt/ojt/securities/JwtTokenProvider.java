package com.fpt.ojt.securities;

import module java.base;

import com.fpt.ojt.securities.dto.AccessTokenData;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {
    String generateAccessTokenByUserId(UUID userId, String familyToken, String userRole);
    Map<String, String> generateRefreshTokenByUserId(UUID userId, String familyToken, String userRole, Boolean rememberMe);
    AccessTokenData generateAccessTokenByRefreshToken(String accessToken);
    void revokeRefreshTokenByFamilyToken(String familyToken);
    void revokeRefreshTokenByRefreshToken(String refreshToken);
    boolean validateToken(String token, HttpServletRequest request) ;
    String extractAccessTokenFromHttpRequest(final HttpServletRequest request);
    Claims getClaimsFromToken(String token);
}
