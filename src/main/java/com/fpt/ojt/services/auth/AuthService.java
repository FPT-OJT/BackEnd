package com.fpt.ojt.services.auth;

import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.presentations.dtos.responses.auth.TokenResponse;

import java.util.UUID;

public interface AuthService {
    UUID getCurrentUserId();
    TokenResponse login(LoginRequest loginRequest);
    String getAccessTokenByRefreshToken(String refreshToken);
    void register(RegisterRequest registerRequest);
    void logout();
}
