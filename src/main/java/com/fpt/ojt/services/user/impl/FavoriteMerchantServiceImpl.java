package com.fpt.ojt.services.user.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fpt.ojt.exceptions.BadRequestException;
import com.fpt.ojt.exceptions.DuplicateException;
import com.fpt.ojt.models.postgres.merchant.MerchantAgency;
import com.fpt.ojt.models.postgres.user.FavoriteMerchant;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.repositories.merchant.MerchantRepository;
import com.fpt.ojt.repositories.user.FavoriteMerchantRepository;
import com.fpt.ojt.repositories.user.UserRepository;
import com.fpt.ojt.services.dtos.FavoriteMerchantDto;
import com.fpt.ojt.services.user.FavoriteMerchantService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
    @Transactional
    public void addFavoriteMerchant(UUID userId, UUID merchantAgencyId) {
       favoriteMerchantRepository.insertOrRestore(userId, merchantAgencyId);
    }

    @Override
    @Transactional
    public void removeFavoriteMerchant(UUID userId, UUID favoriteMerchantId) {
        favoriteMerchantRepository.deleteByUserIdAndMerchantAgencyId(userId, favoriteMerchantId);
       
    }
}
