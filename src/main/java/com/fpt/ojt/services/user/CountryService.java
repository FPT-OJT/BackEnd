package com.fpt.ojt.services.user;

import com.fpt.ojt.services.dtos.CountryDto;
import java.util.List;

public interface CountryService {
    List<CountryDto> getCountries();

    boolean isPhoneCodeExists(String phoneCode);
}
