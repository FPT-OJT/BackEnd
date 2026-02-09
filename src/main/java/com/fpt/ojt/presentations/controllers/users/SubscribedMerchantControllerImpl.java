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

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/users/subscribed-merchants")
@RequiredArgsConstructor
public class SubscribedMerchantControllerImpl extends AbstractBaseController {
    private final SubscribedMerchantService subscribedMerchantService;
    private final AuthService authService;
    
    @GetMapping
    public ResponseEntity<SingleResponse<List<SubscribedMerchantDto >>> getSubscribedMerchants() {
        return responseFactory.successSingle(subscribedMerchantService.getSubscribedMerchants(authService.getCurrentUserId()),
                "Get subscribed merchants successful");
    }
    @GetMapping("/agencies")
    public ResponseEntity<SingleResponse<List<SubscribedMerchantAgencyDto>>> getSubscribedMerchantAgencies() {
        return responseFactory.successSingle(subscribedMerchantService.getSubscribedMerchantAgencies(authService.getCurrentUserId()),
                "Get subscribed merchant agencies successful");
    }
    @PostMapping("/agencies/{merchantAgencyId}")
    public ResponseEntity<SingleResponse<Void>> subscribeMerchantAgency(@PathVariable UUID merchantAgencyId) {
        subscribedMerchantService.subscribeMerchantAgency(authService.getCurrentUserId(), merchantAgencyId);
        return responseFactory.successSingle(null, "Subscribe merchant agency successful");
    }
    @DeleteMapping("/agencies/{merchantAgencyId}")
    public ResponseEntity<SingleResponse<Void>> unsubscribeMerchantAgency(@PathVariable UUID merchantAgencyId) {
        subscribedMerchantService.unsubscribeMerchantAgency(authService.getCurrentUserId(), merchantAgencyId);
        return responseFactory.successSingle(null, "Unsubscribe merchant agency successful");
    }
    @PostMapping("/{merchantId}")
    public ResponseEntity<SingleResponse<Void>> subscribeMerchant(@PathVariable UUID merchantId) {
        subscribedMerchantService.subscribeMerchant(authService.getCurrentUserId(), merchantId);
        return responseFactory.successSingle(null, "Subscribe merchant successful");
    }
    @DeleteMapping("/{merchantId}")
    public ResponseEntity<SingleResponse<Void>> unsubscribeMerchant(@PathVariable UUID merchantId) {
        subscribedMerchantService.unsubscribeMerchant(authService.getCurrentUserId(), merchantId);
        return responseFactory.successSingle(null, "Unsubscribe merchant successful");
    }
}
