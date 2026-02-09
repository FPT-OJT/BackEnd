package com.fpt.ojt.services.user;

import java.util.List;
import java.util.UUID;

import com.fpt.ojt.services.dtos.SubscribedMerchantAgencyDto;
import com.fpt.ojt.services.dtos.SubscribedMerchantDto;

public interface SubscribedMerchantService {
    List<SubscribedMerchantDto> getSubscribedMerchants(UUID userId);
    
    List<SubscribedMerchantAgencyDto> getSubscribedMerchantAgencies(UUID userId);

    void subscribeMerchantAgency(UUID userId, UUID merchantAgencyId);

    void unsubscribeMerchantAgency(UUID userId, UUID merchantAgencyId);

    void subscribeMerchant(UUID userId, UUID merchantId);

    void unsubscribeMerchant(UUID userId, UUID merchantId);
}
