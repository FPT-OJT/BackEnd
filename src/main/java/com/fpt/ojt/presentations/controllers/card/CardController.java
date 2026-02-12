package com.fpt.ojt.presentations.controllers.card;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.card.CardService;
import com.fpt.ojt.services.dtos.CardProductDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
@Tag(name = "Card Management", description = "APIs for managing cards")
public class CardController {
    private final CardService cardService;

    private static final String DEFAULT_SEARCH_LIMIT = "10";

    @Operation(summary = "Search card products", description = "Search for card products by keyword with optional limit")
    @GetMapping("/search")
    public ResponseEntity<SingleResponse<List<CardProductDto>>> searchCardProducts(
            @Parameter(description = "Search keyword for card products") @RequestParam(defaultValue = "", required = false) String keyword,
            @Parameter(description = "Maximum number of results to return", example = "10") @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_LIMIT) Integer limit) {
        return ResponseEntity.ok(
                SingleResponse.<List<CardProductDto>>builder()
                        .data(cardService.searchCardProducts(keyword, limit))
                        .statusCode(200)
                        .message("ok")
                        .build());
    }
}
