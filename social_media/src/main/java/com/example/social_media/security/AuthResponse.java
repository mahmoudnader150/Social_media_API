package com.example.social_media.security;

public class AuthResponse {
    private String token;
    private String email;
    private String userName;

    public AuthResponse(String token, String email, String userName) {
        this.token = token;
        this.email = email;
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
} 