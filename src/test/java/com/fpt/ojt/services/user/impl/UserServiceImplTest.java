package com.fpt.ojt.services.user.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fpt.ojt.exceptions.DuplicateException;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.models.enums.EnumConstants;
import com.fpt.ojt.models.postgres.user.User;
import com.fpt.ojt.repositories.user.UserRepository;
import com.fpt.ojt.services.dtos.Profile;
import com.fpt.ojt.services.user.CountryService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private UserServiceImpl userService;

    // ── createUser ──────────────────────────────────────────────────────────────

    @Test
    void createUser_success() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);

        assertThatNoException()
                .isThrownBy(() -> userService.createUser(
                        EnumConstants.RoleEnum.CUSTOMER, "Alice", "Smith", "alice@example.com"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_duplicateEmail_throwsDuplicateException() {
        when(userRepository.existsByEmail("dup@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(
                EnumConstants.RoleEnum.CUSTOMER, "Bob", "Jones", "dup@example.com"))
                .isInstanceOf(DuplicateException.class)
                .hasMessageContaining("dup@example.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_nullEmail_doesNotCheckDuplicate() {
        assertThatNoException()
                .isThrownBy(() -> userService.createUser(
                        EnumConstants.RoleEnum.CUSTOMER, "Carol", "White", null));

        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository).save(any(User.class));
    }

    // ── updateUser ──────────────────────────────────────────────────────────────

    @Test
    void updateUser_success() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .firstName("Old")
                .lastName("Name")
                .role(EnumConstants.RoleEnum.CUSTOMER)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.updateUser(userId, "New", "Name");

        assertThat(user.getFirstName()).isEqualTo("New");
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_userNotFound_throwsNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userId, "X", "Y"))
                .isInstanceOf(NotFoundException.class);
    }

    // ── getUserByEmail ───────────────────────────────────────────────────────────

    @Test
    void getUserByEmail_returnsUser() {
        User user = User.builder()
                .email("test@example.com")
                .role(EnumConstants.RoleEnum.CUSTOMER)
                .firstName("Test")
                .lastName("User")
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByEmail("test@example.com");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserByEmail_notFound_returnsNull() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(null);

        User result = userService.getUserByEmail("nobody@example.com");

        assertThat(result).isNull();
    }

    // ── getProfileById ───────────────────────────────────────────────────────────

    @Test
    void getProfileById_userNotFound_throwsNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getProfileById(userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getProfileById_success_returnsProfile() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .role(EnumConstants.RoleEnum.CUSTOMER)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Profile profile = userService.getProfileById(userId);

        assertThat(profile).isNotNull();
        assertThat(profile.getFirstName()).isEqualTo("Jane");
        assertThat(profile.getLastName()).isEqualTo("Doe");
    }
}
