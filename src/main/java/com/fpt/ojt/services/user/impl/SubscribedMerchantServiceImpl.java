package com.fpt.ojt.services.user.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.fpt.ojt.exceptions.DuplicateException;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.models.postgres.user.SubscribedMerchant;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.repositories.merchant.MerchantRepository;
import com.fpt.ojt.repositories.user.SubscribedMerchantRepository;
import com.fpt.ojt.repositories.user.UserRepository;
import com.fpt.ojt.services.dtos.SubscribedMerchantAgencyDto;
import com.fpt.ojt.services.dtos.SubscribedMerchantDto;
import com.fpt.ojt.services.user.SubscribedMerchantService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribedMerchantServiceImpl implements SubscribedMerchantService {
    private final SubscribedMerchantRepository subscribedMerchantRepository;
    private final UserRepository userRepository;
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
    public void subscribeMerchantAgency(UUID userId, UUID merchantAgencyId) {

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var userEntityFuture = executor.submit(
                    () -> userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found")));
            var merchantAgencyEntityFuture = executor.submit(() -> merchantAgencyRepository.findById(merchantAgencyId)
                    .orElseThrow(() -> new NotFoundException("Merchant agency not found")));
            var existsFuture = executor.submit(
                    () -> subscribedMerchantRepository.existsByUserIdAndMerchantAgencyId(userId, merchantAgencyId));
            var subscribedMerchantFuture = executor.submit(() -> {
                var userEntity = userEntityFuture.get();
                var merchantAgencyEntity = merchantAgencyEntityFuture.get();
                var exists = existsFuture.get();
                if (exists) {
                    throw new DuplicateException("Merchant agency already subscribed");
                }
                return SubscribedMerchant.builder().user(userEntity).merchantAgency(merchantAgencyEntity).build();
            });
            subscribedMerchantRepository.save(subscribedMerchantFuture.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unsubscribeMerchantAgency(UUID userId, UUID merchantAgencyId) {
        var subscribedMerchant = subscribedMerchantRepository.findByUserIdAndMerchantAgencyId(userId, merchantAgencyId)
                .orElseThrow(() -> new NotFoundException("Merchant agency not subscribed"));
        subscribedMerchantRepository.delete(subscribedMerchant);
    }

    @Override
    @Transactional
    public void subscribeMerchant(UUID userId, UUID merchantId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        merchantRepository.findById(merchantId)
                .orElseThrow(() -> new NotFoundException("Merchant not found"));

        var agencies = merchantAgencyRepository.findByMerchantId(merchantId);
        if (agencies.isEmpty())
            return;

        var subscribedAgencyIds = subscribedMerchantRepository.findMerchantAgencyIdsByUserIdAndMerchantId(userId,
                merchantId);

        var toSubscribe = agencies.stream()
                .filter(a -> !subscribedAgencyIds.contains(a.getId()))
                .map(a -> SubscribedMerchant.builder()
                        .user(user)
                        .merchantAgency(a)
                        .build())
                .toList();

        if (!toSubscribe.isEmpty()) {
            subscribedMerchantRepository.saveAll(toSubscribe);
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
