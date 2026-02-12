package com.fpt.ojt.presentations.controllers.merchants;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.dtos.MerchantSort;
import com.fpt.ojt.services.dtos.NearestAgencyDto;
import com.fpt.ojt.services.merchants.MerchantAgencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/merchants/agencies")
@RequiredArgsConstructor
@Tag(name = "Merchant Agencies", description = "APIs for searching and retrieving merchant agency information")
public class MerchantAgencyController {

    private final MerchantAgencyService merchantAgencyService;

    private static final String DEFAULT_SEARCH_LIMIT = "10";

    @Operation(summary = "Find nearest merchant agencies", description = "Search for the nearest merchant agencies based on location coordinates and keyword")
    @GetMapping("/nearest")
    public ResponseEntity<SingleResponse<List<NearestAgencyDto>>> getNearestAgencies(
            @Parameter(description = "Search keyword for merchant agencies", required = true) @RequestParam(required = true) String keyword,
            @Parameter(description = "Latitude coordinate", required = true, example = "10.762622") @RequestParam(required = true) Double latitude,
            @Parameter(description = "Longitude coordinate", required = true, example = "106.660172") @RequestParam(required = true) Double longitude,
            @Parameter(description = "Maximum number of results to return", example = "10") @RequestParam(defaultValue = DEFAULT_SEARCH_LIMIT, required = false) int limit,
            @Parameter(description = "Sort order for results", example = "NAME_ASC") @RequestParam(defaultValue = "NAME_ASC", required = false) MerchantSort sort)
            {

        var agencies = merchantAgencyService.findNearestAgencies(keyword, latitude, longitude, limit, sort);
        return ResponseEntity.ok(
                SingleResponse.<List<NearestAgencyDto>>builder()
                        .data(agencies)
                        .statusCode(200)
                        .message("ok")
                        .build());
    }
}
