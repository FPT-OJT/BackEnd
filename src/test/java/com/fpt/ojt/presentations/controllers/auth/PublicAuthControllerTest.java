package com.fpt.ojt.presentations.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ojt.infrastructure.securities.JwtAuthenticationFilter;
import com.fpt.ojt.infrastructure.securities.JwtTokenProvider;
import com.fpt.ojt.presentations.controllers.base.ResponseFactory;
import com.fpt.ojt.presentations.dtos.requests.auth.LoginRequest;
import com.fpt.ojt.presentations.dtos.requests.auth.RegisterRequest;
import com.fpt.ojt.presentations.dtos.responses.auth.TokenResponse;
import com.fpt.ojt.services.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.fpt.ojt.infrastructure.constants.Constants.REFRESH_TOKEN_HEADER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PublicAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ResponseFactory.class)
class PublicAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        tokenResponse = TokenResponse.builder()
                .accessToken("test-access-token")
                .refreshToken("test-refresh-token")
                .userId(UUID.randomUUID())
                .role("USER")
                .build();
    }

    @Nested
    @DisplayName("POST /public/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Should return tokens when login is successful")
        void login_Success() throws Exception {
            LoginRequest request = new LoginRequest("testuser", "password123", false);
            when(authService.login(any(LoginRequest.class), eq(null))).thenReturn(tokenResponse);

            mockMvc.perform(post("/public/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("Login successful"))
                    .andExpect(jsonPath("$.data.accessToken").value("test-access-token"))
                    .andExpect(jsonPath("$.data.refreshToken").value("test-refresh-token"));

            verify(authService, times(1)).login(any(LoginRequest.class), eq(null));
        }

        @Test
        @DisplayName("Should return tokens when login with refresh token header")
        void login_WithRefreshToken_Success() throws Exception {
            LoginRequest request = new LoginRequest("testuser", "password123", true);
            String existingRefreshToken = "existing-refresh-token";
            when(authService.login(any(LoginRequest.class), eq(existingRefreshToken))).thenReturn(tokenResponse);

            mockMvc.perform(post("/public/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(REFRESH_TOKEN_HEADER, existingRefreshToken)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("Login successful"));

            verify(authService, times(1)).login(any(LoginRequest.class), eq(existingRefreshToken));
        }

        @Test
        @DisplayName("Should return 422 when username is blank")
        void login_BlankUsername_UnprocessableEntity() throws Exception {
            String requestJson = """
                    {
                        "username": "",
                        "password": "password123",
                        "rememberMe": false
                    }
                    """;

            mockMvc.perform(post("/public/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.statusCode").value(422))
                    .andExpect(jsonPath("$.message").value("Validation error"))
                    .andExpect(jsonPath("$.items.username").exists());

            verify(authService, never()).login(any(), any());
        }

        @Test
        @DisplayName("Should return 422 when password is blank")
        void login_BlankPassword_UnprocessableEntity() throws Exception {
            String requestJson = """
                    {
                        "username": "testuser",
                        "password": "",
                        "rememberMe": false
                    }
                    """;

            mockMvc.perform(post("/public/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.statusCode").value(422))
                    .andExpect(jsonPath("$.message").value("Validation error"))
                    .andExpect(jsonPath("$.items.password").exists());

            verify(authService, never()).login(any(), any());
        }
    }

    @Nested
    @DisplayName("POST /public/auth/register")
    class RegisterTests {

        @Test
        @DisplayName("Should return 201 when registration is successful")
        void register_Success() throws Exception {
            String requestJson = """
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "username": "johndoe",
                        "email": "john@example.com",
                        "password": "password123",
                        "repeatPassword": "password123"
                    }
                    """;
            when(authService.register(any(RegisterRequest.class))).thenReturn(tokenResponse);

            mockMvc.perform(post("/public/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value(201))
                    .andExpect(jsonPath("$.message").value("Registration successful"))
                    .andExpect(jsonPath("$.data.accessToken").value("test-access-token"));

            verify(authService, times(1)).register(any(RegisterRequest.class));
        }

        @Test
        @DisplayName("Should return 422 when passwords do not match")
        void register_PasswordMismatch_UnprocessableEntity() throws Exception {
            String requestJson = """
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "username": "johndoe",
                        "email": "john@example.com",
                        "password": "password123",
                        "repeatPassword": "differentpassword"
                    }
                    """;

            mockMvc.perform(post("/public/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.statusCode").value(422))
                    .andExpect(jsonPath("$.message").value("Validation error"))
                    .andExpect(jsonPath("$.items.passwordMatching").exists());

            verify(authService, never()).register(any());
        }

        @Test
        @DisplayName("Should return 422 when required fields are blank")
        void register_BlankFields_UnprocessableEntity() throws Exception {
            String requestJson = """
                    {
                        "firstName": "",
                        "lastName": "",
                        "username": "",
                        "email": "",
                        "password": "",
                        "repeatPassword": ""
                    }
                    """;

            mockMvc.perform(post("/public/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.statusCode").value(422))
                    .andExpect(jsonPath("$.message").value("Validation error"));

            verify(authService, never()).register(any());
        }
    }

    @Nested
    @DisplayName("POST /public/auth/refresh")
    class RefreshTokenTests {

        @Test
        @DisplayName("Should return new tokens when refresh token is valid")
        void refreshToken_Success() throws Exception {
            String refreshToken = "valid-refresh-token";
            when(authService.getAccessTokenByRefreshToken(refreshToken)).thenReturn(tokenResponse);

            mockMvc.perform(post("/public/auth/refresh")
                            .header("X-Refresh-Token", refreshToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("Token refreshed successfully"))
                    .andExpect(jsonPath("$.data.accessToken").value("test-access-token"));

            verify(authService, times(1)).getAccessTokenByRefreshToken(refreshToken);
        }

        @Test
        @DisplayName("Should return 400 when refresh token header is missing")
        void refreshToken_MissingHeader_BadRequest() throws Exception {
            mockMvc.perform(post("/public/auth/refresh"))
                    .andExpect(status().isBadRequest());

            verify(authService, never()).getAccessTokenByRefreshToken(any());
        }
    }

    @Nested
    @DisplayName("POST /public/auth/google")
    class GoogleLoginTests {

        @Test
        @DisplayName("Should return tokens when Google login is successful")
        void loginWithGoogle_Success() throws Exception {
            String googleToken = "valid-google-token";
            when(authService.loginWithGoogle(googleToken)).thenReturn(tokenResponse);

            mockMvc.perform(post("/public/auth/google")
                            .param("googleToken", googleToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("Google login successful"))
                    .andExpect(jsonPath("$.data.accessToken").value("test-access-token"));

            verify(authService, times(1)).loginWithGoogle(googleToken);
        }

        @Test
        @DisplayName("Should return 400 when Google token is missing")
        void loginWithGoogle_MissingToken_BadRequest() throws Exception {
            mockMvc.perform(post("/public/auth/google"))
                    .andExpect(status().isBadRequest());

            verify(authService, never()).loginWithGoogle(any());
        }
    }

    @Nested
    @DisplayName("POST /public/auth/password/forgot")
    class ForgotPasswordTests {

        @Test
        @DisplayName("Should return success when OTP is sent")
        void forgotPassword_Success() throws Exception {
            String email = "test@example.com";
            doNothing().when(authService).initiatePasswordReset(email);

            mockMvc.perform(post("/public/auth/password/forgot")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("email", email))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("OTP has been sent to your email"));

            verify(authService, times(1)).initiatePasswordReset(email);
        }
    }

    @Nested
    @DisplayName("POST /public/auth/password/reset")
    class ResetPasswordTests {

        @Test
        @DisplayName("Should return success when password is reset")
        void resetPassword_Success() throws Exception {
            String requestJson = """
                    {
                        "email": "test@example.com",
                        "otp": "123456",
                        "newPassword": "newpassword123"
                    }
                    """;
            doNothing().when(authService).resetPassword(any(), any(), any());

            mockMvc.perform(post("/public/auth/password/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("Password has been reset successfully"));

            verify(authService, times(1)).resetPassword(eq("test@example.com"), eq("123456"), eq("newpassword123"));
        }

        @Test
        @DisplayName("Should return 422 when email is invalid")
        void resetPassword_InvalidEmail_UnprocessableEntity() throws Exception {
            String requestJson = """
                    {
                        "email": "invalid-email",
                        "otp": "123456",
                        "newPassword": "newpassword123"
                    }
                    """;

            mockMvc.perform(post("/public/auth/password/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.statusCode").value(422))
                    .andExpect(jsonPath("$.message").value("Validation error"))
                    .andExpect(jsonPath("$.items.email").exists());

            verify(authService, never()).resetPassword(any(), any(), any());
        }

        @Test
        @DisplayName("Should return 422 when OTP is not 6 digits")
        void resetPassword_InvalidOtp_UnprocessableEntity() throws Exception {
            String requestJson = """
                    {
                        "email": "test@example.com",
                        "otp": "12345",
                        "newPassword": "newpassword123"
                    }
                    """;

            mockMvc.perform(post("/public/auth/password/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.statusCode").value(422))
                    .andExpect(jsonPath("$.message").value("Validation error"))
                    .andExpect(jsonPath("$.items.otp").exists());

            verify(authService, never()).resetPassword(any(), any(), any());
        }

        @Test
        @DisplayName("Should return 422 when password is too short")
        void resetPassword_ShortPassword_UnprocessableEntity() throws Exception {
            String requestJson = """
                    {
                        "email": "test@example.com",
                        "otp": "123456",
                        "newPassword": "short"
                    }
                    """;

            mockMvc.perform(post("/public/auth/password/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.statusCode").value(422))
                    .andExpect(jsonPath("$.message").value("Validation error"))
                    .andExpect(jsonPath("$.items.newPassword").exists());

            verify(authService, never()).resetPassword(any(), any(), any());
        }
    }
}
