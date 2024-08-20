package com.github.ahmed_zein.ecommerce_backend.api.model;

import jakarta.validation.constraints.*;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegistrationBody{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
