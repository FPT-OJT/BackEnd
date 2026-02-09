package com.fpt.ojt.services.dtos;

import lombok.Data;

@Data
public class UpdateProfileDto {
    private String firstName;
    private String lastName;
    private String countryPhoneCode;
    private String phoneNumber;
    private String email;
}
