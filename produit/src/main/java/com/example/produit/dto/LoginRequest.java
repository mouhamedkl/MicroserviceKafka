package com.example.produit.dto;


public class LoginRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}
