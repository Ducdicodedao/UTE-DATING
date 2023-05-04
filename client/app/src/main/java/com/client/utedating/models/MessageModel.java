package com.client.utedating.models;

import java.util.List;

public class MessageModel {
    private Boolean success;
    private String message;
    private List<Message> result;

    public MessageModel() {

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

    public List<Message> getResult() {
        return result;
    }

    public void setResult(List<Message> result) {
        this.result = result;
    }
}
