package com.fpt.ojt.presentations.controllers.home;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;
import com.fpt.ojt.services.dtos.Coordinate;
import com.fpt.ojt.services.dtos.HomeParam;
import com.fpt.ojt.services.home.HomeService;
import com.fpt.ojt.services.location.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Tag(name = "Home Screen", description = "Home Screen API endpoints")
@Slf4j
public class HomeController extends AbstractBaseController {

    private final HomeService homeService;
    private final LocationService locationService;

    @GetMapping
    @Operation(summary = "Home page endpoint", description = "Get homepage")
    public ResponseEntity<SingleResponse<HomePageResponse>> getHomeData(
            @RequestParam(required = false, name = "lat") Double latitude,
            @RequestParam(required = false, name = "long") Double longitude,
            HttpServletRequest request)
            throws ExecutionException, InterruptedException {
        Optional<Coordinate> userLocation;
        log.info("latitude: {}, longitude: {}", latitude, longitude);
        if (latitude != null && longitude != null) {
            userLocation = Optional.of(
                    Coordinate.builder().latitude(latitude).longitude(longitude).build());
        } else {
            userLocation = Optional.empty();
        }
        var homeData = HomeParam.builder()
                .userLocation(userLocation)
                .ipAddress(request.getRemoteAddr())
                .build();
        return responseFactory.successSingle(homeService.getHomeData(homeData), "Retrieve Home page  successfully");
    }

    @GetMapping("/location")
    @Operation(summary = "Location endpoint", description = "Get location")
    public ResponseEntity<SingleResponse<Coordinate>> getLocation(@RequestParam String ipAddress) {
        return responseFactory.successSingle(
                locationService.getCurrentUserLocation(), "Retrieve location successfully");
    }
}
