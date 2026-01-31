package com.fpt.ojt.services.merchants;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;

import java.util.concurrent.ExecutionException;

public interface MerchantService {
    HomePageResponse getHomePage() throws ExecutionException, InterruptedException;
}
