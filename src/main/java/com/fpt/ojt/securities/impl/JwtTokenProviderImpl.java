package com.fpt.ojt.securities.impl;

import com.fpt.ojt.models.RefreshToken;
import com.fpt.ojt.repositories.RefreshTokenRepository;
import com.fpt.ojt.securities.JwtTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.secret}")
    private String jwtSecretKey;

    @Value("${app.jwt.token.expires-in}")
    private long jwtTokenExpiresIn;

    @Value("${app.jwt.refresh-token.expires-in}")
    private long jwtRefreshTokenExpiresIn;

    @Value("${app.jwt.refresh-token.expires-in-with-rememberme}")
    private long jwtRefreshTokenExpiresInWithRememberme;

    @Override
    public String generateAccessTokenByUserId(UUID userId, String userRole) {
        return generateTokenByUserId(userId, userRole, jwtTokenExpiresIn);
    }

    @Override
    public String generateRefreshTokenByUserId(UUID userId, String userRole, Boolean rememberMe) {

        long refreshTokenExpiresIn;
        if (rememberMe)
            refreshTokenExpiresIn = jwtRefreshTokenExpiresInWithRememberme;
        else
            refreshTokenExpiresIn = jwtRefreshTokenExpiresIn;

        String refreshToken = generateTokenByUserId(userId, userRole, refreshTokenExpiresIn);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .refreshToken(refreshToken)
                .userRole(userRole)
                .ttl(refreshTokenExpiresIn / 1000)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
        log.debug("Refresh token stored in Redis for userID: {}, ttl: {} seconds", userId, refreshTokenEntity.getTtl());

        return refreshToken;
    }

    @Override
    public String generateAccessTokenByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(storedToken -> {
                    if (validateToken(refreshToken, null)) {
                        log.debug("Generating new access token for userID: {}", storedToken.getUserId());
                        return generateAccessTokenByUserId(storedToken.getUserId(), storedToken.getUserRole());
                    }
                    log.warn("Refresh token is expired or invalid for userID: {}", storedToken.getUserId());
                    return null;
                })
                .orElseGet(() -> {
                    log.error("Refresh token not found in Redis");
                    return null;
                });
    }

    @Override
    public int getRefreshTokenMaxAge(boolean rememberMe) {
        return Math.toIntExact(rememberMe ? jwtRefreshTokenExpiresInWithRememberme : jwtRefreshTokenExpiresIn);
    }

    @Override
    public void deleteRefreshTokenByUserId(UUID userId) {
        try {
            refreshTokenRepository.findByUserId(userId)
                    .ifPresent(refreshTokenRepository::delete);
            log.debug("Deleted refresh token for userID: {}", userId);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to delete refresh token for userID: " + userId, exception);
        }
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
            if (request != null) {
                request.setAttribute("expired", e.getMessage());
            }
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            if (request != null) {
                request.setAttribute("malformed", e.getMessage());
            }
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            if (request != null) {
                request.setAttribute("signature", e.getMessage());
            }
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            if (request != null) {
                request.setAttribute("unsupported", e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            if (request != null) {
                request.setAttribute("empty", e.getMessage());
            }
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

        return bearer;
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
