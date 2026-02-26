package com.fpt.ojt.infrastructure.securities.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessTokenData {
    private String accessToken;
    private UUID userId;
    private String userRole;
    private String familyToken;
}
