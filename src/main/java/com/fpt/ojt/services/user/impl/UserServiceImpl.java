package com.fpt.ojt.services.user.impl;

import com.fpt.ojt.exceptions.DuplicateException;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.models.enums.EnumConstants;
import com.fpt.ojt.models.postgres.user.User;
import com.fpt.ojt.repositories.user.UserRepository;
import com.fpt.ojt.services.dtos.Profile;
import com.fpt.ojt.services.dtos.UpdateProfileDto;
import com.fpt.ojt.services.user.CountryService;
import com.fpt.ojt.services.user.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CountryService countryService;

    @Override
    public void createUser(EnumConstants.RoleEnum roleEnum, String firstName, String lastName, String email) {
        // Check for duplicate email
        if (email != null && userRepository.existsByEmail(email)) {
            throw new DuplicateException("Email '" + email + "' is already registered");
        }

        try {
            userRepository.save(User.builder()
                    .role(roleEnum)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .build());
        } catch (Exception exception) {
            log.error("Failed to create user: {}", exception.getMessage(), exception);
            throw new RuntimeException("Failed to create user", exception);
        }
    }

    @Override
    public void updateUser(UUID userId, String firstName, String lastName) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        user.setFirstName(firstName);
        user.setLastName(lastName);

        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User handleUpdateGoogleCredential(
            String googleId, String email, String firstName, String lastName, String pictureUrl) {
        // TODO: Handle picture here
        User user = userRepository.findByGoogleId(googleId);
        if (user == null) {
            // Handle create new
            createUser(EnumConstants.RoleEnum.CUSTOMER, firstName, lastName, email);
        } else {
            updateUser(user.getId(), firstName, lastName);
        }

        return userRepository.findByGoogleId(googleId);
    }

    @Override
    public Profile getProfileById(UUID userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        return Profile.fromEntity(user);
    }

    @Override
    public void updateProfile(UUID userId, UpdateProfileDto updateProfileDto) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        var existByEmail = userRepository.findByEmail(updateProfileDto.getEmail());
        if (existByEmail != null && !existByEmail.getId().equals(userId)) {
            throw new DuplicateException("Email '" + updateProfileDto.getEmail() + "' is already registered");
        }
        boolean isPhoneCodeExists = countryService.isPhoneCodeExists(updateProfileDto.getCountryPhoneCode());
        if (!isPhoneCodeExists) {
            throw new NotFoundException(
                    "Country phone code '" + updateProfileDto.getCountryPhoneCode() + "' is not valid");
        }
        user.setFirstName(updateProfileDto.getFirstName());
        user.setLastName(updateProfileDto.getLastName());
        user.setCountryPhoneCode(updateProfileDto.getCountryPhoneCode());
        user.setPhoneNumber(updateProfileDto.getPhoneNumber());
        user.setEmail(updateProfileDto.getEmail());
        userRepository.save(user);
    }
}
