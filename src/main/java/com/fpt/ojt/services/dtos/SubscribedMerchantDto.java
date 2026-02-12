package com.fpt.ojt.services.dtos;

import java.util.UUID;

import com.fpt.ojt.models.postgres.merchant.Merchant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscribedMerchantDto {
    private String logoUrl;
    private UUID merchantId;

    public static SubscribedMerchantDto fromEntity(Merchant merchant) {
        return SubscribedMerchantDto.builder()
                .logoUrl(merchant.getLogoUrl())
                .merchantId(merchant.getId())
                .build();
    }
}
