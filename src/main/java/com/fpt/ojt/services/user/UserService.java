package com.fpt.ojt.services.user;

import com.fpt.ojt.models.enums.EnumConstants;
import com.fpt.ojt.models.postgres.user.User;
import com.fpt.ojt.services.dtos.Profile;
import com.fpt.ojt.services.dtos.UpdateProfileDto;
import java.util.UUID;

public interface UserService {
    void createUser(EnumConstants.RoleEnum roleEnum, String firstName, String lastName, String email);

    void updateUser(UUID userId, String firstName, String lastName);

    User getUserByEmail(String email);

    Profile getProfileById(UUID userId);

    void updateProfile(UUID userId, UpdateProfileDto updateProfileDto);
}
