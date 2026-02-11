package com.fpt.ojt.presentations.dtos.responses.merchant;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MerchantAgencyCardsDealsResponse {
    private UUID merchantAgencyId;
    private String merchantAgencyName;
    private String imageUrl;
    private List<CardWithDeals> cards;

    public enum DealType {
        MERCHANT_DEAL,
        CARD_DEAL
    }
    @Data
    @Builder
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
