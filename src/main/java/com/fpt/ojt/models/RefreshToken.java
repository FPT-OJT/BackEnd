package com.fpt.ojt.models;

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
    private String id;

    @Indexed
    private UUID userId;

    @Indexed
    private String refreshToken;

    private String userRole;

    private boolean isRevoked;

    @TimeToLive
    private Long ttl;
}
