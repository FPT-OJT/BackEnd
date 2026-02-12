package com.fpt.ojt.services.dtos;

import java.util.UUID;

import com.fpt.ojt.models.postgres.card.Country;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryDto {
    private String name;
    private String isoCode;
    private String phoneCode;

    public static CountryDto fromEntity(Country country) {
        return CountryDto.builder()
                .name(country.getName())
                .isoCode(country.getIsoCode())
                .phoneCode(country.getPhoneCode())
                .build();
    }
}
