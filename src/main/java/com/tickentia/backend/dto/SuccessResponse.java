package com.tickentia.backend.dto;

public class SuccessResponse {
    private String message;
    private boolean status;

    public SuccessResponse(boolean status, String message) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
