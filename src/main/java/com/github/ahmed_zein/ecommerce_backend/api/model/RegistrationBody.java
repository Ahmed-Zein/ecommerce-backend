package com.github.ahmed_zein.ecommerce_backend.api.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationBody {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 255, message = "should be between 3 and 255")
    private String userName;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 255, message = "should be between 3 and 255")
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 255, message = "should be between 3 and 255")
    private String lastName;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", message = "Minimum six characters, at least one letter and one number")
    @Size(min = 6, max = 32, message = "should be between 6 and 32")
    private String password;

}
