package com.fpt.ojt.services.user.impl;

import com.fpt.ojt.constants.Constants;
import com.fpt.ojt.models.User;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.repositories.UserRepository;
import com.fpt.ojt.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(Constants.RoleEnum roleEnum,
                           String firstName,
                           String lastName,
                           String userName, String passwordEncoded) {
        try {
            userRepository.save(
                    User.builder()
                            .role(roleEnum)
                            .firstName(firstName)
                            .lastName(lastName)
                            .userName(userName)
                            .password(passwordEncoded)
                            .build()
            );
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create user", exception);
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
}
