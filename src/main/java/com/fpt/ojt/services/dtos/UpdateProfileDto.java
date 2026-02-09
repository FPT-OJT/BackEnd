package com.fpt.ojt.services.dtos;

import lombok.Data;

@Data
public class UpdateProfileDto {
    private String firstName;
    private String lastName;
    private String countryCode;
    private String phoneNumber;
    private String email;
}
