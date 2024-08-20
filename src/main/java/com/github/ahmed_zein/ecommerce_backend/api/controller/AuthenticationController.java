package com.github.ahmed_zein.ecommerce_backend.api.controller;

import com.github.ahmed_zein.ecommerce_backend.api.model.RegistrationBody;
import com.github.ahmed_zein.ecommerce_backend.exception.UserAlreadyExists;
import com.github.ahmed_zein.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            var user = userService.registerUser(registrationBody);
            System.out.println(user);
            return ResponseEntity.ok().body(user);
        } catch (UserAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
}
