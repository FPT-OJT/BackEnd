package com.fpt.ojt.presentations.controllers.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.auth.AuthService;
import com.fpt.ojt.services.dtos.Profile;
import com.fpt.ojt.services.dtos.UpdateProfileDto;
import com.fpt.ojt.services.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class ProfileController extends AbstractBaseController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<SingleResponse<Profile>> getProfile() {
        return responseFactory.successSingle(userService.getProfileById(authService.getCurrentUserId()),
                "Get profile successful");
    }

    @PatchMapping
    public ResponseEntity<SingleResponse<Profile>> updateProfile(
            @RequestBody @Valid UpdateProfileDto updateProfileDto) {
        userService.updateProfile(authService.getCurrentUserId(), updateProfileDto);
        return responseFactory.successSingle(null, "Update profile successful");
    }
}
