package com.fpt.ojt.services.auth.impl;

import com.fpt.ojt.constants.Constants;
import com.fpt.ojt.models.User;
import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.securities.JwtTokenProvider;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UUID) {
            return (UUID) principal;
        }

        throw new RuntimeException("Principal type is not supported or ID is null");
    }

    @Override
    public Map<String, Object> login(LoginRequest loginRequest) {
        User user = userService.getUserByUserName(loginRequest.getUsername());
        UUID userId = user.getId();
        String userRole = user.getRole().toString();
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return Map.of(
                    "userId", userId,
                    "role", userRole,
                    "accessToken", jwtTokenProvider.generateAccessTokenByUserId(
                            userId, userRole
                    )
            );
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public String getAccessTokenByRefreshToken(String refreshToken) {
        return jwtTokenProvider.generateAccessTokenByRefreshToken(refreshToken);
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        String password = registerRequest.getPassword();
        String passwordHash = passwordEncoder.encode(password);
        userService.createUser(
                Constants.RoleEnum.CUSTOMER, // TODO: Assume that only customer need register
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getUsername(),
                passwordHash
        );
    }

    @Override
    public void logout() {
        try {
            jwtTokenProvider.deleteRefreshTokenByUserId(getCurrentUserId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to logout" + e.getMessage());
        }
    }
}
