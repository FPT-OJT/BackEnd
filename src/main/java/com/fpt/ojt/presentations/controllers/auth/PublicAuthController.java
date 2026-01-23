package com.fpt.ojt.presentations.controllers.auth;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.presentations.dtos.requests.password.ResetPasswordRequest;
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

import static com.fpt.ojt.constants.Constants.REFRESH_TOKEN_HEADER;

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
            @RequestBody @Validated final LoginRequest request,
            @Parameter(description = "Optional refresh token for session continuity", required = false)
            @RequestHeader(value = REFRESH_TOKEN_HEADER, required = false) final String refreshToken
    ) {
        return responseFactory.successSingle(
                authService.login(request, refreshToken), "Login successful");
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
    public ResponseEntity<SingleResponse<TokenResponse>> refreshToken(
            @Parameter(description = "Refresh token", required = true)
            @RequestHeader("X-Refresh-Token") final String refreshToken
    ) {
        TokenResponse newAccessToken = authService.getAccessTokenByRefreshToken(refreshToken);
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

    @PostMapping("/password/forgot")
    @Operation(summary = "Forgot Password", description = "Send OTP to email for password reset")
    public ResponseEntity<SingleResponse<Void>> forgotPassword(
            @Parameter(description = "Email to send OTP", required = true)
            @RequestBody @RequestParam String email
    ) {
        authService.initiatePasswordReset(email);
        return responseFactory.successSingle(null, "OTP has been sent to your email");
    }

    @PostMapping("/password/reset")
    @Operation(summary = "Reset Password", description = "Reset password using OTP")
    public ResponseEntity<SingleResponse<Void>> resetPassword(
            @Parameter(description = "Reset password request with email, OTP and new password", required = true)
            @RequestBody @Valid ResetPasswordRequest request
    ) {
        authService.resetPassword(request.email(), request.otp(), request.newPassword());
        return responseFactory.successSingle(null, "Password has been reset successfully");
    }
}
