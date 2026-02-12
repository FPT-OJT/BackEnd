package com.fpt.ojt.services.merchants.impl;

import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.merchant.MerchantCategory;
import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.repositories.merchant.MerchantCategoryRepository;
import com.fpt.ojt.repositories.user.FavoriteMerchantRepository;
import com.fpt.ojt.repositories.user.SubscribedMerchantRepository;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.card.CardService;
import com.fpt.ojt.services.deal.DealService;
import com.fpt.ojt.services.dtos.AvailableCardRulesDto;
import com.fpt.ojt.services.dtos.Coordinate;
import com.fpt.ojt.services.dtos.MerchantAgencyWithAvailableDealsDto;
import com.fpt.ojt.services.merchants.MerchantService;
import com.fpt.ojt.services.dtos.MerchantOfferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final AuthService authService;
    private final CardService cardService;
    private final DealService dealService;
    private final MerchantCategoryRepository merchantCategoryRepository;
    private final MerchantAgencyRepository merchantAgencyRepository;
    private final FavoriteMerchantRepository favoriteMerchantRepository;
    private final SubscribedMerchantRepository subscribedMerchantRepository;
    static final int NEAREST_MERCHANT_DEALS_RADIUS_METERS = 20_000; // 20km


    @Override
    public List<HomePageResponse.MerchantOffer> getMerchantOffers(int limit, UUID currentUserId, Coordinate userLocation) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var offersWithMerchantsFuture = executor.submit(() -> getOffersWithMerchants(currentUserId, userLocation));
            var offersWithoutMerchantsFuture = executor.submit(() -> getOffersWithoutMerchants(currentUserId));

            List<MerchantOfferDto> offersWithMerchant =  offersWithMerchantsFuture.get();
            List<MerchantOfferDto> offersWithoutMerchant = offersWithoutMerchantsFuture.get();

            return Stream.concat(offersWithMerchant.stream(), offersWithoutMerchant.stream())
                    .sorted((o1, o2) -> Double.compare(
                            o2.getTotalDiscount() != null ? o2.getTotalDiscount() : 0.0,
                            o1.getTotalDiscount() != null ? o1.getTotalDiscount() : 0.0
                    ))
                    .limit(limit)
                    .map(dto -> HomePageResponse.MerchantOffer.builder()
                            .merchantAgencyId(dto.getMerchantAgencyId())
                            .merchantAgencyName(dto.getMerchantAgencyName())
                            .imageUrl(dto.getImageUrl())
                            .merchantDealName(dto.getMerchantDealName())
                            .isFavorite(dto.isFavorite())
                            .isSubscribed(dto.isSubscribed())
                            .lat(dto.getLat())
                            .lng(dto.getLng())
                            .totalDiscount(dto.getTotalDiscount())
                            .build())
                    .toList();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<AvailableCardRulesDto> getUserAvailableCardRules(UUID currentUserId) {
        return cardService.getAvailableCardRulesByUserId(currentUserId);
    }

    private List<MerchantAgencyWithAvailableDealsDto> getUserAvailableMerchantDeals(Coordinate userLocation) {
        return dealService.getNearestMerchantDeals(userLocation, NEAREST_MERCHANT_DEALS_RADIUS_METERS);
    }

    private List<MerchantOfferDto> getOffersWithMerchants(UUID currentUserId, Coordinate userLocation) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var availableCardRulesFuture = executor.submit(() -> getUserAvailableCardRules(currentUserId));
            var allMerchantDealsFuture = executor.submit(() -> getUserAvailableMerchantDeals(userLocation));

            List<AvailableCardRulesDto> availableCardRulesDtos = availableCardRulesFuture.get();
            List<MerchantAgencyWithAvailableDealsDto> agencies = allMerchantDealsFuture.get();

            var offerFutures = agencies.stream()
                    .flatMap(agency -> agency.getMerchantDealItems().stream()
                            .map(availableDeal -> executor.submit(() -> {
                                var isFavoriteFuture = executor.submit(() ->
                                        favoriteMerchantRepository.existsByUserIdAndMerchantAgencyId(
                                                currentUserId,
                                                agency.getMerchantAgencyId()
                                        ));

                                var isSubscribedFuture = executor.submit(() ->
                                        subscribedMerchantRepository.existsByUserIdAndMerchantAgencyId(
                                                currentUserId,
                                                agency.getMerchantAgencyId()
                                        ));

                                var totalDiscountFuture = executor.submit(() ->
                                        dealService.calculateMerchantOfferOnMerchantDealAndUserCardList(
                                                agency.getMcc(),
                                                availableDeal,
                                                availableCardRulesDtos
                                        ));

                                boolean isFavorite = isFavoriteFuture.get();
                                boolean isSubscribed = isSubscribedFuture.get();
                                Double totalDiscount = totalDiscountFuture.get();

                                return MerchantOfferDto.builder()
                                        .merchantAgencyId(agency.getMerchantAgencyId())
                                        .merchantAgencyName(agency.getMerchantAgencyName())
                                        .mcc(agency.getMcc())
                                        .imageUrl(agency.getImageUrl())
                                        .merchantDealName(availableDeal.getDealName())
                                        .isFavorite(isFavorite)
                                        .isSubscribed(isSubscribed)
                                        .lat(agency.getLat())
                                        .lng(agency.getLng())
                                        .totalDiscount(totalDiscount)
                                        .build();
                            })))
                    .toList();

            List<MerchantOfferDto> merchantOfferDtos = new ArrayList<>();
            for (var future : offerFutures) {
                merchantOfferDtos.add(future.get());
            }

            return merchantOfferDtos;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MerchantOfferDto> getOffersWithoutMerchants(UUID currentUserId) {
        List<AvailableCardRulesDto> availableCardRulesDtos = getUserAvailableCardRules(currentUserId);

        if (availableCardRulesDtos == null || availableCardRulesDtos.isEmpty()) {
            return List.of();
        }

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var offerFutures = availableCardRulesDtos.stream()
                    .flatMap(cardDto -> cardDto.getCardRules().stream()
                            .flatMap(cardRule -> {
                                double cardBenefit = dealService.calculateCardOnlyBenefit(cardRule);
                                // Skip if no benefit
                                if (cardBenefit <= 0) {
                                    return Stream.empty();
                                }

                                List<String> allowMccs = cardRule.getAllowMccs();
                                List<String> rejectMccs = cardRule.getRejectMccs();

                                // Case 1: allowMccs == null && rejectMccs == null → all merchants
                                if (allowMccs == null && rejectMccs == null) {
                                    List<MerchantAgency> agencies = merchantAgencyRepository.findAllActiveWithMerchant();

                                    return agencies.stream()
                                            .map(agency -> executor.submit(() -> {
                                                var isFavoriteFuture = executor.submit(() ->
                                                        favoriteMerchantRepository.existsByUserIdAndMerchantAgencyId(
                                                                currentUserId,
                                                                agency.getId()
                                                        ));

                                                var isSubscribedFuture = executor.submit(() ->
                                                        subscribedMerchantRepository.existsByUserIdAndMerchantAgencyId(
                                                                currentUserId,
                                                                agency.getId()
                                                        ));

                                                boolean isFavorite = isFavoriteFuture.get();
                                                boolean isSubscribed = isSubscribedFuture.get();

                                                return MerchantOfferDto.builder()
                                                        .merchantAgencyId(agency.getId())
                                                        .merchantAgencyName(agency.getName())
                                                        .mcc(agency.getMerchant().getMcc())
                                                        .imageUrl(agency.getImageUrl())
                                                        .merchantDealName("Card Benefit: " + String.format("%.1f%%", cardBenefit))
                                                        .isFavorite(isFavorite)
                                                        .isSubscribed(isSubscribed)
                                                        .lat(agency.getLatitude())
                                                        .lng(agency.getLongitude())
                                                        .totalDiscount(cardBenefit)
                                                        .build();
                                            }));
                                }

                                // Case 2: allowMccs == null && rejectMccs != null → Allow all except rejected
                                if (allowMccs == null && !rejectMccs.isEmpty()) {
                                    List<MerchantAgency> agencies = merchantAgencyRepository.findAllActiveExcludingMccs(rejectMccs);

                                    return agencies.stream()
                                            .map(agency -> executor.submit(() -> {
                                                var isFavoriteFuture = executor.submit(() ->
                                                        favoriteMerchantRepository.existsByUserIdAndMerchantAgencyId(
                                                                currentUserId,
                                                                agency.getId()
                                                        ));

                                                var isSubscribedFuture = executor.submit(() ->
                                                        subscribedMerchantRepository.existsByUserIdAndMerchantAgencyId(
                                                                currentUserId,
                                                                agency.getId()
                                                        ));

                                                boolean isFavorite = isFavoriteFuture.get();
                                                boolean isSubscribed = isSubscribedFuture.get();

                                                return MerchantOfferDto.builder()
                                                        .merchantAgencyId(agency.getId())
                                                        .merchantAgencyName(agency.getName())
                                                        .mcc(agency.getMerchant().getMcc())
                                                        .imageUrl(agency.getImageUrl())
                                                        .merchantDealName("Card Benefit: " + String.format("%.1f%%", cardBenefit))
                                                        .isFavorite(isFavorite)
                                                        .isSubscribed(isSubscribed)
                                                        .lat(agency.getLatitude())
                                                        .lng(agency.getLongitude())
                                                        .totalDiscount(cardBenefit)
                                                        .build();
                                            }));
                                }

                                // Case 3: allowMccs != null → Only allow specific MCCs
                                if (allowMccs != null && !allowMccs.isEmpty()) {
                                    return allowMccs.stream()
                                            .flatMap(mcc -> {
                                                List<MerchantAgency> agencies = merchantAgencyRepository.findByMerchantMcc(mcc);

                                                return agencies.stream()
                                                        .map(agency -> executor.submit(() -> {
                                                            var isFavoriteFuture = executor.submit(() ->
                                                                    favoriteMerchantRepository.existsByUserIdAndMerchantAgencyId(
                                                                            currentUserId,
                                                                            agency.getId()
                                                                    ));

                                                            var isSubscribedFuture = executor.submit(() ->
                                                                    subscribedMerchantRepository.existsByUserIdAndMerchantAgencyId(
                                                                            currentUserId,
                                                                            agency.getId()
                                                                    ));

                                                            boolean isFavorite = isFavoriteFuture.get();
                                                            boolean isSubscribed = isSubscribedFuture.get();

                                                            return MerchantOfferDto.builder()
                                                                    .merchantAgencyId(agency.getId())
                                                                    .merchantAgencyName(agency.getName())
                                                                    .mcc(agency.getMerchant().getMcc())
                                                                    .imageUrl(agency.getImageUrl())
                                                                    .merchantDealName("Card Benefit: " + String.format("%.1f%%", cardBenefit))
                                                                    .isFavorite(isFavorite)
                                                                    .isSubscribed(isSubscribed)
                                                                    .lat(agency.getLatitude())
                                                                    .lng(agency.getLongitude())
                                                                    .totalDiscount(cardBenefit)
                                                                    .build();
                                                        }));
                                            });
                                }

                                return Stream.empty();
                            }))
                    .toList();

            // Collect all results
            List<MerchantOfferDto> merchantOfferDtos = new ArrayList<>();
            for (var future : offerFutures) {
                merchantOfferDtos.add(future.get());
            }

            return merchantOfferDtos.stream()
                    .collect(java.util.stream.Collectors.toMap(
                            MerchantOfferDto::getMerchantAgencyId,
                            offer -> offer,
                            (existing, replacement) -> existing.getTotalDiscount() >= replacement.getTotalDiscount() ? existing : replacement
                    ))
                    .values()
                    .stream()
                    .sorted((o1, o2) -> Double.compare(
                            o2.getTotalDiscount() != null ? o2.getTotalDiscount() : 0.0,
                            o1.getTotalDiscount() != null ? o1.getTotalDiscount() : 0.0
                    ))
                    .toList();

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error calculating card-only offers: " + e.getMessage(), e);
        }
    }

    private List<HomePageResponse.MerchantCategory> getMerchantCategories() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<MerchantCategory> merchantCategories = merchantCategoryRepository.findAllByDeletedAtIsNull(pageable);

        if (merchantCategories == null || !merchantCategories.hasContent()) {
            return List.of();
        }

        return merchantCategories.getContent().stream()
                .map(category -> HomePageResponse.MerchantCategory.builder()
                        .id(category.getId())
                        .name(category.getCategoryName())
                        .imageUrl(category.getImageUrl())
                        .build())
                .toList();
    }
}
