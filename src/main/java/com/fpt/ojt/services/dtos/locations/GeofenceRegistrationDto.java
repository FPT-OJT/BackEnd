package com.fpt.ojt.services.dtos.locations;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class GeofenceRegistrationDto {

    private UUID id;
    private double latitude;
    private double longitude;
    @Default
    private double metersRadius = 100;
}