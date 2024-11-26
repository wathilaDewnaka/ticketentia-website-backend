package com.tickentia.backend.dto;

public class UpdatePassword extends DeleteAccount {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
