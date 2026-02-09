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

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
@Tag(name = "Card Management", description = "APIs for managing cards")
public class CardController {
    private final CardService cardService;

    private static final String DEFAULT_SEARCH_LIMIT = "10";

    @GetMapping("/search")
    public ResponseEntity<SingleResponse<List<CardProductDto>>> searchCardProducts(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_LIMIT) Integer limit) {
        return ResponseEntity.ok(
                SingleResponse.<List<CardProductDto>>builder()
                        .data(cardService.searchCardProducts(keyword, limit))
                        .statusCode(200)
                        .message("ok")
                        .build());
    }
}
