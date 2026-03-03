package com.fpt.ojt.services.auth;

import com.fpt.ojt.services.dtos.UserDto;
import java.util.UUID;

public interface AuthService {
    UUID getCurrentUserId();

    UserDto getCurrentUser();
}
