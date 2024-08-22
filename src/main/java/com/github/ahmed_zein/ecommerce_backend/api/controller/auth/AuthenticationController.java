package com.github.ahmed_zein.ecommerce_backend.api.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.ahmed_zein.ecommerce_backend.api.model.LoginBody;
import com.github.ahmed_zein.ecommerce_backend.api.model.LoginResponse;
import com.github.ahmed_zein.ecommerce_backend.api.model.RegistrationBody;
import com.github.ahmed_zein.ecommerce_backend.exception.EmailFailureException;
import com.github.ahmed_zein.ecommerce_backend.exception.UserAlreadyExists;
import com.github.ahmed_zein.ecommerce_backend.exception.UserNotVerifiedException;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginBody loginBody) {
        LoginResponse response = new LoginResponse();
        try {
            String jwt = userService.loginUser(loginBody);
            if (jwt == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        } catch (UserNotVerifiedException e) {
            String failureReason = "USER_IS_UNVERIFIED";
            if (e.isNewEmailSent()) {
                failureReason += "_NEW_EMAIL_SENT";
            }
            response.setFailureReason(failureReason);
            response.setSuccess(false);
            return ResponseEntity.ok(response);
        } catch (EmailFailureException e) {
            System.err.println("email exception");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam String token) {
        if (userService.verifyEmail(token)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/ping")
    public LocalUser ping(@AuthenticationPrincipal LocalUser user) {
        // LocalUser user = (LocalUser)
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
