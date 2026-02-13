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
    public MerchantAgencyCardsDealsResponse getCardsWithDeals(UUID merchantAgencyId, UUID userId) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var agencyFuture = executor.submit(() -> getMerchantAgency(merchantAgencyId));
            var userCardsFuture = executor.submit(() -> getUserCards(userId));
            var merchantDealsFuture = executor.submit(() -> getMerchantDeals(merchantAgencyId));
            var cardRulesFuture = executor.submit(() -> getCardRules(userId));

            MerchantAgency agency = agencyFuture.get();
            List<UserCreditCard> userCards = userCardsFuture.get();
            List<MerchantDeal> merchantDeals = merchantDealsFuture.get();
            List<CardRule> cardRules = cardRulesFuture.get();

            String mcc = agency.getMerchant().getMcc();
            Map<UUID, List<CardRule>> cardRulesByProduct = groupCardRulesByProduct(cardRules);
            List<MerchantAgencyCardsDealsResponse.CardWithDeals> cardsWithDeals = processCardsWithDeals(
                    userCards, cardRulesByProduct, merchantDeals, mcc);

            return buildResponse(agency, cardsWithDeals);

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error fetching cards with deals for merchant agency: {}", merchantAgencyId, e);
            throw new RuntimeException("Error fetching cards with deals: " + e.getMessage(), e);
        }
    }


    @Cacheable(value = CacheNames.MERCHANT_AGENCY_CACHE_NAME, key = "#merchantAgencyId")
    private MerchantAgency getMerchantAgency(UUID merchantAgencyId) {
        return merchantAgencyRepository.findById(merchantAgencyId)
                .orElseThrow(() -> new NotFoundException("Merchant agency not found"));
    }

    @Cacheable(value = CacheNames.USER_CARDS_CACHE_NAME, key = "#userId")
    private List<UserCreditCard> getUserCards(UUID userId) {
        return userCreditCardRepository.findByUserIdAndDeletedAtIsNull(userId);
    }

    @Cacheable(value = CacheNames.MERCHANT_DEALS_CACHE_NAME, key = "#merchantAgencyId")
    private List<MerchantDeal> getMerchantDeals(UUID merchantAgencyId) {
        return merchantDealRepository.findAvailableByMerchantAgencyId(merchantAgencyId);
    }

    @Cacheable(value = CacheNames.CARD_RULES_CACHE_NAME, key = "#userId")
    private List<CardRule> getCardRules(UUID userId) {
        return cardRuleRepository.findAllAvailableByCardRulesWithConditionByUserId(userId);
    }


    private Map<UUID, List<CardRule>> groupCardRulesByProduct(List<CardRule> cardRules) {
        return cardRules.stream()
                .collect(Collectors.groupingBy(rule -> rule.getCardProduct().getId()));
    }

    private List<MerchantAgencyCardsDealsResponse.CardWithDeals> processCardsWithDeals(
            List<UserCreditCard> userCards,
            Map<UUID, List<CardRule>> cardRulesByProduct,
            List<MerchantDeal> merchantDeals,
            String mcc) {
        
        return userCards.stream()
                .map(userCard -> processCardWithDeals(userCard, cardRulesByProduct, merchantDeals, mcc))
                .toList();
    }

    private MerchantAgencyCardsDealsResponse.CardWithDeals processCardWithDeals(
            UserCreditCard userCard,
            Map<UUID, List<CardRule>> cardRulesByProduct,
            List<MerchantDeal> merchantDeals,
            String mcc) {
        
        UUID cardProductId = userCard.getCardProduct().getId();
        List<CardRule> rulesForThisCard = cardRulesByProduct.getOrDefault(cardProductId, List.of());

        List<MerchantAgencyCardsDealsResponse.DealItem> deals = new ArrayList<>();
        
        addMerchantDeals(deals, merchantDeals, cardProductId);
        
        addCardDeals(deals, rulesForThisCard, mcc);

        return buildCardWithDeals(userCard, cardProductId, deals);
    }

    private void addMerchantDeals(
            List<MerchantAgencyCardsDealsResponse.DealItem> deals,
            List<MerchantDeal> merchantDeals,
            UUID cardProductId) {
        
        for (MerchantDeal merchantDeal : merchantDeals) {
            if (isCardEligibleForDeal(merchantDeal.getId(), cardProductId)) {
                deals.add(buildMerchantDealItem(merchantDeal));
            }
        }
    }

    private void addCardDeals(
            List<MerchantAgencyCardsDealsResponse.DealItem> deals,
            List<CardRule> rulesForThisCard,
            String mcc) {
        
        for (CardRule rule : rulesForThisCard) {
            if (isMccMatching(mcc, rule.getMatchAllowMccs(), rule.getMatchRejectMccs())) {
                double benefit = calculateCardBenefit(rule);
                if (benefit > 0) {
                    deals.add(buildCardDealItem(rule));
                }
            }
        }
    }


    private MerchantAgencyCardsDealsResponse buildResponse(
            MerchantAgency agency,
            List<MerchantAgencyCardsDealsResponse.CardWithDeals> cardsWithDeals) {
        
        return MerchantAgencyCardsDealsResponse.builder()
                .merchantAgencyId(agency.getId())
                .merchantAgencyName(agency.getName())
                .imageUrl(agency.getImageUrl())
                .cards(cardsWithDeals)
                .build();
    }

    private MerchantAgencyCardsDealsResponse.CardWithDeals buildCardWithDeals(
            UserCreditCard userCard,
            UUID cardProductId,
            List<MerchantAgencyCardsDealsResponse.DealItem> deals) {
        
        return MerchantAgencyCardsDealsResponse.CardWithDeals.builder()
                .userCardId(userCard.getId())
                .cardProductId(cardProductId)
                .cardName(userCard.getCardProduct().getCardName())
                .cardImageUrl(userCard.getCardProduct().getImageUrl())
                .cardType(userCard.getCardProduct().getCardType())
                .deals(deals)
                .build();
    }

    private MerchantAgencyCardsDealsResponse.DealItem buildMerchantDealItem(MerchantDeal merchantDeal) {
        return MerchantAgencyCardsDealsResponse.DealItem.builder()
                .type(DealType.MERCHANT_DEAL)
                .dealId(merchantDeal.getId())
                .dealName(merchantDeal.getDealName())
                .discountRate(merchantDeal.getDiscountRate())
                .cashbackRate(merchantDeal.getCashbackRate())
                .pointsMultiplier(merchantDeal.getPointsMultiplier())
                .description(merchantDeal.getDescription())
                .validFrom(merchantDeal.getValidFrom())
                .validTo(merchantDeal.getValidTo())
                .build();
    }

    private MerchantAgencyCardsDealsResponse.DealItem buildCardDealItem(CardRule rule) {
        return MerchantAgencyCardsDealsResponse.DealItem.builder()
                .type(DealType.CARD_DEAL)
                .dealId(null)
                .dealName("Card Benefit")
                .discountRate(calculateDiscountRate(rule))
                .cashbackRate(rule.getEffectCashbackRate())
                .pointsMultiplier(null)
                .description(null)
                .validFrom(null)
                .validTo(null)
                .build();
    }


    private boolean isCardEligibleForDeal(UUID dealId, UUID cardProductId) {
        if (cardMerchantDealRepository.isDealAvailableForAllCards(dealId)) {
            return true;
        }
        return cardMerchantDealRepository.existsByDealAndCard(dealId, cardProductId);
    }

    private boolean isMccMatching(String mcc, List<String> allowMccs, List<String> rejectMccs) {
        if ((allowMccs == null || allowMccs.isEmpty()) && (rejectMccs == null || rejectMccs.isEmpty())) {
            return true;
        }

        if (rejectMccs != null && rejectMccs.contains(mcc)) {
            return false;
        }

        if (allowMccs != null && !allowMccs.isEmpty()) {
            return allowMccs.contains(mcc);
        }

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
