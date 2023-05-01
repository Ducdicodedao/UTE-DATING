package com.client.utedating.models;

import java.util.List;

public class UsersLikedModel {
    private Boolean success;
    private String message;
    private UserLiked result;

    public UsersLikedModel() {

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

    public UserLiked getResult() {
        return result;
    }

    public void setResult(UserLiked result) {
        this.result = result;
    }
}
