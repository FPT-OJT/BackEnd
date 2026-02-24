package com.fpt.ojt.services.merchantdetail;

import com.fpt.ojt.presentations.dtos.responses.merchant.MerchantAgencyCardsDealsResponse;

import java.util.UUID;

public interface MerchantDetailService {
    MerchantAgencyCardsDealsResponse getCardsWithDeals(UUID merchantAgencyId, UUID userId);
}
