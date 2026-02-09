package com.fpt.ojt.services.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fpt.ojt.repositories.user.CountryRepository;
import com.fpt.ojt.services.dtos.CountryDto;
import com.fpt.ojt.services.user.CountryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<CountryDto> getCountries() {
        return countryRepository.findAll().stream()
                .map(CountryDto::fromEntity)
                .toList();
    }

    @Override
    public boolean isPhoneCodeExists(String phoneCode) {
        var exists = countryRepository.existsByPhoneCode(phoneCode);
        return exists;
    }
    
}
