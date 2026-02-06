package com.fpt.ojt.services.merchants;

import java.util.List;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse.MerchantCategory;


public interface CategoryService {
    public List<MerchantCategory> getMerchantCategories(int limit);
}
