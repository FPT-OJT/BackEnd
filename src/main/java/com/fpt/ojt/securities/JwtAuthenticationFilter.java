package com.fpt.ojt.securities;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected final void doFilterInternal(@NotNull final HttpServletRequest request,
                                          @NotNull final HttpServletResponse response,
                                          @NotNull final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.extractJwtFromHttpRequest(request);
            if (token != null) {
                if (jwtTokenProvider.validateToken(token, request)) {
                    Claims claims = jwtTokenProvider.getClaimsFromToken(token);
                    UUID userId = UUID.fromString(claims.getSubject());

                    String userRole = claims.get("role").toString();

                    List<SimpleGrantedAuthority> authorities;
                    if (userRole != null) {
                        authorities = List.of(new SimpleGrantedAuthority(userRole));
                    } else {
                        authorities = List.of();
                    }

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userId, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
