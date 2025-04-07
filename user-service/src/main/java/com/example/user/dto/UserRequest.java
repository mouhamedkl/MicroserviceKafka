package com.example.user.dto;

public class UserRequest {
    private String username;
    private String firstname;
    private String lastname;
    private Boolean enabled;
    private String email;
    private String password;

    public String getUsername() {
        return username;
    }

    public UserRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserRequest setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserRequest setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public UserRequest setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
