package com.client.utedating.models;

public class SignUpModel {
    private Boolean success;
    private String message;
    private User result;
    private String jwtToken;
    public SignUpModel() {
    }

    public SignUpModel(Boolean success, String message, User result, String jwtToken ) {
        this.success = success;
        this.message = message;
        this.result = result;
        this.jwtToken = jwtToken;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getResult() {
        return result;
    }

    public void setResult(User result) {
        this.result = result;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
