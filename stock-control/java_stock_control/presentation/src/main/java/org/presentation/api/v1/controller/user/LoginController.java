package org.presentation.api.v1.controller.user;

import org.application.user.UserService;
import org.domain.user.User;
import org.domain.user.UserDTO;
import org.security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class LoginController {
    private final UserService service;
    private final JWTService jwtService;

    @Autowired
    public LoginController(UserService service, JWTService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User persistedUser = this.service.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(persistedUser);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserDTO> login(@RequestBody User user) {
        UserDTO responseDTO;
        User foundUser = service.findUser(user.getUserName(), user.getPassword());

        if (foundUser != null && foundUser.getPassword() != null) {
            String token = jwtService.generateToken(foundUser);
            responseDTO = service.buildDTO(foundUser, token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDTO);
        }
        if (foundUser != null && foundUser.getPassword() == null){
            responseDTO = UserDTO.builder().build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
