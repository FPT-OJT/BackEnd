package com.fpt.ojt.infrastructure.securities.impl;

import static com.fpt.ojt.infrastructure.constants.Constants.ACCESS_TOKEN_HEADER;

import com.fpt.ojt.exceptions.RefreshTokenExpiredException;
import com.fpt.ojt.exceptions.SuspiciousDetectedException;
import com.fpt.ojt.infrastructure.securities.JwtTokenProvider;
import com.fpt.ojt.infrastructure.securities.dto.AccessTokenData;
import com.fpt.ojt.models.redis.RefreshToken;
import com.fpt.ojt.repositories.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Long> revokeTokenScript;
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtTokenProviderImpl(
            RefreshTokenRepository refreshTokenRepository,
            @Lazy RedisTemplate<String, Object> redisTemplate,
            @Lazy RedisScript<Long> revokeTokenScript,
            RSAPrivateKey privateKey,
            RSAPublicKey publicKey) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisTemplate = redisTemplate;
        this.revokeTokenScript = revokeTokenScript;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

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
    public Map<String, String> generateRefreshTokenByUserId(
            UUID userId, String familyToken, String userRole, Boolean rememberMe) {

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
        if (rememberMe) refreshTokenExpiresIn = jwtRefreshTokenExpiresInWithRememberme;
        else refreshTokenExpiresIn = jwtRefreshTokenExpiresIn;

        String newFamilyToken =
                familyToken != null ? familyToken : UUID.randomUUID().toString();

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
        RefreshToken storedRefreshToken = refreshTokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenExpiredException("Refresh token has expired"));

        String redisKey = "refresh_tokens:" + refreshToken;
        Long result = redisTemplate.execute(revokeTokenScript, Collections.singletonList(redisKey), "isRevoked", "0");

        if (result == null || result == -1) {
            throw new RefreshTokenExpiredException("Refresh token not found in Redis");
        }

        if (result == 0) {
            revokeAllRefreshTokensByFamilyTokenInRefreshToken(refreshToken);
            throw new SuspiciousDetectedException("Refresh token has already been used - potential replay attack");
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
    @Async("taskExecutor")
    public CompletableFuture<Void> revokeRefreshTokenByFamilyToken(String familyToken) {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByFamilyTokenAndIsRevoked(familyToken, false);

        for (RefreshToken rt : refreshTokens) {
            if (!rt.isRevoked()) {
                rt.setRevoked(true);
            }
        }

        refreshTokenRepository.saveAll(refreshTokens);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void revokeRefreshTokenByRefreshToken(String refreshToken) {
        RefreshToken storedRefreshToken = refreshTokenRepository
                .findByRefreshToken(refreshToken)
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
            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);
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
        String header = request.getHeader(ACCESS_TOKEN_HEADER);
        if (header != null && header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return header.substring(7);
        }
        return header;
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateTokenByUserId(UUID userId, String userRole, String familyToken, long expiryTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiryTime);

        var jwtBuilder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", "ROLE_" + userRole)
                .issuedAt(now)
                .expiration(expiryDate);

        if (familyToken != null) {
            jwtBuilder.claim("family_token", familyToken);
        }

        String token = jwtBuilder.signWith(privateKey, Jwts.SIG.RS256).compact();

        log.trace(
                "Token generated for userID: {}, familyToken: {}, ttl: {}ms",
                userId,
                familyToken != null ? "present" : "null",
                expiryTime);

        return token;
    }
}
