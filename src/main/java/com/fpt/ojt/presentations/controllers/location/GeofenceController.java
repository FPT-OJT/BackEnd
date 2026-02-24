package com.fpt.ojt.presentations.controllers.location;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.dtos.Coordinate;
import com.fpt.ojt.services.dtos.locations.GeofenceRegistrationDto;
import com.fpt.ojt.services.location.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/geofence")
@RequiredArgsConstructor
public class GeofenceController extends AbstractBaseController {
    private final LocationService locationService;

    @GetMapping("/candidates")
    public ResponseEntity<SingleResponse<List<GeofenceRegistrationDto>>> getGeofenceCandidates(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        Optional<Coordinate> userLocation = Optional.empty();
        if (latitude != null && longitude != null && latitude != 0 && longitude != 0) {
            userLocation = Optional.of(Coordinate.builder().latitude(latitude).longitude(longitude).build());
        }
        return responseFactory.successSingle(locationService.getGeofenceRegistrations(userLocation),
                "Get geofence candidates successfully");
    }
}
