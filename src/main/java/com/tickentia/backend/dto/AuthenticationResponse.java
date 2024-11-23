package com.tickentia.backend.dto;

public class AuthenticationResponse {
    private String token;
    private String name;
    private String role;
    private long userId;

    public AuthenticationResponse(String token, String name, String role, long userId) {
        this.token = token;
        this.name = name;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

