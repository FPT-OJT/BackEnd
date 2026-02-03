package com.fpt.ojt.services.dtos;

import com.fpt.ojt.models.postgres.card.CardProduct;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardProductDto {
    private String cardName;

    private String cardType;

    private String imageUrl;

    private String cardCode;

    public static CardProductDto fromEntity(CardProduct cardProduct) {
        if (cardProduct == null) {
            return null;
        }
        return CardProductDto.builder()
                .cardName(cardProduct.getCardName())
                .cardType(cardProduct.getCardType())
                .imageUrl(cardProduct.getImageUrl())
                .cardCode(cardProduct.getCardCode())
                .build();
    }

}