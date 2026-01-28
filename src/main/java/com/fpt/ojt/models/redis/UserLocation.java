package com.fpt.ojt.models.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.UUID;

@RedisHash("user_location")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLocation {
    @Id
    private UUID userId;

    private Double longitude;
    private Double latitude;

    private Double lastLatitude;
    private Double lastLongitude;

    private LocalDateTime updatedAt;
}
