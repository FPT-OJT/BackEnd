package com.fpt.ojt.services.home;

import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;
import com.fpt.ojt.services.dtos.HomeParam;
import java.util.concurrent.ExecutionException;

public interface HomeService {
    HomePageResponse getHomeData(HomeParam homeParam) throws ExecutionException, InterruptedException;
}
