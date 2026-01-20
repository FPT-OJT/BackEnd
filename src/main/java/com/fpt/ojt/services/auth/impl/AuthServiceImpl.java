package com.fpt.ojt.services.auth.impl;

import com.fpt.ojt.constants.Constants;
import com.fpt.ojt.exceptions.BadRequestException;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.models.PasswordResetToken;
import com.fpt.ojt.models.User;
import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.presentations.dtos.responses.auth.TokenResponse;
import com.fpt.ojt.repositories.PasswordResetTokenRepository;
import com.fpt.ojt.securities.JwtTokenProvider;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.email.EmailService;
import com.fpt.ojt.services.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
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
import java.util.Optional;
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
    public TokenResponse login(LoginRequest loginRequest) {
        User user = userService.getUserByUserName(loginRequest.getUsername());
        UUID userId = user.getId();
        String userRole = user.getRole().toString();
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return TokenResponse.builder()
                    .userId(userId)
                    .role(userRole)
                    .accessToken(jwtTokenProvider.generateAccessTokenByUserId(
                            userId, userRole
                    ))
                    .refreshToken(jwtTokenProvider.generateRefreshTokenByUserId(
                            userId, userRole, loginRequest.getRememberMe()
                    ))
                    .build();
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public String getAccessTokenByRefreshToken(String refreshToken) {
        return jwtTokenProvider.generateAccessTokenByRefreshToken(refreshToken);
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
        ));
    }

    @Override
    public void logout() {
        try {
            if (!jwtTokenProvider.deleteRefreshTokenByUserId(getCurrentUserId())) {

                // TODO: handleSuspiciousAccess();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to logout" + e.getMessage());
        }
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

            String accessToken = jwtTokenProvider.generateAccessTokenByUserId(
                    user.getId(),
                    user.getRole().getValue()
            );
            String refreshToken = jwtTokenProvider.generateRefreshTokenByUserId(
                    user.getId(),
                    user.getRole().getValue(),
                    false
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
        Optional<PasswordResetToken> oldToken = passwordResetTokenRepository.findByUserId(user.getId());
        oldToken.ifPresent(passwordResetTokenRepository::delete);

        PasswordResetToken token = PasswordResetToken.builder()
                .otp(otp)
                .userId(user.getId())
                .ttl(otpExpirationMinutes * 60)
                .build();

        passwordResetTokenRepository.save(token);

        log.info("Password reset initiated for email: {}", email);

        emailService.sendOtpEmail(email, otp);
    }

    @Transactional
    public void resetPassword(String email, String otp, String newPassword) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("Email not found");
        }

        PasswordResetToken token = passwordResetTokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));

        if (token == null) {
            throw new BadRequestException("OTP has expired");
        }

        if (!token.getOtp().equals(otp)) {
            throw new BadRequestException("Invalid OTP");
        }

        userService.updateNewPassword(
                user.getId(),
                passwordEncoder.encode(newPassword)
        );

        log.info("Password reset successful for email: {}", email);
        passwordResetTokenRepository.deleteAllByUserId(user.getId());
    }

    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
