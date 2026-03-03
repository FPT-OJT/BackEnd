package com.fpt.ojt.presentations.dtos.responses;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractBaseDataResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
