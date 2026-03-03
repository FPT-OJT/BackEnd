package com.fpt.ojt.infrastructure.securities.impl;

import static com.fpt.ojt.infrastructure.constants.Constants.ACCESS_TOKEN_HEADER;

import com.fpt.ojt.infrastructure.securities.JwtTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private final RSAPublicKey publicKey;

    public JwtTokenProviderImpl(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
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
}
