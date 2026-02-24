package com.fpt.ojt.services.merchants;

import java.util.List;
import java.util.UUID;


import com.fpt.ojt.services.dtos.MerchantDealDto;
import com.fpt.ojt.services.dtos.MerchantSort;
import com.fpt.ojt.services.dtos.NearestAgencyDto;

public interface MerchantAgencyService {
    List<NearestAgencyDto> findNearestAgencies(String keyword, Double latitude, Double longitude, int limit, MerchantSort sort);
    List<MerchantDealDto> findAvailableDeals(UUID merchantAgencyId);
}
