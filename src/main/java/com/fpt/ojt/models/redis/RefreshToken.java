package com.fpt.ojt.models.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@RedisHash("refresh_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String refreshToken;

    @Indexed
    private String familyToken;

    @Indexed
    private UUID userId;

    private String userRole;

    private boolean isRevoked;

    @TimeToLive
    private Long ttl;
}
