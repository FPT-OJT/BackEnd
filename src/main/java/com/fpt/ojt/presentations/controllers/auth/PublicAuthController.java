package com.fpt.ojt.presentations.controllers.auth;

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
public class PublicAuthController extends AbstractBaseController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return access and refresh tokens")
    public ResponseEntity<SingleResponse<TokenResponse>> login(
            @Parameter(description = "Request body to login", required = true)
            @RequestBody @Validated final LoginRequest request
    ) {
        return responseFactory.successSingle(
                authService.login(request), "Login successful");
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register a new user account")
    public ResponseEntity<SingleResponse<TokenResponse>> register(
            @Parameter(description = "Request body to register", required = true)
            @RequestBody @Valid final RegisterRequest request
    ) {
        return responseFactory.sendSingle(authService.register(request), "Registration successful", HttpStatus.CREATED);
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

    @PostMapping("/google")
    @Operation(summary = "Google Login", description = "Authenticate with Google ID Token and return JWT tokens")
    public ResponseEntity<SingleResponse<TokenResponse>> loginWithGoogle(
            @Valid @RequestParam String googleToken
    ) {
        TokenResponse authResponse = authService.loginWithGoogle(googleToken);
        return responseFactory.successSingle(authResponse, "Google login successful");
    }
}
