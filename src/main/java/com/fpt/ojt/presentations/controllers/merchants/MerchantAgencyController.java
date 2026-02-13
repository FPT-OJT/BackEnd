package com.fpt.ojt.presentations.controllers.merchants;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.presentations.dtos.responses.merchant.MerchantAgencyCardsDealsResponse;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.dtos.MerchantSort;
import com.fpt.ojt.services.dtos.NearestAgencyDto;
import com.fpt.ojt.services.merchantdetail.MerchantDetailService;
import com.fpt.ojt.services.merchants.MerchantAgencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/merchants/agencies")
@RequiredArgsConstructor
public class MerchantAgencyController extends AbstractBaseController {

        private final MerchantAgencyService merchantAgencyService;
        private final MerchantDetailService merchantDetailService;
        private final AuthService authService;

        private static final String DEFAULT_SEARCH_LIMIT = "10";

        @GetMapping("/nearest")
        public ResponseEntity<SingleResponse<List<NearestAgencyDto>>> getNearestAgencies(
                        @Parameter(description = "Search keyword for merchant agencies", required = true) @RequestParam(required = true) String keyword,
                        @Parameter(description = "Latitude coordinate", required = true, example = "10.762622") @RequestParam(required = true) Double latitude,
                        @Parameter(description = "Longitude coordinate", required = true, example = "106.660172") @RequestParam(required = true) Double longitude,
                        @Parameter(description = "Maximum number of results to return", example = "10") @RequestParam(defaultValue = DEFAULT_SEARCH_LIMIT, required = false) int limit,
                        @Parameter(description = "Sort order for results", example = "NAME_ASC") @RequestParam(defaultValue = "NAME_ASC", required = false) MerchantSort sort) {
                var agencies = merchantAgencyService.findNearestAgencies(keyword, latitude, longitude, limit, sort);
                return responseFactory.successSingle(
                                agencies,
                                "Get nearest agencies successfully");
        }

        @GetMapping("/{merchantAgencyId}/cards-deals")
        public ResponseEntity<SingleResponse<MerchantAgencyCardsDealsResponse>> getCardsWithDeals(
                        @PathVariable UUID merchantAgencyId) {

                UUID currentUserId = authService.getCurrentUserId();
                var cardsWithDeals = merchantDetailService.getCardsWithDeals(merchantAgencyId, currentUserId);

                return responseFactory.successSingle(cardsWithDeals, "Get cards with deals successfully");
        }
}
