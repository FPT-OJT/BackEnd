package com.fpt.ojt.presentations.dtos.requests.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username/Email must not be blank")
    @Schema(description = "Username or Email", example = "john_smith")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Schema(description = "User password", example = "P@sswd123.")
    private String password;

    @Schema(description = "Remember me option", example = "true")
    private Boolean rememberMe;
}
