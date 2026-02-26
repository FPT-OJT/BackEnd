package com.fpt.ojt.presentations.dtos.requests.card;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class EditUserCard {

    @NotNull(message = "First payment date is required")
    private int firstPaymentDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;
}
