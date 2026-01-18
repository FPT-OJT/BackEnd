package com.fpt.ojt.securities.impl;

import com.fpt.ojt.securities.JwtTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static com.fpt.ojt.constants.Constants.TOKEN_HEADER;
import static com.fpt.ojt.constants.Constants.TOKEN_TYPE;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {
    @Value("{jwt.secret}")
    private String jwtSecretKey;

    @Value("{jwt.token.expires-in}")
    private long jwtTokenExpiresIn;

    @Value("{jwt.refresh-token.expires-in}")
    private long jwtRefreshTokenExpiresIn;

    @Override
    public String generateAccessTokenByUserId(UUID userId, String userRole) {
        return generateTokenByUserId(userId, userRole, jwtTokenExpiresIn);
    }

    @Override
    public String generateRefreshTokenByUserId(UUID userId, String userRole) {
        String refreshToken = generateTokenByUserId(userId, userRole, jwtRefreshTokenExpiresIn);
        // Store in Redis

        return refreshToken;
    }

    @Override
    public String generateRefreshTokenByAccessToken(String accessToken) {
        return "";
    }

    @Override
    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            request.setAttribute("expired", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            request.setAttribute("malformed", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            request.setAttribute("signature", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            request.setAttribute("unsupported", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            request.setAttribute("empty", e.getMessage());
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            // Else -> Unauthorized
        }

        return false;
    }

    @Override
    public String extractJwtFromHttpRequest(final HttpServletRequest request) {
        String bearer = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith(String.format("%s ", TOKEN_TYPE))) {
            return bearer.substring(TOKEN_TYPE.length() + 1);
        }

        return null;
    }

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        String userIdString = (String) principal;

        if (userIdString != null) {
            return UUID.fromString(userIdString);
        }

        throw new RuntimeException("Principal type is not supported or ID is null");
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateTokenByUserId(UUID userId, String userRole, long expiryTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiryTime);

        SecretKey key = getSigningKey();

        String token = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", "ROLE_" + userRole)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        log.trace("Token is added to the local cache for userID: {}, ttl: {}", userId, expiryTime);

        return token;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }
}
