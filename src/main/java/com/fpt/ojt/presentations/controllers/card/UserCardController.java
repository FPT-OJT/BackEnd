package com.fpt.ojt.presentations.controllers.card;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.fpt.ojt.presentations.dtos.requests.card.AddCardToUserRequest;
import com.fpt.ojt.presentations.dtos.requests.card.EditUserCard;
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
                List<UserCardDto> userCards =
                                cardService.getUserCards(authService.getCurrentUserId());
                return ResponseEntity.ok(SingleResponse.<List<UserCardDto>>builder().data(userCards)
                                .statusCode(200).message("ok").build());
        }

        @PostMapping("")
        public ResponseEntity<SingleResponse<String>> addCardToUser(AddCardToUserRequest request) {
                cardService.addCardToUser(authService.getCurrentUserId(), request);
                return ResponseEntity.ok(SingleResponse.<String>builder().data("ok").statusCode(201)
                                .message("ok").build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<SingleResponse<String>> editCardToUser(@PathVariable UUID id,
                        EditUserCard request) {
                cardService.editUserCard(id, authService.getCurrentUserId(), request);
                return ResponseEntity.ok(SingleResponse.<String>builder().data("ok").statusCode(200)
                                .message("ok").build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<SingleResponse<String>> deleteCardToUser(@PathVariable UUID id) {
                cardService.removeUserCard(id, authService.getCurrentUserId());
                return ResponseEntity.ok(SingleResponse.<String>builder().data("ok").statusCode(200)
                                .message("ok").build());
        }
}
