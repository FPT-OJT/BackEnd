package com.fpt.ojt.presentations.controllers.auth;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API endpoints")
public class AuthController extends AbstractBaseController {
    private final AuthService authService;

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout current user and invalidate tokens")
    public ResponseEntity<SingleResponse<Void>> logout() {
        authService.logout();
        return responseFactory.successSingle(null, "Logout successful");
    }
}
