package com.fpt.ojt.presentations.controllers;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.presentations.dtos.responses.auth.TokenResponse;
import com.fpt.ojt.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/auth")
@Tag(name = "Public Authentication", description = "Authentication API endpoints")
public class AuthPublicController extends AbstractBaseController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return access and refresh tokens")
    public ResponseEntity<SingleResponse<TokenResponse>> login(
            @Parameter(description = "Request body to login", required = true)
            @RequestBody @Validated final LoginRequest request
    ) {
        TokenResponse tokenResponse = authService.login(request);
        return responseFactory.successSingle(tokenResponse, "Login successful");
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
        String newAccessToken = authService.resetTokenByRefreshToken(refreshToken);
        return responseFactory.successSingle(newAccessToken, "Token refreshed successfully");
    }
}
