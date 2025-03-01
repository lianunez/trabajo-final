package org.presentation.api.v1.controller.healthCheck;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/liveness")
    public ResponseEntity<String> healthCheck() {
        System.out.println("Liveness check");
        String message = "Service it's running";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
