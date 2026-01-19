package com.fpt.ojt.presentations.dtos.responses.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class TokenResponse {
    private String accessToken;
    private UUID userId;
    private String role;
}
