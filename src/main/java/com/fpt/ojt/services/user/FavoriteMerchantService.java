package com.fpt.ojt.services.user;

import java.util.List;
import java.util.UUID;

import com.fpt.ojt.services.dtos.FavoriteMerchantDto;

public interface FavoriteMerchantService {
    List<FavoriteMerchantDto> getFavoriteMerchants(UUID userId);

    void addFavoriteMerchant(UUID userId, UUID merchantAgencyId);

    void removeFavoriteMerchant(UUID userId, UUID merchantId);
}
