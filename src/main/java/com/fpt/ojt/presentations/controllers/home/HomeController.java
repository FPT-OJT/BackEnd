package com.fpt.ojt.presentations.controllers.home;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;
import com.fpt.ojt.services.merchants.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Tag(name = "Home Screen", description = "Home Screen API endpoints")
public class HomeController extends AbstractBaseController {

    private final MerchantService merchantService;

    @GetMapping
    @Operation(summary = "Home page endpoint", description = "Get homepage")
    public ResponseEntity<SingleResponse<HomePageResponse>> test(
    ) throws ExecutionException, InterruptedException {
        return responseFactory.successSingle(
                merchantService.getHomePage(),
                "Retrieve Home page successfully"
        );
    }
}
