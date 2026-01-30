package com.fpt.ojt.services.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantOfferDto {
    private UUID merchantAgencyId;
    private String merchantAgencyName;
    private String mcc;
    private String imageUrl;
    private String merchantDealName;
    private boolean isFavorite;
    private boolean isSubscribed;
    private Double distance;
    private Double totalDiscount;
}