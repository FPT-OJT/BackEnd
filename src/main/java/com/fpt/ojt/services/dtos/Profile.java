package com.fpt.ojt.services.dtos;

import java.util.UUID;

import com.fpt.ojt.models.postgres.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Profile {
    private UUID id;
    private String firstName;
    private String lastName;
    private String countryPhoneCode;
    private String phoneNumber;
    private String email;

    public static Profile fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return Profile.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .countryPhoneCode(user.getCountryPhoneCode())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }
}
