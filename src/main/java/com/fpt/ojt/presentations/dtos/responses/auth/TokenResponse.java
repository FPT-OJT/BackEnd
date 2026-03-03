package com.fpt.ojt.presentations.dtos.responses.auth;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private UUID userId;
    private String role;
}
