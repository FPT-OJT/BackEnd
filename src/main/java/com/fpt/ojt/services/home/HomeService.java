package com.fpt.ojt.services.home;

import java.util.concurrent.ExecutionException;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;
import com.fpt.ojt.services.dtos.HomeParam;


public interface HomeService {
    HomePageResponse getHomeData(HomeParam homeParam) throws ExecutionException, InterruptedException;
}
