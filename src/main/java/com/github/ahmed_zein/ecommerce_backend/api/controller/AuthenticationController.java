package com.github.ahmed_zein.ecommerce_backend.api.controller;

import com.github.ahmed_zein.ecommerce_backend.api.model.LoginBody;
import com.github.ahmed_zein.ecommerce_backend.api.model.LoginResponse;
import com.github.ahmed_zein.ecommerce_backend.api.model.RegistrationBody;
import com.github.ahmed_zein.ecommerce_backend.exception.UserAlreadyExists;
import com.github.ahmed_zein.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
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
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginBody loginBody) {
        String jwt = userService.loginUser(loginBody);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        LoginResponse response = new LoginResponse();
        response.setJwt(jwt);
        return ResponseEntity.ok(response);
    }
}
