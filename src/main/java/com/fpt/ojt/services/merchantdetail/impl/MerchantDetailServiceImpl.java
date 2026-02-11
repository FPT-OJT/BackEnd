package com.fpt.ojt.services.merchantdetail.impl;

import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.infrastructure.configs.CacheNames;
import com.fpt.ojt.models.postgres.card.CardRule;
import com.fpt.ojt.models.postgres.card.UserCreditCard;
import com.fpt.ojt.models.postgres.deal.MerchantDeal;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.presentations.dtos.responses.merchant.MerchantAgencyCardsDealsResponse;
import com.fpt.ojt.presentations.dtos.responses.merchant.MerchantAgencyCardsDealsResponse.DealType;
import com.fpt.ojt.repositories.card.CardRuleRepository;
import com.fpt.ojt.repositories.card.UserCreditCardRepository;
import com.fpt.ojt.repositories.deal.CardMerchantDealRepository;
import com.fpt.ojt.repositories.deal.MerchantDealRepository;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.services.merchantdetail.MerchantDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantDetailServiceImpl implements MerchantDetailService {
    private final MerchantAgencyRepository merchantAgencyRepository;
    private final UserCreditCardRepository userCreditCardRepository;
    private final MerchantDealRepository merchantDealRepository;
    private final CardRuleRepository cardRuleRepository;
    private final CardMerchantDealRepository cardMerchantDealRepository;

    @Override
    @Cacheable(value = CacheNames.MERCHANT_DETAIL_CACHE_NAME, key = "#merchantAgencyId + '-' + #userId")
    public MerchantAgencyCardsDealsResponse getCardsWithDeals(UUID merchantAgencyId, UUID userId) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Parallel fetch all required data
            var agencyFuture = executor.submit(() -> merchantAgencyRepository.findById(merchantAgencyId)
                    .orElseThrow(() -> new NotFoundException("Merchant agency not found")));

            var userCardsFuture = executor
                    .submit(() -> userCreditCardRepository.findByUserIdAndDeletedAtIsNull(userId));

            var merchantDealsFuture = executor
                    .submit(() -> merchantDealRepository.findAvailableByMerchantAgencyId(merchantAgencyId));

            var cardRulesFuture = executor
                    .submit(() -> cardRuleRepository.findAllAvailableByCardRulesWithConditionByUserId(userId));

            // Wait for results
            MerchantAgency agency = agencyFuture.get();
            List<UserCreditCard> userCards = userCardsFuture.get();
            List<MerchantDeal> merchantDeals = merchantDealsFuture.get();
            List<CardRule> cardRules = cardRulesFuture.get();

            String mcc = agency.getMerchant().getMcc();

            // Group card rules by cardProductId
            Map<UUID, List<CardRule>> cardRulesByProduct = cardRules.stream()
                    .collect(Collectors.groupingBy(rule -> rule.getCardProduct().getId()));

            // Process each user card
            List<MerchantAgencyCardsDealsResponse.CardWithDeals> cardsWithDeals = userCards.stream()
                    .map(userCard -> {
                        UUID cardProductId = userCard.getCardProduct().getId();
                        List<CardRule> rulesForThisCard = cardRulesByProduct.getOrDefault(cardProductId, List.of());

                        List<MerchantAgencyCardsDealsResponse.DealItem> deals = new ArrayList<>();

                        // 1. Add merchant deals
                        for (MerchantDeal merchantDeal : merchantDeals) {
                            if (isCardEligibleForDeal(merchantDeal.getId(), cardProductId)) {
                                deals.add(MerchantAgencyCardsDealsResponse.DealItem.builder()
                                        .type(DealType.MERCHANT_DEAL)
                                        .dealId(merchantDeal.getId())
                                        .dealName(merchantDeal.getDealName())
                                        .discountRate(merchantDeal.getDiscountRate())
                                        .cashbackRate(merchantDeal.getCashbackRate())
                                        .pointsMultiplier(merchantDeal.getPointsMultiplier())
                                        .description(merchantDeal.getDescription())
                                        .validFrom(merchantDeal.getValidFrom())
                                        .validTo(merchantDeal.getValidTo())
                                        .build());
                            }
                        }

                        // 2. Add card deals
                        for (CardRule rule : rulesForThisCard) {
                            if (isMccMatching(mcc, rule.getMatchAllowMccs(), rule.getMatchRejectMccs())) {
                                double benefit = calculateCardBenefit(rule);
                                if (benefit > 0) {
                                    deals.add(MerchantAgencyCardsDealsResponse.DealItem.builder()
                                            .type(DealType.CARD_DEAL)
                                            .dealId(null)
                                            .dealName("Card Benefit")
                                            .discountRate(calculateDiscountRate(rule))
                                            .cashbackRate(rule.getEffectCashbackRate())
                                            .pointsMultiplier(null)
                                            .description(null)
                                            .validFrom(null)
                                            .validTo(null)
                                            .build());
                                }
                            }
                        }

                        return MerchantAgencyCardsDealsResponse.CardWithDeals.builder()
                                .userCardId(userCard.getId())
                                .cardProductId(cardProductId)
                                .cardName(userCard.getCardProduct().getCardName())
                                .cardImageUrl(userCard.getCardProduct().getImageUrl())
                                .cardType(userCard.getCardProduct().getCardType())
                                .deals(deals)
                                .build();
                    })
                    .toList();

            return MerchantAgencyCardsDealsResponse.builder()
                    .merchantAgencyId(agency.getId())
                    .merchantAgencyName(agency.getName())
                    .imageUrl(agency.getImageUrl())
                    .cards(cardsWithDeals)
                    .build();

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error fetching cards with deals for merchant agency: {}", merchantAgencyId, e);
            throw new RuntimeException("Error fetching cards with deals: " + e.getMessage(), e);
        }
    }

    private boolean isCardEligibleForDeal(UUID dealId, UUID cardProductId) {
        // If deal is available for all cards, return true
        if (cardMerchantDealRepository.isDealAvailableForAllCards(dealId)) {
            return true;
        }
        // Otherwise check if this specific card is linked to the deal
        return cardMerchantDealRepository.existsByDealAndCard(dealId, cardProductId);
    }

    private boolean isMccMatching(String mcc, List<String> allowMccs, List<String> rejectMccs) {
        // If both lists are null or empty, match all
        if ((allowMccs == null || allowMccs.isEmpty()) && (rejectMccs == null || rejectMccs.isEmpty())) {
            return true;
        }

        // If MCC is in reject list, don't match
        if (rejectMccs != null && rejectMccs.contains(mcc)) {
            return false;
        }

        // If allowMccs is specified and not empty, only match if MCC is in the list
        if (allowMccs != null && !allowMccs.isEmpty()) {
            return allowMccs.contains(mcc);
        }

        // Otherwise match
        return true;
    }

    private double calculateCardBenefit(CardRule rule) {
        double rebateRate = rule.getEffectRebateRate() != null ? rule.getEffectRebateRate() : 0.0;
        double merchantDiscountRate = rule.getEffectMerchantDiscountRate() != null
                ? rule.getEffectMerchantDiscountRate()
                : 0.0;
        double feeRate = rule.getEffectFeeRate() != null ? rule.getEffectFeeRate() : 0.0;
        double cashbackRate = rule.getEffectCashbackRate() != null ? rule.getEffectCashbackRate() : 0.0;

        double discountRate = rebateRate + merchantDiscountRate - feeRate;
        return discountRate + cashbackRate * (1 - discountRate / 100);
    }

    private Double calculateDiscountRate(CardRule rule) {
        double rebateRate = rule.getEffectRebateRate() != null ? rule.getEffectRebateRate() : 0.0;
        double merchantDiscountRate = rule.getEffectMerchantDiscountRate() != null
                ? rule.getEffectMerchantDiscountRate()
                : 0.0;
        double feeRate = rule.getEffectFeeRate() != null ? rule.getEffectFeeRate() : 0.0;
        return rebateRate + merchantDiscountRate - feeRate;
    }
}
