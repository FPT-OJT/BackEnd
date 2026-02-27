package com.fpt.ojt.services.dtos;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MerchantDealDto {
    private UUID id;
    private UUID agencyId;
    private String merchantName;
    private String logoUrl;
    private String merchantDescription;
    private String agencyName;
    private String dealName;
    private Double discountRate;
    private Double cashbackRate;
    private Double pointsMultiplier;
    private String description;
}
