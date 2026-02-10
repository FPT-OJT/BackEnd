package com.fpt.ojt.services.user.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.repositories.merchant.MerchantRepository;
import com.fpt.ojt.repositories.user.SubscribedMerchantRepository;
import com.fpt.ojt.services.dtos.SubscribedMerchantAgencyDto;
import com.fpt.ojt.services.dtos.SubscribedMerchantDto;
import com.fpt.ojt.services.user.SubscribedMerchantService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribedMerchantServiceImpl implements SubscribedMerchantService {
    private final SubscribedMerchantRepository subscribedMerchantRepository;
    private final MerchantAgencyRepository merchantAgencyRepository;
    private final MerchantRepository merchantRepository;

    @Override
    public List<SubscribedMerchantDto> getSubscribedMerchants(UUID userId) {
        return subscribedMerchantRepository.findDistinctSubscribedMerchantsByUserId(userId).stream()
                .map(SubscribedMerchantDto::fromEntity)
                .toList();
    }

    @Override
    public List<SubscribedMerchantAgencyDto> getSubscribedMerchantAgencies(UUID userId) {
        return subscribedMerchantRepository.findDistinctSubscribedMerchantAgenciesByUserId(userId).stream()
                .map(SubscribedMerchantAgencyDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public void subscribeMerchantAgency(UUID userId, UUID merchantAgencyId) {
        subscribedMerchantRepository.insertOrRestore(userId, merchantAgencyId);
    }

    @Override
    @Transactional
    public void unsubscribeMerchantAgency(UUID userId, UUID merchantAgencyId) {
        subscribedMerchantRepository.deleteByUserIdAndMerchantAgencyId(userId, merchantAgencyId);
    }

    @Override
    @Transactional
    public void subscribeMerchant(UUID userId, UUID merchantId) {
        merchantRepository.findById(merchantId)
                .orElseThrow(() -> new NotFoundException("Merchant not found"));

        var agencies = merchantAgencyRepository.findByMerchantId(merchantId);
        if (agencies.isEmpty())
            return;

        for (var agency : agencies) {
            subscribedMerchantRepository.insertOrRestore(userId, agency.getId());
        }
    }

    @Override
    @Transactional
    public void unsubscribeMerchant(UUID userId, UUID merchantId) {
        merchantRepository.findById(merchantId)
                .orElseThrow(() -> new NotFoundException("Merchant not found"));

        subscribedMerchantRepository.deleteByUserIdAndMerchantId(userId, merchantId);
    }

}
