package com.fpt.ojt.securities.impl;

import com.fpt.ojt.exceptions.RefreshTokenExpiredException;
import com.fpt.ojt.exceptions.SuspiciousDetectedException;
import com.fpt.ojt.models.RefreshToken;
import com.fpt.ojt.repositories.RefreshTokenRepository;
import com.fpt.ojt.securities.JwtTokenProvider;
import com.fpt.ojt.securities.dto.AccessTokenData;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.fpt.ojt.constants.Constants.*;

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
    public String generateAccessTokenByUserId(UUID userId, String familyToken, String userRole) {
        return generateTokenByUserId(userId, userRole, familyToken, jwtTokenExpiresIn);
    }

    @Override
    public Map<String, String> generateRefreshTokenByUserId(UUID userId, String familyToken, String userRole, Boolean rememberMe) {

        // Check and revoke existing refresh token
        if (familyToken != null) {
            // Old device
            List<RefreshToken> refreshToken = refreshTokenRepository.findAllByFamilyToken(familyToken);
            for (RefreshToken rt : refreshToken) {
                if (!rt.isRevoked()) {
                    rt.setRevoked(true);
                }
            }
            refreshTokenRepository.saveAll(refreshToken);
        }

        long refreshTokenExpiresIn;
        if (rememberMe)
            refreshTokenExpiresIn = jwtRefreshTokenExpiresInWithRememberme;
        else
            refreshTokenExpiresIn = jwtRefreshTokenExpiresIn;

        String newFamilyToken = familyToken != null ? familyToken
                : UUID.randomUUID().toString();

        String refreshToken = generateTokenByUserId(userId, userRole, newFamilyToken, refreshTokenExpiresIn);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .familyToken(newFamilyToken)
                .userId(userId)
                .userRole(userRole)
                .ttl(refreshTokenExpiresIn / 1000)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
        log.debug("Refresh token stored in Redis for userID: {}, ttl: {} seconds", userId, refreshTokenEntity.getTtl());

        return Map.of("refresh_token", refreshToken, "family_token", newFamilyToken);
    }

    @Override
    public AccessTokenData generateAccessTokenByRefreshToken(String refreshToken) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenExpiredException("Refresh token has expired"));

        if (storedRefreshToken.isRevoked()) {
            revokeAllRefreshTokensByFamilyTokenInRefreshToken(refreshToken);
            throw new SuspiciousDetectedException("Refresh token is revoked");
        }

        storedRefreshToken.setRevoked(true);
        refreshTokenRepository.save(storedRefreshToken);

        UUID userId = storedRefreshToken.getUserId();
        String familyToken = storedRefreshToken.getFamilyToken();
        String userRole = storedRefreshToken.getUserRole();

        String accessToken = generateAccessTokenByUserId(userId, familyToken, userRole);

        return AccessTokenData.builder()
                .accessToken(accessToken)
                .userId(userId)
                .userRole(userRole)
                .familyToken(familyToken)
                .build();
    }

    private void revokeAllRefreshTokensByFamilyTokenInRefreshToken(String refreshToken) {
        Claims claims = getClaimsFromToken(refreshToken);
        String familyToken = claims.get("family_token").toString();
        revokeRefreshTokenByFamilyToken(familyToken);
    }

    @Override
    @Async
    public void revokeRefreshTokenByFamilyToken(String familyToken) {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByFamilyTokenAndIsRevoked(
                familyToken, false
        );

        for (RefreshToken rt : refreshTokens) {
            if (!rt.isRevoked()) {
                rt.setRevoked(true);
            }
        }
        CompletableFuture.completedFuture(true);
    }

    @Override
    public void revokeRefreshTokenByRefreshToken(String refreshToken) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenExpiredException("Refresh token has expired"));

        if (storedRefreshToken.isRevoked()) {
            throw new SuspiciousDetectedException("Refresh token is revoked");
        }

        storedRefreshToken.setRevoked(true);
        refreshTokenRepository.save(storedRefreshToken);
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
    public String extractAccessTokenFromHttpRequest(HttpServletRequest request) {
        return request.getHeader(ACCESS_TOKEN_HEADER);
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateTokenByUserId(UUID userId, String userRole, String familyToken, long expiryTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiryTime);

        SecretKey key = getSigningKey();

        var jwtBuilder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", "ROLE_" + userRole)
                .issuedAt(now)
                .expiration(expiryDate);

        if (familyToken != null) {
            jwtBuilder.claim("family_token", familyToken);
        }

        String token = jwtBuilder
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        log.trace("Token generated for userID: {}, familyToken: {}, ttl: {}ms",
                userId, familyToken != null ? "present" : "null", expiryTime);

        return token;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }
}
