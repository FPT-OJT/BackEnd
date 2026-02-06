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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/favorite-merchants")
@RequiredArgsConstructor
public class FavoriteMerchantController {
    private final FavoriteMerchantService favoriteMerchantService;
    private final AuthService authService;
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<SingleResponse<List<FavoriteMerchantDto>>> getFavoriteMerchants() {
        var userId = authService.getCurrentUserId();
        return responseFactory.successSingle(favoriteMerchantService.getFavoriteMerchants(userId), null);
    }

    @PostMapping
    public ResponseEntity<SingleResponse<Void>> addFavoriteMerchant(@RequestBody AddFavoriteMerchantRequest request) {
        var userId = authService.getCurrentUserId();
        favoriteMerchantService.addFavoriteMerchant(userId, request.getMerchantAgencyId());
        return ResponseEntity.ok(SingleResponse.<Void>builder()
                .statusCode(201)
                .build());
    }

    @DeleteMapping("{favoriteMerchantId}")
    public ResponseEntity<SingleResponse<Void>> removeFavoriteMerchant(@PathVariable UUID favoriteMerchantId) {
        var userId = authService.getCurrentUserId();
        favoriteMerchantService.removeFavoriteMerchant(userId, favoriteMerchantId);
        return ResponseEntity.ok(SingleResponse.<Void>builder()
                .statusCode(200)
                .build());
    }
}
