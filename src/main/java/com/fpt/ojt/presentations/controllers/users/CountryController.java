package com.fpt.ojt.presentations.controllers.users;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.dtos.CountryDto;
import com.fpt.ojt.services.user.CountryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController  extends AbstractBaseController{
    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<SingleResponse<List<CountryDto>>> getCountries() {
        return responseFactory.successSingle(countryService.getCountries(), "Get countries successful");
    }
}