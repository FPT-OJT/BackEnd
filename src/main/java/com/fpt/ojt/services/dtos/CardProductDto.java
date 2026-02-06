package com.fpt.ojt.services.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardProductDto {
    private String cardName;

    private String cardType;

    private String imageUrl;

    private String cardCode;

}