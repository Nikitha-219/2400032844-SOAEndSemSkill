package com.example.jwtvalidation.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenRequest {

    @NotBlank(message = "Token must not be empty")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
