package com.github.ahmed_zein.ecommerce_backend.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String jwt;
    private boolean success;
    private String failureReason;

}
