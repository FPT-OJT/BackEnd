package com.fpt.ojt.services.user.impl;

import com.fpt.ojt.constants.Constants;
import com.fpt.ojt.exceptions.DuplicateException;
import com.fpt.ojt.models.User;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.repositories.UserRepository;
import com.fpt.ojt.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(Constants.RoleEnum roleEnum,
                           String googleId,
                           String firstName,
                           String lastName,
                           String userName,
                           String email,
                           String passwordEncoded) {
        try {
            userRepository.save(
                    User.builder()
                            .role(roleEnum)
                            .googleId(googleId)
                            .firstName(firstName)
                            .lastName(lastName)
                            .userName(userName)
                            .email(email)
                            .password(passwordEncoded)
                            .build()
            );
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create user", exception);
        }
    }

    @Override
    public void updateUser(UUID userId, String googleId, String email, String userName, String firstName, String lastName) {
        try {
            List<User> users = userRepository.findAllByGoogleIdOrEmail(googleId, email);
            if (!users.isEmpty()) {
                throw new DuplicateException("User with this google id or email is already exist");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setGoogleId(googleId);

            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public User getUserByUserName(String userName) {
        try {
            User user = userRepository.findByUserName(userName);
            if (user == null) {
                throw new NotFoundException("User not found with name " + userName);
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user by user name",e);
        }
    }

    @Override
    public User handleUpdateGoogleCredential(String googleId,
                                             String email,
                                             String firstName, String lastName,
                                             String pictureUrl) {
        // TODO: Handle picture here
        User user = userRepository.findByGoogleId(googleId);
        if (user == null) {
            // Handle create new
            createUser(
                    Constants.RoleEnum.CUSTOMER,
                    googleId,
                    firstName, lastName,
                    null,
                    email,
                    null
            );
        } else {
            updateUser(
                    user.getId(),
                    googleId,
                    email,
                    null,
                    firstName, lastName
            );
        }

        return userRepository.findByGoogleId(googleId);
    }
}
