package com.fpt.ojt.presentations.dtos.requests.auth;

import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username/Email must not be blank")
    @Description("Username or Email")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    private Boolean rememberMe;
}
