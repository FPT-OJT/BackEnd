package com.fpt.ojt.services.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.fpt.ojt.models.postgres.card.UserCreditCard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCardDetailDto {

    private UUID userCardId;

    private LocalDate firstPaymentDate;

    private LocalDate expiryDate;

    private String cardName;

    private String cardType;

    private String cardImageUrl;

    public static UserCardDetailDto fromEntity(UserCreditCard userCreditCard) {
        if (userCreditCard == null) {
            return null;
        }
        UserCardDetailDto.UserCardDetailDtoBuilder builder = UserCardDetailDto.builder()
                .userCardId(userCreditCard.getId())
                .firstPaymentDate(userCreditCard.getFirstPaymentDate())
                .expiryDate(userCreditCard.getExpiryDate());
        if (userCreditCard.getCardProduct() != null) {
            builder.cardName(userCreditCard.getCardProduct().getCardName())
                    .cardType(userCreditCard.getCardProduct().getCardType())
                    .cardImageUrl(userCreditCard.getCardProduct().getImageUrl());
        }
        return builder.build();
    }
}
