package com.fpt.ojt.presentations.controllers;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.presentations.dtos.responses.auth.TokenResponse;
import com.fpt.ojt.securities.JwtTokenProvider;
import com.fpt.ojt.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/auth")
@Tag(name = "Public Authentication", description = "Authentication API endpoints")
public class AuthPublicController extends AbstractBaseController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return access and refresh tokens")
    public ResponseEntity<SingleResponse<TokenResponse>> login(
            @Parameter(description = "Request body to login", required = true)
            @RequestBody @Validated final LoginRequest request
    ) {
        Map<String, Object> loginResult = authService.login(request);

        UUID userId = (UUID) loginResult.get("userId");
        String userRole = (String) loginResult.get("role");
        String accessToken = (String) loginResult.get("accessToken");

        TokenResponse tokenResponse = TokenResponse.builder()
                .userId(userId)
                .role(userRole)
                .accessToken(accessToken)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", jwtTokenProvider.generateRefreshTokenByUserId(
                        userId, userRole, request.getRememberMe()
                ))
                .httpOnly(true)
                .secure(true)
                .path("/public/auth/refresh")
                .maxAge(jwtTokenProvider.getRefreshTokenMaxAge(
                        request.getRememberMe()
                ))
                .sameSite("Strict")
                .build();

        return responseFactory.successSingleWithCookie(tokenResponse, "Login successful", refreshCookie);
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register a new user account")
    public ResponseEntity<SingleResponse<Void>> register(
            @Parameter(description = "Request body to register", required = true)
            @RequestBody @Valid final RegisterRequest request
    ) {
        authService.register(request);
        return responseFactory.sendSingle(null, "Registration successful", HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Generate new access token using refresh token")
    public ResponseEntity<SingleResponse<String>> refreshToken(
            @Parameter(description = "Refresh token", required = true)
            @RequestHeader("X-Refresh-Token") final String refreshToken
    ) {
        String newAccessToken = authService.getAccessTokenByRefreshToken(refreshToken);
        return responseFactory.successSingle(newAccessToken, "Token refreshed successfully");
    }
}
