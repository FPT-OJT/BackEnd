package com.fpt.ojt.presentations.controllers.merchants;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.dtos.NearestAgencyDto;
import com.fpt.ojt.services.merchants.MerchantAgencyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/merchants/agencies")
@RequiredArgsConstructor
public class MerchantAgencyController {

    private final MerchantAgencyService merchantAgencyService;

    private static final String DEFAULT_SEARCH_LIMIT = "10";

    @GetMapping("/nearest")
    public ResponseEntity<SingleResponse<List<NearestAgencyDto>>> getNearestAgencies(
            @RequestParam(required = true) String keyword,
            @RequestParam(required = true) Double latitude,
            @RequestParam(required = true) Double longitude,
            @RequestParam(defaultValue = DEFAULT_SEARCH_LIMIT, required = false) int limit) {

        var agencies = merchantAgencyService.findNearestAgencies(keyword, latitude, longitude, limit);
        return ResponseEntity.ok(
                SingleResponse.<List<NearestAgencyDto>>builder()
                        .data(agencies)
                        .statusCode(200)
                        .message("ok")
                        .build());
    }
}
