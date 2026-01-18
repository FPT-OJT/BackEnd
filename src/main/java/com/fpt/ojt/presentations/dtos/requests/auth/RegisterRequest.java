package com.fpt.ojt.presentations.dtos.requests.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotBlank(message = "Firstname must not be blank")
    private String firstName;

    @NotBlank(message = "Lastname must not be blank")
    private String lastName;

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotBlank(message = "Repeat password must not be blank")
    private String repeatPassword;

    @JsonIgnore
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching() {
        if (password == null) return false;
        return password.equals(repeatPassword);
    }
}
