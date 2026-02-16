package com.fpt.ojt.services.home.impl;

import java.net.InetAddress;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ojt.exceptions.BadRequestException;
import com.fpt.ojt.services.dtos.Coordinate;
import com.fpt.ojt.services.home.LocationService;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final DatabaseReader databaseReader;

    @Override
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
}
