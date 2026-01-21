package com.fpt.ojt.services.user.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpt.ojt.constants.Constants;
import com.fpt.ojt.exceptions.DuplicateException;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.models.User;
import com.fpt.ojt.repositories.UserRepository;
import com.fpt.ojt.services.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        // Check for duplicate username
        if (userName != null && userRepository.existsByUserName(userName)) {
            throw new DuplicateException("Username '" + userName + "' is already taken");
        }

        // Check for duplicate email
        if (email != null && userRepository.existsByEmail(email)) {
            throw new DuplicateException("Email '" + email + "' is already registered");
        }

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
                            .build());
        } catch (Exception exception) {
            log.error("Failed to create user: {}", exception.getMessage(), exception);
            throw new RuntimeException("Failed to create user", exception);
        }
    }

    @Override
    public void updateUser(UUID userId, String userName, String firstName,
            String lastName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        user.setFirstName(firstName);
        user.setLastName(lastName);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateNewPassword(UUID userId, String hashPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        user.setPassword(hashPassword);
        userRepository.save(user);
    }

    @Override
    public User getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new NotFoundException("User not found with username " + userName);
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found with email " + email);
        }
        return user;
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
                    email,
                    email,
                    null);
        } else {
            updateUser(
                    user.getId(),
                    user.getUserName(),
                    firstName, lastName);
        }

        return userRepository.findByGoogleId(googleId);
    }
}
