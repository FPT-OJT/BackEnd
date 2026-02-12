package com.fpt.ojt.presentations.dtos.responses.merchant;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantAgencyCardsDealsResponse {

    private UUID merchantAgencyId;
    private String merchantAgencyName;
    private String imageUrl;
    private List<CardWithDeals> cards;

    public enum DealType {
        MERCHANT_DEAL,
        CARD_DEAL,
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardWithDeals {

        private UUID userCardId;
        private UUID cardProductId;
        private String cardName;
        private String cardImageUrl;
        private String cardType;
        private List<DealItem> deals;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealItem {

        private DealType type;
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
