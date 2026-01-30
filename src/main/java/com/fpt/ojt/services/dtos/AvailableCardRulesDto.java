package com.fpt.ojt.services.dtos;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AvailableCardRulesDto {
    UUID cardProductId;
    List<CardRulesDto> cardRules;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardRulesDto {
        List<String> allowMccs;
        List<String> rejectMccs;
        private Double effectCashbackRate;
        private Double effectRebateRate;
        private Double effectMerchantDiscountRate;
        private Double effectFeeRate;
    }
}
