package com.fpt.ojt.infrastructure.securities;

import java.util.UUID;

public record UserPrincipal(UUID userId, String familyToken) {
}
