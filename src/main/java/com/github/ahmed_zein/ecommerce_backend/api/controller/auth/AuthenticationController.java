package com.github.ahmed_zein.ecommerce_backend.api.controller.auth;

import com.github.ahmed_zein.ecommerce_backend.api.model.LoginBody;
import com.github.ahmed_zein.ecommerce_backend.api.model.LoginResponse;
import com.github.ahmed_zein.ecommerce_backend.api.model.PasswordResetBody;
import com.github.ahmed_zein.ecommerce_backend.api.model.RegistrationBody;
import com.github.ahmed_zein.ecommerce_backend.exception.EmailFailureException;
import com.github.ahmed_zein.ecommerce_backend.exception.EmailNotFoundException;
import com.github.ahmed_zein.ecommerce_backend.exception.UserAlreadyExistsException;
import com.github.ahmed_zein.ecommerce_backend.exception.UserNotVerifiedException;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginBody loginBody) {
        var response = LoginResponse.builder();
        try {
            String jwt = userService.loginUser(loginBody);
            if (jwt == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            response.jwt(jwt);
            response.success(true);
            return ResponseEntity.ok(response.build());
        } catch (UserNotVerifiedException e) {
            String failureReason = "USER_IS_UNVERIFIED";
            if (e.isNewEmailSent()) {
                failureReason += "_NEW_EMAIL_SENT";
            }
            response.failureReason(failureReason);
            response.success(false);
            return ResponseEntity.ok(response.build());
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam String token) {
        if (userService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/account/forgot-password")
    public ResponseEntity<LoginResponse> forgotPassword(@Email @RequestParam String email) {
        var response = LoginResponse.builder();
        try {
            userService.forgetPassword(email);
            response.success(true);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException e) {
            response.success(false).failureReason("EMAIL_NOT_FOUND");
            return ResponseEntity.badRequest().body(response.build());
        } catch (EmailFailureException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/account/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetBody resetBody) {
        try {
            userService.resetPassword(resetBody);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/me")
    public LocalUser ping(@AuthenticationPrincipal LocalUser user) {
        // LocalUser user = (LocalUser)
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
