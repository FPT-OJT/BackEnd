package com.fpt.ojt.services.user;

import com.fpt.ojt.constants.Constants;
import com.fpt.ojt.models.User;

public interface UserService {
    void createUser(Constants.RoleEnum roleEnum, String firstName, String lastName, String userName, String password);
    User getUserByUserName(String userName);
}
