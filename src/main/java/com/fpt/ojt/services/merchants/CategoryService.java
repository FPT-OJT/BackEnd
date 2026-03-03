package com.fpt.ojt.services.merchants;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse.MerchantCategory;
import java.util.List;

public interface CategoryService {
    public List<MerchantCategory> getMerchantCategories(int limit);
}
