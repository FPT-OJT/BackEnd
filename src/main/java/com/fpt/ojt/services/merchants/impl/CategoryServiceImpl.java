package com.fpt.ojt.services.merchants.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt.ojt.repositories.merchant.MerchantCategoryRepository;
import com.fpt.ojt.services.merchants.CategoryService;

import lombok.RequiredArgsConstructor;
import java.util.List;

import com.fpt.ojt.infrastructure.configs.CacheNames;
import com.fpt.ojt.models.postgres.merchant.MerchantCategory;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final MerchantCategoryRepository merchantCategoryRepository;

    @Override
    @Cacheable(value = CacheNames.MERCHANT_CATEGORIES_CACHE_NAME, key = "#limit")
    public List<com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse.MerchantCategory> getMerchantCategories(
            int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<MerchantCategory> merchantCategories = merchantCategoryRepository.findAllByDeletedAtIsNull(pageable);
        if (merchantCategories == null || !merchantCategories.hasContent()) {
            return List.of();
        }
        return merchantCategories.getContent().stream()
                .map(category -> com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse.MerchantCategory
                        .builder()
                        .id(category.getId())
                        .name(category.getCategoryName())
                        .imageUrl(category.getImageUrl())
                        .build())
                .toList();
    }

}
