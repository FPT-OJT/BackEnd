package com.fpt.ojt.securities;

import module java.base;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {
    String generateAccessTokenByUserId(UUID userId, String userRole);
    String generateRefreshTokenByUserId(UUID userId, String userRole, Boolean rememberMe);
    String generateAccessTokenByRefreshToken(String accessToken);
    boolean deleteRefreshTokenByUserId(UUID userId);
    boolean validateToken(String token, HttpServletRequest request) ;
    String extractJwtFromHttpRequest(final HttpServletRequest request);
    Claims getClaimsFromToken(String token);
}
