package com.fpt.ojt.services.merchants;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;
import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse.MerchantOffer;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface MerchantService {
    HomePageResponse getHomePage() throws ExecutionException, InterruptedException;

    List<MerchantOffer> getBestOffers();
}
