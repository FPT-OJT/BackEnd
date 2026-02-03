package com.fpt.ojt.services.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.fpt.ojt.models.postgres.card.UserCreditCard;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCardDto {

    private UUID id;

    private CardProductDto cardProduct;

    private LocalDate firstPaymentDate;

    private LocalDate expiryDate;

    public static UserCardDto fromEntity(UserCreditCard userCreditCard) {
        if (userCreditCard == null) {
            return null;
        }
        UserCardDto.UserCardDtoBuilder builder = UserCardDto.builder()
                .id(userCreditCard.getId())
                .firstPaymentDate(userCreditCard.getFirstPaymentDate())
                .expiryDate(userCreditCard.getExpiryDate());
        if (userCreditCard.getCardProduct() != null) {
            builder.cardProduct(CardProductDto.fromEntity(userCreditCard.getCardProduct()));
        }
        return builder.build();
    }
}
