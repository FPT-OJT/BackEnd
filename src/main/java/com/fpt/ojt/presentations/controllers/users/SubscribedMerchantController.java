package com.fpt.ojt.presentations.controllers.users;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.dtos.SubscribedMerchantAgencyDto;
import com.fpt.ojt.services.dtos.SubscribedMerchantDto;
import com.fpt.ojt.services.user.SubscribedMerchantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/users/subscribed-merchants")
@RequiredArgsConstructor
@Tag(name = "Subscribed Merchants", description = "APIs for managing user merchant subscriptions")
public class SubscribedMerchantController extends AbstractBaseController {
    private final SubscribedMerchantService subscribedMerchantService;
    private final AuthService authService;
    
    @Operation(summary = "Get subscribed merchants", description = "Retrieve all merchants that the current user has subscribed to")
    @GetMapping
    public ResponseEntity<SingleResponse<List<SubscribedMerchantDto >>> getSubscribedMerchants() {
        return responseFactory.successSingle(subscribedMerchantService.getSubscribedMerchants(authService.getCurrentUserId()),
                "Get subscribed merchants successful");
    }
    
    @Operation(summary = "Get subscribed merchant agencies", description = "Retrieve all merchant agencies that the current user has subscribed to")
    @GetMapping("/agencies")
    public ResponseEntity<SingleResponse<List<SubscribedMerchantAgencyDto>>> getSubscribedMerchantAgencies() {
        return responseFactory.successSingle(subscribedMerchantService.getSubscribedMerchantAgencies(authService.getCurrentUserId()),
                "Get subscribed merchant agencies successful");
    }
    
    @Operation(summary = "Subscribe to merchant agency", description = "Subscribe the current user to a specific merchant agency")
    @PostMapping("/agencies/{merchantAgencyId}")
    public ResponseEntity<SingleResponse<Void>> subscribeMerchantAgency(
            @Parameter(description = "UUID of the merchant agency to subscribe to", required = true) @PathVariable UUID merchantAgencyId) {
        subscribedMerchantService.subscribeMerchantAgency(authService.getCurrentUserId(), merchantAgencyId);
        return responseFactory.successSingle(null, "Subscribe merchant agency successful");
    }
    
    @Operation(summary = "Unsubscribe from merchant agency", description = "Unsubscribe the current user from a specific merchant agency")
    @DeleteMapping("/agencies/{merchantAgencyId}")
    public ResponseEntity<SingleResponse<Void>> unsubscribeMerchantAgency(
            @Parameter(description = "UUID of the merchant agency to unsubscribe from", required = true) @PathVariable UUID merchantAgencyId) {
        subscribedMerchantService.unsubscribeMerchantAgency(authService.getCurrentUserId(), merchantAgencyId);
        return responseFactory.successSingle(null, "Unsubscribe merchant agency successful");
    }
    
    @Operation(summary = "Subscribe to merchant", description = "Subscribe the current user to a specific merchant")
    @PostMapping("/{merchantId}")
    public ResponseEntity<SingleResponse<Void>> subscribeMerchant(
            @Parameter(description = "UUID of the merchant to subscribe to", required = true) @PathVariable UUID merchantId) {
        subscribedMerchantService.subscribeMerchant(authService.getCurrentUserId(), merchantId);
        return responseFactory.successSingle(null, "Subscribe merchant successful");
    }
    
    @Operation(summary = "Unsubscribe from merchant", description = "Unsubscribe the current user from a specific merchant")
    @DeleteMapping("/{merchantId}")
    public ResponseEntity<SingleResponse<Void>> unsubscribeMerchant(
            @Parameter(description = "UUID of the merchant to unsubscribe from", required = true) @PathVariable UUID merchantId) {
        subscribedMerchantService.unsubscribeMerchant(authService.getCurrentUserId(), merchantId);
        return responseFactory.successSingle(null, "Unsubscribe merchant successful");
    }
}
