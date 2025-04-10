package com.karhacter.movies_webapp.payloads;

public class LoginResponse {

    private String email;
    private String password;

    // Constructors, Getters, and Setters
    public LoginResponse() {
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

}
