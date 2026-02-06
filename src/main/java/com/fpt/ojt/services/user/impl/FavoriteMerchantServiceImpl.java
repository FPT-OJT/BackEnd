package com.fpt.ojt.services.user.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.user.FavoriteMerchant;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.repositories.merchant.MerchantRepository;
import com.fpt.ojt.repositories.user.FavoriteMerchantRepository;
import com.fpt.ojt.repositories.user.UserRepository;
import com.fpt.ojt.services.dtos.FavoriteMerchantDto;
import com.fpt.ojt.services.user.FavoriteMerchantService;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteMerchantServiceImpl implements FavoriteMerchantService {
    private final FavoriteMerchantRepository favoriteMerchantRepository;
    private final UserRepository userRepository;
    private final MerchantAgencyRepository merchantAgencyRepository;

    @Override
    public List<FavoriteMerchantDto> getFavoriteMerchants(UUID userId) {
        return favoriteMerchantRepository.findDistinctFavoriteMerchantsByUserId(userId).stream()
                .map(FavoriteMerchantDto::fromEntity)
                .toList();
    }

    @Override
    public void addFavoriteMerchant(UUID userId, UUID merchantAgencyId) {
        var userEntity = userRepository.findById(userId).orElseThrow();
        var merchantEntity = merchantAgencyRepository.findById(merchantAgencyId).orElseThrow();
        var favoriteMerchant = FavoriteMerchant.builder()
                .user(userEntity)
                .merchantAgency(merchantEntity)
                .build();
        favoriteMerchantRepository.save(favoriteMerchant);
    }

    @Override
    public void removeFavoriteMerchant(UUID userId, UUID favoriteMerchantId) {
        favoriteMerchantRepository.deleteByUserIdAndId(userId, favoriteMerchantId);
    }
}
