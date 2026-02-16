package com.fpt.ojt.services.dtos;

import java.util.Optional;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomeParam {
    private Optional<Coordinate> userLocation;
    private String ipAddress;
}
