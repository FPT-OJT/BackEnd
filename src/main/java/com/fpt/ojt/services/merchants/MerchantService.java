package com.fpt.ojt.services.merchants;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;

import java.util.List;
import java.util.UUID;

public interface MerchantService {
    public List<HomePageResponse.MerchantOffer> getMerchantOffers(int limit, UUID currentUserId);

}
