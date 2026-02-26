package com.fpt.ojt.services.merchants.impl;

import com.fpt.ojt.infrastructure.configs.CacheNames;
import com.fpt.ojt.models.postgres.merchant.NearestAgencyProjection;
import com.fpt.ojt.repositories.deal.MerchantDealRepository;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.services.dtos.MerchantDealDto;
import com.fpt.ojt.services.dtos.MerchantSort;
import com.fpt.ojt.services.dtos.NearestAgencyDto;
import com.fpt.ojt.services.location.LocationService;
import com.fpt.ojt.services.merchants.MerchantAgencyService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantAgencyServiceImpl implements MerchantAgencyService {

    private final MerchantAgencyRepository merchantAgencyRepository;
    private final LocationService locationService;
    private final MerchantDealRepository merchantDealRepository;

    @Cacheable(
            cacheNames = CacheNames.SEARCH_NEAREST_MERCHANT_CACHE_NAME,
            keyGenerator = "nearestMerchantCacheKeyGenerator")
    @Override
    public List<NearestAgencyDto> findNearestAgencies(
            String keyword, Double latitude, Double longitude, int limit, MerchantSort sort) {
        log.info(
                "Searching nearest agencies with keyword: {}, latitude: {}, longitude: {}, limit: {}, sort: {}",
                keyword,
                latitude,
                longitude,
                limit,
                sort);
        if (latitude == 0 || longitude == 0) {
            var coordinate = locationService.getCurrentUserLocation();
            latitude = coordinate.getLatitude();
            longitude = coordinate.getLongitude();
        }

        var result = merchantAgencyRepository.searchNearestAgenciesWithSort(
                keyword, latitude, longitude, limit, sort.name());
        return result.stream().map(this::mapToNearestAgencyDto).toList();
    }

    private NearestAgencyDto mapToNearestAgencyDto(NearestAgencyProjection projection) {
        return NearestAgencyDto.builder()
                .agencyId(projection.getAgencyId())
                .merchantName(projection.getMerchantName())
                .agencyName(projection.getAgencyName())
                .latitude(projection.getLatitude())
                .longitude(projection.getLongitude())
                .logoUrl(projection.getLogoUrl())
                .description(projection.getDescription())
                .distanceMeters(projection.getDistanceMeters())
                .build();
    }

    @Override
    public List<MerchantDealDto> findAvailableDeals(UUID merchantAgencyId) {
        var deals = merchantDealRepository.findAvailableByMerchantAgencyId(merchantAgencyId);
        return deals.stream().map(this::mapToMerchantDealDto).toList();
    }

    private MerchantDealDto mapToMerchantDealDto(com.fpt.ojt.models.postgres.deal.MerchantDeal deal) {
        return MerchantDealDto.builder()
                .id(deal.getId())
                .merchantName(deal.getMerchantAgency().getMerchant().getName())
                .logoUrl(deal.getMerchantAgency().getMerchant().getLogoUrl())
                .merchantDescription(deal.getMerchantAgency().getMerchant().getDescription())
                .agencyName(deal.getMerchantAgency().getName())
                .dealName(deal.getDealName())
                .discountRate(deal.getDiscountRate())
                .cashbackRate(deal.getCashbackRate())
                .pointsMultiplier(deal.getPointsMultiplier())
                .description(deal.getDescription())
                .build();
    }
}
