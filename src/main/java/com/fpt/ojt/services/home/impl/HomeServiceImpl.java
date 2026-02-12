package com.fpt.ojt.services.home.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.card.CardService;
import com.fpt.ojt.services.dtos.Coordinate;
import com.fpt.ojt.services.dtos.HomeParam;
import com.fpt.ojt.services.home.HomeService;
import com.fpt.ojt.services.home.LocationService;
import com.fpt.ojt.services.merchants.CategoryService;
import com.fpt.ojt.services.merchants.MerchantService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final MerchantService merchantService;
    private final CardService cardService;
    private final AuthService authService;
    private final CategoryService categoryService;
    private static final int MERCHANT_OFFERS_LIMIT = 10;
    private static final int MERCHANT_CATEGORIES_LIMIT = 10;
    private final LocationService locationService;

    @Override
    public HomePageResponse getHomeData(HomeParam homeParam) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Coordinate userLocation = homeParam.getUserLocation().orElse(
                    locationService.mapFromIpAddress(homeParam.getIpAddress()));
            UUID userId = authService.getCurrentUserId();

            var merchantFuture = CompletableFuture.supplyAsync(
                    () -> merchantService.getMerchantOffers(MERCHANT_OFFERS_LIMIT, userId, userLocation), executor)
                    .exceptionally(ex -> List.of());

            var categoryFuture = CompletableFuture.supplyAsync(
                    () -> categoryService.getMerchantCategories(MERCHANT_CATEGORIES_LIMIT), executor)
                    .exceptionally(ex -> Collections.emptyList());

            var cardFuture = CompletableFuture.supplyAsync(
                    () -> cardService.isUserCardEmpty(userId), executor)
                    .exceptionally(ex -> false);

            return HomePageResponse.builder()
                    .merchantOffers(merchantFuture.join())
                    .merchantCategories(categoryFuture.join())
                    .isUserCardEmpty(cardFuture.join())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
