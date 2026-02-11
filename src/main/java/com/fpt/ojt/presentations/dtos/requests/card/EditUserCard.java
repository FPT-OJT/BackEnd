package com.fpt.ojt.presentations.dtos.requests.card;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditUserCard {

    @NotNull(message = "First payment date is required")
    private int firstPaymentDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

}
