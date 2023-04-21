package com.client.utedating.models;

public class UserModel {
    private Boolean success;
    private String message;
    private User result;

    public UserModel() {
    }

    public UserModel(Boolean success, String message, User result) {
        this.success = success;
        this.message = message;
        this.result = result;
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
}
