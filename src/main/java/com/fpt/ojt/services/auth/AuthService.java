package com.fpt.ojt.services.auth;

import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.presentations.dtos.responses.auth.TokenResponse;
import com.fpt.ojt.services.dtos.UserDto;

import java.util.UUID;

public interface AuthService {
    UUID getCurrentUserId();
    String getCurrentFamilyToken();
    TokenResponse login(LoginRequest loginRequest, String refreshToken);
    TokenResponse loginWithGoogle(String googleToken);
    TokenResponse getAccessTokenByRefreshToken(String refreshToken);
    TokenResponse register(RegisterRequest registerRequest);
    void resetPassword(String email, String otp, String newPassword);
    void initiatePasswordReset(String email);
    void logout();
    UserDto getCurrentUser();
}
