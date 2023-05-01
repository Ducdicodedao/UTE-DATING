package com.client.utedating.models;

public class UsersMatchedModel {
    private Boolean success;
    private String message;
    private UserMatched result;

    public UsersMatchedModel() {

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

    public UserMatched getResult() {
        return result;
    }

    public void setResult(UserMatched result) {
        this.result = result;
    }
}
