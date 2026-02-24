package com.fpt.ojt.services.deal.impl;

import com.fpt.ojt.exceptions.QueryErrorException;
import com.fpt.ojt.models.postgres.deal.MerchantDeal;
import com.fpt.ojt.models.postgres.merchant.Merchant;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.merchant.MerchantDealFlatProjection;
import com.fpt.ojt.repositories.deal.CardMerchantDealRepository;
import com.fpt.ojt.repositories.deal.MerchantDealRepository;
import com.fpt.ojt.services.deal.DealService;
import com.fpt.ojt.services.dtos.AvailableCardRulesDto;
import com.fpt.ojt.services.dtos.Coordinate;
import com.fpt.ojt.services.dtos.MerchantAgencyWithAvailableDealsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final MerchantDealRepository merchantDealRepository;
    private final CardMerchantDealRepository cardMerchantDealRepository;

    @Override
    public List<MerchantAgencyWithAvailableDealsDto> getAvailableMerchantDeals() {
        try {
            List<MerchantDeal> merchantDeals = merchantDealRepository.findAllAvailableMerchantDeals();

            Map<UUID, List<MerchantDeal>> groupedByAgency = merchantDeals.stream()
                    .collect(Collectors.groupingBy(deal -> deal.getMerchantAgency().getId()));

            return groupedByAgency.entrySet().stream()
                    .map(entry -> {
                        MerchantDeal firstDeal = entry.getValue().getFirst();
                        MerchantAgency agency = firstDeal.getMerchantAgency();
                        Merchant merchant = agency.getMerchant();

                        return MerchantAgencyWithAvailableDealsDto.builder()
                                .merchantAgencyId(entry.getKey())
                                .merchantAgencyName(agency.getName())
                                .imageUrl(merchant.getLogoUrl())
                                .mcc(merchant.getMcc())
                                .lat(agency.getLatitude())
                                .lng(agency.getLongitude())

                                .merchantDealItems(entry.getValue().stream()
                                        .map(this::mapToMerchantDealItem)
                                        .toList())
                                .build();
                    })
                    .toList();
        } catch (RuntimeException e) {
            log.error("Error fetching available merchant deals", e);
            throw new QueryErrorException("Failed to fetch available merchant deals: " + e.getMessage());
        }
    }

    @Override
    public Double calculateMerchantOfferOnMerchantDealAndUserCardList(
            String mcc,
            MerchantAgencyWithAvailableDealsDto.MerchantDealItem merchantDealItem,
            List<AvailableCardRulesDto> userCardList) {
        try {
            double merchantDiscountRate = 0.0;
            double merchantCashbackRate = 0.0;
            boolean alreadySetMerchantRate = false;

            double totalCardDiscountRate = 0.0;
            double totalCardCashbackRate = 0.0;
            double totalCardEffectFeeRate = 0.0;

            if (userCardList != null && !userCardList.isEmpty()) {
                for (AvailableCardRulesDto userCard : userCardList) {
                    if (!checkAvailableUserCardForMerchantDeal(
                            merchantDealItem.getDealId(),
                            userCard.getCardProductId()))
                        continue;

                    alreadySetMerchantRate = true;

                    for (AvailableCardRulesDto.CardRulesDto cardRule : userCard.getCardRules()) {
                        if (checkAvailableMcc(mcc, cardRule.getAllowMccs(), cardRule.getRejectMccs())) {
                            totalCardDiscountRate += cardRule.getEffectRebateRate()
                                    + cardRule.getEffectMerchantDiscountRate();
                            totalCardCashbackRate += cardRule.getEffectCashbackRate();
                            totalCardEffectFeeRate += cardRule.getEffectFeeRate();
                        }
                    }
                }

                if (alreadySetMerchantRate) {
                    merchantDiscountRate = merchantDealItem.getDiscountRate() != null
                            ? merchantDealItem.getDiscountRate()
                            : 0.0;
                    merchantCashbackRate = merchantDealItem.getCashbackRate() != null
                            ? merchantDealItem.getCashbackRate()
                            : 0.0;
                }

                double totalCardBenefit = totalCardDiscountRate - totalCardEffectFeeRate;

                double totalDiscount = merchantDiscountRate + totalCardBenefit * (1 - merchantDiscountRate / 100);

                return totalDiscount + merchantCashbackRate + totalCardCashbackRate;
            } else {
                merchantDiscountRate = merchantDealItem.getDiscountRate() != null ? merchantDealItem.getDiscountRate()
                        : 0.0;
                merchantCashbackRate = merchantDealItem.getCashbackRate() != null ? merchantDealItem.getCashbackRate()
                        : 0.0;

                return merchantDiscountRate + merchantCashbackRate * (1 - merchantDiscountRate / 100);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error calculating merchant deals: " + e.getMessage());
        }
    }

    private boolean checkAvailableMcc(String mcc, List<String> allowMccs, List<String> rejectMccs) {
        if (mcc == null)
            throw new RuntimeException("Error in passing string mcc");

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

    private boolean checkAvailableUserCardForMerchantDeal(UUID dealId, UUID cardProductId) {
        if (cardMerchantDealRepository.isDealAvailableForAllCards(dealId))
            return true;

        return cardMerchantDealRepository.existsByDealAndCard(dealId, cardProductId);
    }

    private MerchantAgencyWithAvailableDealsDto.MerchantDealItem mapToMerchantDealItem(MerchantDeal deal) {
        return MerchantAgencyWithAvailableDealsDto.MerchantDealItem.builder()
                .dealName(deal.getDealName())
                .dealId(deal.getId())
                .discountRate(deal.getDiscountRate())
                .cashbackRate(deal.getCashbackRate())
                .pointsMultiplier(deal.getPointsMultiplier())
                .description(deal.getDescription())
                .validFrom(deal.getValidFrom())
                .validTo(deal.getValidTo())
                .build();
    }

    @Override
    public Double calculateCardOnlyBenefit(AvailableCardRulesDto.CardRulesDto cardRule) {
        try {
            double rebateRate = cardRule.getEffectRebateRate() != null ? cardRule.getEffectRebateRate() : 0.0;
            double merchantDiscountRate = cardRule.getEffectMerchantDiscountRate() != null
                    ? cardRule.getEffectMerchantDiscountRate()
                    : 0.0;
            double feeRate = cardRule.getEffectFeeRate() != null ? cardRule.getEffectFeeRate() : 0.0;
            double cashbackRate = cardRule.getEffectCashbackRate() != null ? cardRule.getEffectCashbackRate() : 0.0;

            double discountRate = rebateRate + merchantDiscountRate - feeRate;
            return discountRate + cashbackRate * (1 - discountRate / 100);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error calculating card-only benefit: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MerchantAgencyWithAvailableDealsDto> getNearestMerchantDeals(Coordinate userLocation,
            int radiusMeters) {
        List<MerchantDealFlatProjection> flatData = merchantDealRepository.findAvailableDealsInRadius(
                userLocation.getLatitude(),
                userLocation.getLongitude(),
                radiusMeters);

        Map<UUID, List<MerchantDealFlatProjection>> grouped = flatData.stream()
                .collect(Collectors.groupingBy(MerchantDealFlatProjection::getAgencyId));

        return grouped.values().stream()
                .map(projections -> {
                    MerchantDealFlatProjection first = projections.getFirst();
                    return MerchantAgencyWithAvailableDealsDto.builder()
                            .merchantAgencyId(first.getAgencyId())
                            .merchantAgencyName(first.getAgencyName())
                            .mcc(first.getMerchantMcc())
                            .imageUrl(first.getMerchantLogoUrl())
                            .lat(first.getLatitude())
                            .lng(first.getLongitude())
                            .isFavorite(false)
                            .isSubscribed(false)

                            // Map danh sách deals
                            .merchantDealItems(projections.stream()
                                    .map(p -> MerchantAgencyWithAvailableDealsDto.MerchantDealItem.builder()
                                            .dealId(p.getDealId())
                                            .dealName(p.getDealName())
                                            .discountRate(p.getDiscountRate())
                                            .cashbackRate(p.getCashbackRate())
                                            .pointsMultiplier(p.getPointsMultiplier())
                                            .description(p.getDescription())
                                            .validFrom(p.getValidFrom())
                                            .validTo(p.getValidTo())
                                            .build())
                                    .collect(Collectors.toList()))
                            .distanceMeters(first.getDistanceMeters())
                            .build();
                })
                .sorted(Comparator.comparingDouble(dto -> {
                    return dto.getDistanceMeters();
                }))
                .toList();
    }
}
