package com.fpt.ojt.services.auth;

import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;

import java.util.Map;
import java.util.UUID;

public interface AuthService {
    UUID getCurrentUserId();
    Map<String, Object> login(LoginRequest loginRequest);
    String getAccessTokenByRefreshToken(String refreshToken);
    void register(RegisterRequest registerRequest);
    void logout();
}
