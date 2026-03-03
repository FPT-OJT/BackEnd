package com.fpt.ojt.services.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
public class MerchantAgencyWithAvailableDealsDto {
    private UUID merchantAgencyId;
    private String merchantAgencyName;
    private String mcc;
    private String imageUrl;
    private boolean isFavorite;
    private boolean isSubscribed;
    private Double lat;
    private Double lng;
    List<MerchantDealItem> merchantDealItems;
    private Double distanceMeters;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MerchantDealItem {
        private UUID dealId;
        private String dealName;
        private Double discountRate;
        private Double cashbackRate;
        private Double pointsMultiplier;
        private String description;
        private LocalDate validFrom;
        private LocalDate validTo;
    }
}
