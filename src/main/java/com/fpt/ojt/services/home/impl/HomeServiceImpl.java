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
import com.fpt.ojt.services.home.HomeService;
import com.fpt.ojt.services.merchants.CategoryService;
import com.fpt.ojt.services.merchants.MerchantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeServiceImpl implements HomeService {
    private final MerchantService merchantService;
    private final CardService cardService;
    private final AuthService authService;
    private final CategoryService categoryService;
    private static final int MERCHANT_OFFERS_LIMIT = 10;
    private static final int MERCHANT_CATEGORIES_LIMIT = 10;

    @Override
    public HomePageResponse getHomeData() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            UUID userId = authService.getCurrentUserId();

            var merchantFuture = CompletableFuture.supplyAsync(
                    () -> merchantService.getMerchantOffers(MERCHANT_OFFERS_LIMIT, userId), executor)
                    .exceptionally(ex -> {
                        log.error("Error getting merchant offers", ex);
                        return List.of();
                    });

            var categoryFuture = CompletableFuture.supplyAsync(
                    () -> categoryService.getMerchantCategories(MERCHANT_CATEGORIES_LIMIT), executor)
                    .exceptionally(ex -> {
                        log.error("Error getting merchant categories", ex);
                        return Collections.emptyList();
                    });

            var cardFuture = CompletableFuture.supplyAsync(
                    () -> cardService.isUserCardEmpty(userId), executor)
                    .exceptionally(ex -> {
                        log.error("Error getting user card empty", ex);
                        return false;
                    });

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
