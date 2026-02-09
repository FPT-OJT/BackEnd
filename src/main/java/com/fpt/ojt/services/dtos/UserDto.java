package com.fpt.ojt.services.dtos;

import java.util.UUID;

import com.fpt.ojt.models.enums.EnumConstants;
import com.fpt.ojt.models.postgres.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private UUID id;
    private String userName;
    private String email;
    private EnumConstants.RoleEnum role;
    private String firstName;
    private String lastName;
    private String countryPhoneCode;
    private String phoneNumber;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .countryPhoneCode(user.getCountryPhoneCode())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
