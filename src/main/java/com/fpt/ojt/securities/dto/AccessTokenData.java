package com.fpt.ojt.securities.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AccessTokenData {
    private String accessToken;
    private UUID userId;
    private String userRole;
    private String familyToken;
}
