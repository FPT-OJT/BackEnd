package com.fpt.ojt.presentations.controllers.health;

import com.fpt.ojt.presentations.controllers.base.AbstractBaseController;
import com.fpt.ojt.presentations.dtos.responses.SingleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/health")
@Tag(name = "Health", description = "Application health check")
public class HealthController extends AbstractBaseController {

    @GetMapping
    @Operation(summary = "Health check", description = "Returns application status and current server time")
    public ResponseEntity<SingleResponse<Map<String, Object>>> health() {
        Map<String, Object> data =
                Map.of("status", "UP", "timestamp", Instant.now().toString());
        return responseFactory.successSingle(data, "Application is healthy");
    }
}
