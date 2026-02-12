package com.fpt.ojt.services.user;

import java.util.List;

import com.fpt.ojt.services.dtos.CountryDto;

public interface CountryService {
    List<CountryDto> getCountries();
    boolean isPhoneCodeExists(String phoneCode);
}
