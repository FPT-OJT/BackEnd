package com.fpt.ojt.services.auth.impl;

import com.fpt.ojt.constants.Constants;
import com.fpt.ojt.exceptions.BadRequestException;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.exceptions.SuspiciousDetectedException;
import com.fpt.ojt.models.redis.PasswordResetToken;
import com.fpt.ojt.models.postgres.User;
import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.presentations.dtos.responses.auth.TokenResponse;
import com.fpt.ojt.repositories.PasswordResetTokenRepository;
import com.fpt.ojt.securities.JwtTokenProvider;
import com.fpt.ojt.securities.UserPrincipal;
import com.fpt.ojt.securities.dto.AccessTokenData;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.email.EmailService;
import com.fpt.ojt.services.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Value("${app.otp.expiration-minutes}")
    private long otpExpirationMinutes;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Override
    public UUID getCurrentUserId() {
        Object principal = getPrincipal();

        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).userId();
        }

        if (principal instanceof UUID) {
            return (UUID) principal;
        }

        throw new RuntimeException("Principal type is not supported or ID is null");
    }

    @Override
    public String getCurrentFamilyToken() {
        Object principal = getPrincipal();

        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).familyToken();
        }

        throw new RuntimeException("Family token not found in authentication context");
    }

    private Object getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getPrincipal();
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest, String refreshToken) {
        User user = userService.getUserByUserName(loginRequest.getUsername());
        UUID userId = user.getId();
        String userRole = user.getRole().toString();
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            String familyToken = null;
            if (refreshToken != null && !refreshToken.isBlank()) {
                jwtTokenProvider.revokeRefreshTokenByRefreshToken(refreshToken);
                Claims claims = jwtTokenProvider.getClaimsFromToken(refreshToken);
                familyToken = claims.get("family_token", String.class);
            } else {
                log.debug("No refresh token found in header, this might be new device, generate new family token");
            }

            Map<String, String> map = jwtTokenProvider.generateRefreshTokenByUserId(
                    userId,
                    familyToken,
                    userRole,
                    loginRequest.getRememberMe() != null ? loginRequest.getRememberMe() : false
            );

            String newRefreshToken = map.get("refresh_token");
            String newFamilyToken = map.get("family_token");

            String accessToken = jwtTokenProvider.generateAccessTokenByUserId(userId, newFamilyToken, userRole);

            return TokenResponse.builder()
                    .userId(userId)
                    .role(userRole)
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public TokenResponse getAccessTokenByRefreshToken(String refreshToken) {
        AccessTokenData tokenData = jwtTokenProvider.generateAccessTokenByRefreshToken(refreshToken);

        String newRefreshToken = jwtTokenProvider.generateRefreshTokenByUserId(
                tokenData.getUserId(),
                tokenData.getFamilyToken(),
                tokenData.getUserRole(),
                false
        ).get("refresh_token");

        return TokenResponse.builder()
                .role(tokenData.getUserRole())
                .accessToken(tokenData.getAccessToken())
                .userId(tokenData.getUserId())
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public TokenResponse register(RegisterRequest registerRequest) {
        String password = registerRequest.getPassword();
        String passwordHash = passwordEncoder.encode(password);
        userService.createUser(
                Constants.RoleEnum.CUSTOMER, // TODO: Assume that only customer need register
                null,
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordHash
        );

        return login(new LoginRequest(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                false
        ), null);
    }

    @Override
    public void logout() {
        String familyToken = getCurrentFamilyToken();
        jwtTokenProvider.revokeRefreshTokenByFamilyToken(familyToken);
    }

    @Override
    public TokenResponse loginWithGoogle(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                log.error("Invalid Google ID Token received");
                throw new BadRequestException("Invalid Google ID Token.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleId = payload.getSubject();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String pictureUrl = (String) payload.get("picture");

            log.info("Google login successful for email: {}, googleId: {}", email, googleId);

            User user = userService.handleUpdateGoogleCredential(
                    googleId, email, firstName, lastName, pictureUrl
            );

            Map<String, String> map = jwtTokenProvider.generateRefreshTokenByUserId(
                    user.getId(),
                    null,
                    user.getRole().getValue(),
                    false
            );
            String refreshToken = map.get("refresh_token");
            String familyToken = map.get("family_token");

            String accessToken = jwtTokenProvider.generateAccessTokenByUserId(
                    user.getId(),
                    familyToken,
                    user.getRole().getValue()
            );

            log.info("Generated tokens for user: {}", user.getId());

            return TokenResponse.builder()
                    .userId(user.getId())
                    .role(user.getRole().getValue())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            log.error("Google token verification failed", e);
            throw new BadRequestException("Invalid Google Token: " + e.getMessage());
        }
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        User user = userService.getUserByEmail(email);
        String otp = generateRandomOtp();

        PasswordResetToken token = PasswordResetToken.builder()
                .otp(otp)
                .userId(user.getId())
                .ttl(otpExpirationMinutes * 60)
                .isRevoked(false)
                .build();

        passwordResetTokenRepository.save(token);

        log.info("Password reset initiated for email: {}", email);

        emailService.sendOtpEmail(email, otp);
    }

    @Transactional
    public void resetPassword(String email, String otp, String newPassword) {
        User user = userService.getUserByEmail(email);

        PasswordResetToken token = passwordResetTokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Password reset token not found or expired"));

        if (!token.getOtp().equals(otp)) {
            throw new BadRequestException("Invalid OTP");
        }

        if (token.isRevoked()) {
            throw new SuspiciousDetectedException("Password reset token is revoked");
        }

        userService.updateNewPassword(
                user.getId(),
                passwordEncoder.encode(newPassword)
        );

        log.info("Password reset successful for email: {}", email);

        token.setRevoked(true);
        passwordResetTokenRepository.save(token);
    }

    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
