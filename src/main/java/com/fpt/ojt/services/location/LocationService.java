package com.fpt.ojt.services.location;

import com.fpt.ojt.services.dtos.Coordinate;
import com.fpt.ojt.services.dtos.locations.GeofenceRegistrationDto;
import java.util.List;
import java.util.Optional;

public interface LocationService {
    Coordinate getCurrentUserLocation();

    List<GeofenceRegistrationDto> getGeofenceRegistrations(Optional<Coordinate> userLocation);
}
