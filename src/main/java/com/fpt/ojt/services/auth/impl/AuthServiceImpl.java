package com.fpt.ojt.services.auth.impl;

import com.fpt.ojt.exceptions.UnAuthorizedException;
import com.fpt.ojt.infrastructure.securities.UserPrincipal;
import com.fpt.ojt.models.postgres.user.User;
import com.fpt.ojt.repositories.user.UserRepository;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.dtos.UserDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UUID getCurrentUserId() {
        Object principal = getPrincipal();

        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).userId();
        }

        if (principal instanceof UUID) {
            return (UUID) principal;
        }

        throw new RuntimeException("Principal type is not supported or ID is null");
    }

    private Object getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getPrincipal();
    }

    @Override
    public UserDto getCurrentUser() {
        User user = userRepository
                .findById(getCurrentUserId())
                .orElseThrow(() -> new UnAuthorizedException("You are not authorized to access this resource"));
        return UserDto.fromEntity(user);
    }
}
