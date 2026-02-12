package com.fpt.ojt.services.merchants;

import java.util.List;

import com.fpt.ojt.services.dtos.MerchantSort;
import com.fpt.ojt.services.dtos.NearestAgencyDto;

public interface MerchantAgencyService {
    List<NearestAgencyDto> findNearestAgencies(String keyword, Double latitude, Double longitude, int limit, MerchantSort sort);
}
