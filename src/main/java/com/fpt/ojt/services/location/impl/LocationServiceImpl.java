package com.fpt.ojt.services.location.impl;

import com.fpt.ojt.exceptions.BadRequestException;
import com.fpt.ojt.models.postgres.merchant.GeofenceAgencyProjection;
import com.fpt.ojt.repositories.merchant.MerchantAgencyRepository;
import com.fpt.ojt.services.dtos.Coordinate;
import com.fpt.ojt.services.dtos.locations.GeofenceRegistrationDto;
import com.fpt.ojt.services.location.LocationService;
import com.fpt.ojt.utils.IpUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final DatabaseReader databaseReader;
    private final IpUtils ipUtils;
    private static final int GEOFENCE_REGISTRATIONS_LIMIT = 50;
    private final MerchantAgencyRepository merchantAgencyRepository;

    public Coordinate mapFromIpAddress(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = databaseReader.city(ipAddress);

            Double latitude = response.location().latitude();
            Double longitude = response.location().longitude();

            return Coordinate.builder().latitude(latitude).longitude(longitude).build();
        } catch (Exception e) {
            throw new BadRequestException("Failed to map IP address to coordinate: " + e.getMessage());
        }
    }

    @Override
    public Coordinate getCurrentUserLocation() {
        String ip = ipUtils.getClientIp();
        return mapFromIpAddress(ip);
    }

    @Override
    public List<GeofenceRegistrationDto> getGeofenceRegistrations(Optional<Coordinate> userLocation) {
        Coordinate coordinate = userLocation.orElseGet(this::getCurrentUserLocation);
        List<GeofenceAgencyProjection> projections = merchantAgencyRepository.getNearestMerchantAgencies(
                coordinate.getLatitude(), coordinate.getLongitude(), GEOFENCE_REGISTRATIONS_LIMIT);

        return projections.stream()
                .map(projection -> GeofenceRegistrationDto.builder()
                        .id(projection.getId())
                        .latitude(projection.getLatitude())
                        .longitude(projection.getLongitude())
                        .build())
                .collect(Collectors.toList());
    }
}
