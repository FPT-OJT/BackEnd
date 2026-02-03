package com.fpt.ojt.services.merchants.impl;

import java.time.Duration;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fpt.ojt.models.postgres.merchant.NearestAgencyProjection;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.services.dtos.NearestAgencyDto;
import com.fpt.ojt.services.merchants.MerchantAgencyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MerchantAgencyServiceImpl implements MerchantAgencyService {
    private final MerchantAgencyRepository merchantAgencyRepository;
    private final NearestMerchantCacheKeyGenerator geoGridUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration CACHE_TTL = Duration.ofMinutes(30);

    @Cacheable(cacheNames = "nearestAgency", keyGenerator = "nearestMerchantCacheKeyGenerator")
    @Override
    public List<NearestAgencyDto> findNearestAgencies(String keyword, Double latitude, Double longitude, int limit) {

        var result = merchantAgencyRepository.searchNearestAgencies(keyword, latitude, longitude, limit);
        return result.stream()
                .map(this::mapToNearestAgencyDto)
                .toList();
    }

    private NearestAgencyDto mapToNearestAgencyDto(NearestAgencyProjection projection) {
        return NearestAgencyDto.builder()
                .brandName(projection.getBrandName())
                .agencyName(projection.getAgencyName())
                .latitude(projection.getLatitude())
                .longitude(projection.getLongitude())
                .logoUrl(projection.getLogoUrl())
                .description(projection.getDescription())
                .distanceMeters(projection.getDistanceMeters())
                .build();
    }

}
