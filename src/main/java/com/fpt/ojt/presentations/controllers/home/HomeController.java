package com.fpt.ojt.presentations.controllers.home;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Tag(name = "Home Screen", description = "Home Screen API endpoints")
public class HomeController extends AbstractBaseController {

    @GetMapping("/test")
    @Operation(summary = "Test protected endpoint", description = "Test protected endpoint, return a string to confirm")
    public ResponseEntity<SingleResponse<Void>> test(
    ) {
        return responseFactory.sendSingle(null, "Test successful", HttpStatus.OK);
    }
}
