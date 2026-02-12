package com.fpt.ojt.services.dtos;

import java.util.UUID;

import com.fpt.ojt.models.postgres.merchant.Merchant;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscribedMerchantAgencyDto {
    private String logoUrl;
    private UUID agencyId;
    private String agencyName;
    private String merchantName;

    public static SubscribedMerchantAgencyDto fromEntity(MerchantAgency merchantAgency) {
        if (merchantAgency == null) {
            return null;
        }
        var builder = SubscribedMerchantAgencyDto.builder()
                .agencyId(merchantAgency.getId())
                .agencyName(merchantAgency.getName());
        if (merchantAgency.getMerchant() != null) {
            builder.merchantName(merchantAgency.getMerchant().getName());
            builder.logoUrl(merchantAgency.getMerchant().getLogoUrl());
        }
        return builder.build();
    }
}
