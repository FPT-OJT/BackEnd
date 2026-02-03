package com.fpt.ojt.presentations.controllers.card;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.card.CardService;
import com.fpt.ojt.services.dtos.UserCardDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-cards")
@RequiredArgsConstructor
public class UserCardController {

    final CardService cardService;
    final AuthService authService;

    @GetMapping("/@me")
    public ResponseEntity<SingleResponse<List<UserCardDto>>> getUserCards() {
        List<UserCardDto> userCards = cardService.getUserCards(authService.getCurrentUserId());
        return ResponseEntity.ok(
                SingleResponse.<List<UserCardDto>>builder()
                        .data(userCards)
                        .statusCode(200)
                        .message("ok")
                        .build());
    }

    @PostMapping("")
    public ResponseEntity<SingleResponse<String>> addCardToUser() {
        return ResponseEntity.ok(
                SingleResponse.<String>builder()
                        .data("Not implemented yet")
                        .statusCode(501)
                        .message("Not implemented yet")
                        .build());
    }
}
