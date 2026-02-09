package com.fpt.ojt.services.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.fpt.ojt.models.postgres.card.UserCreditCard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCardDto {

    private UUID userCardId;

    private String cardImageUrl;

    private String cardName;

    public static UserCardDto fromEntity(UserCreditCard userCreditCard) {
        if (userCreditCard == null) {
            return null;
        }
        UserCardDto.UserCardDtoBuilder builder = UserCardDto.builder()
                .userCardId(userCreditCard.getId());
        if (userCreditCard.getCardProduct() != null) {
            builder.cardName(userCreditCard.getCardProduct().getCardName())
                 .cardImageUrl(userCreditCard.getCardProduct().getImageUrl());
        }
        return builder.build();
    }
}
