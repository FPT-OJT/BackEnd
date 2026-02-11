package com.fpt.ojt.presentations.dtos.requests.card;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Request to add a card to user")
public class AddCardToUserRequest {
    @NotNull(message = "Card ID is required")
    @Schema(description = "UUID of the card to add", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID cardId;

    // @NotNull(message = "First payment date is required")
    @Schema(description = "First payment date for the card", example = "2024-03-15")
    private int firstPaymentDate;

    // @NotNull(message = "Expiry date is required")
    @Schema(description = "Card expiry date", example = "2029-12-31")
    private LocalDate expiryDate;
}