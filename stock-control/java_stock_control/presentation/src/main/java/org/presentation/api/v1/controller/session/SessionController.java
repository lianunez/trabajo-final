package org.presentation.api.v1.controller.session;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/session")
public class SessionController {
    @GetMapping(path = "/alive")
    ResponseEntity<HttpStatus> sessionIsAlive(){
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
