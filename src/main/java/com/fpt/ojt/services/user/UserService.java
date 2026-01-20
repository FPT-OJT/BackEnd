package com.fpt.ojt.services.user;

import com.fpt.ojt.constants.Constants;
import com.fpt.ojt.models.User;

import java.util.UUID;

public interface UserService {
    void createUser(Constants.RoleEnum roleEnum, String googleId, String firstName, String lastName, String userName, String email, String password);
    void updateUser(UUID userId, String googleId, String email, String userName, String firstName, String lastName);
    void updateNewPassword(UUID userId, String hashPassword);
    User handleUpdateGoogleCredential(String googleId, String email, String firstName, String lastName, String pictureUrl);
    User getUserByUserName(String userName);
    User getUserByEmail(String email);
}