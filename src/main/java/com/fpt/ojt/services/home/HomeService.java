package com.fpt.ojt.services.home;

import java.util.concurrent.ExecutionException;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;


public interface HomeService {
    HomePageResponse getHomeData() throws ExecutionException, InterruptedException;
}
