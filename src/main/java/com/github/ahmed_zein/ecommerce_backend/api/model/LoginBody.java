package com.github.ahmed_zein.ecommerce_backend.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginBody {

    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
