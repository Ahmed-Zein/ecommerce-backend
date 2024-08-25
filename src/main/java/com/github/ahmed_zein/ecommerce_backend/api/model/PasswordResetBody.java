package com.github.ahmed_zein.ecommerce_backend.api.model;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class PasswordResetBody {
    @NotBlank
    @NotNull
    private String token;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", message = "Minimum six characters, at least one letter and one number")
    @Size(min = 6, max = 32, message = "should be between 6 and 32")
    private String password;

}
