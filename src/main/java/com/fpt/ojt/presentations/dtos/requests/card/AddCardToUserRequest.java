package com.fpt.ojt.presentations.dtos.requests.card;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddCardToUserRequest {
    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @NotNull(message = "First payment date is required")
    private LocalDate firstPaymentDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

}