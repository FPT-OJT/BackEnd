package com.fpt.ojt.infrastructure.securities;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {
    boolean validateToken(String token, HttpServletRequest request);

    String extractAccessTokenFromHttpRequest(final HttpServletRequest request);

    Claims getClaimsFromToken(String token);
}
