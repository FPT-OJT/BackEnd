package com.fpt.ojt.presentations.controllers.users;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.controllers.base.ResponseFactory;
import com.fpt.ojt.presentations.dtos.requests.user.AddFavoriteMerchantRequest;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.dtos.FavoriteMerchantDto;
import com.fpt.ojt.services.user.FavoriteMerchantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/favorite-merchants")
@RequiredArgsConstructor
@Tag(name = "Favorite Merchants", description = "APIs for managing user favorite merchants")
public class FavoriteMerchantController {
    private final FavoriteMerchantService favoriteMerchantService;
    private final AuthService authService;
    private final ResponseFactory responseFactory;

    @Operation(summary = "Get favorite merchants", description = "Retrieve all favorite merchants for the current user")
    @GetMapping
    public ResponseEntity<SingleResponse<List<FavoriteMerchantDto>>> getFavoriteMerchants() {
        var userId = authService.getCurrentUserId();
        return responseFactory.successSingle(favoriteMerchantService.getFavoriteMerchants(userId), "ok");
    }

    @Operation(summary = "Add favorite merchant", description = "Add a merchant agency to the current user's favorites")
    @PostMapping
    public ResponseEntity<SingleResponse<Void>> addFavoriteMerchant(@RequestBody AddFavoriteMerchantRequest request) {
        var userId = authService.getCurrentUserId();
        favoriteMerchantService.addFavoriteMerchant(userId, request.getMerchantAgencyId());
        return responseFactory.successSingle(null, "Favorite merchant added successfully");
    }

    @Operation(summary = "Remove favorite merchant", description = "Remove a merchant from the current user's favorites")
    @DeleteMapping("{favoriteMerchantId}")
    public ResponseEntity<SingleResponse<Void>> removeFavoriteMerchant(
            @Parameter(description = "UUID of the favorite merchant to remove", required = true) @PathVariable UUID favoriteMerchantId) {
        var userId = authService.getCurrentUserId();
        favoriteMerchantService.removeFavoriteMerchant(userId, favoriteMerchantId);
        return responseFactory.successSingle(null, "Favorite merchant removed successfully");
    }
}
