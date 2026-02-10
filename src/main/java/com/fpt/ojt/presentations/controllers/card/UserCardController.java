package com.fpt.ojt.presentations.controllers.card;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.requests.card.AddCardToUserRequest;
import com.fpt.ojt.presentations.dtos.requests.card.EditUserCard;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.card.CardService;
import com.fpt.ojt.services.dtos.UserCardDetailDto;
import com.fpt.ojt.services.dtos.UserCardDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-cards")
@RequiredArgsConstructor
@Tag(name = "User Cards", description = "APIs for managing user card operations")
public class UserCardController extends AbstractBaseController {

        final CardService cardService;
        final AuthService authService;

        @Operation(summary = "Get current user's cards", description = "Retrieve all cards belonging to the authenticated user")
        @GetMapping("/@me")
        public ResponseEntity<SingleResponse<List<UserCardDto>>> getUserCards() {
                List<UserCardDto> userCards = cardService.getUserCards(authService.getCurrentUserId());
                return responseFactory.successSingle(userCards, "Get user cards successful");
        }

        @Operation(summary = "Add a new card to user", description = "Add a new card to the authenticated user's collection")
        @PostMapping("")
        public ResponseEntity<SingleResponse<String>> addCardToUser(@RequestBody AddCardToUserRequest request) {
                cardService.addCardToUser(authService.getCurrentUserId(), request);
                return responseFactory.successSingle("ok", "Add card to user successful");
        }

        @Operation(summary = "Edit user card", description = "Update details of a specific user card")
        @PutMapping("/{id}")
        public ResponseEntity<SingleResponse<String>> editCardToUser(
                        @Parameter(description = "UUID of the card to edit", required = true) @PathVariable UUID id,
                        @RequestBody EditUserCard request) {
                cardService.editUserCard(id, authService.getCurrentUserId(), request);
                return responseFactory.successSingle("ok", "Edit card to user successful");
        }

        @Operation(summary = "Delete user card", description = "Remove a specific card from the user's collection")
        @DeleteMapping("/{id}")
        public ResponseEntity<SingleResponse<String>> deleteCardToUser(
                        @Parameter(description = "UUID of the card to delete", required = true) @PathVariable UUID id) {
                cardService.removeUserCard(id, authService.getCurrentUserId());
                return responseFactory.successSingle("ok", "Delete card to user successful");
        }

        @Operation(summary = "Get user card details", description = "Retrieve detailed information about a specific user card")
        @GetMapping("/{id}")
        public ResponseEntity<SingleResponse<UserCardDetailDto>> getUserCardDetail(
                        @Parameter(description = "UUID of the card to retrieve", required = true) @PathVariable UUID id) {
                UserCardDetailDto userCardDetail = cardService.getUserCardDetail(id, authService.getCurrentUserId());
                return responseFactory.successSingle(userCardDetail, "Get user card detail successful");
        }

        @Operation(summary = "Get user cards by card type", description = "Retrieve all user cards filtered by a specific card type")
        @GetMapping("/card-type/{cardType}")
        public ResponseEntity<SingleResponse<List<UserCardDto>>> getUserCardsByCardType(
                        @Parameter(description = "Type of card to filter by", required = true, example = "PAYMENT_APP") @PathVariable String cardType) {
                List<UserCardDto> userCards = cardService.getUserCardsByCardType(authService.getCurrentUserId(),
                                cardType);
                return responseFactory.successSingle(userCards, "Get user cards by card type successful");
        }

        @Operation(summary = "Check if user card exists", description = "Verify whether a specific card exists in the user's collection")
        @GetMapping("/is-exists/{cardId}")
        public ResponseEntity<SingleResponse<Boolean>> isUserCardExists(
                        @Parameter(description = "UUID of the card to check", required = true) @PathVariable UUID cardId) {
                Boolean isUserCardExists = cardService.isUserCardExists(cardId, authService.getCurrentUserId());
                return responseFactory.successSingle(isUserCardExists, "Check if user card exists successful");
        }
}
