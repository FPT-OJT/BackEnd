package com.fpt.ojt.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NearestAgencyDto {
    private String brandName;
    private String agencyName;
    private Double latitude;
    private Double longitude;
    private String logoUrl;
    private String description;
    private Double distanceMeters;
 
}
