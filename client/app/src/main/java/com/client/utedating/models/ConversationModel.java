package com.client.utedating.models;

import java.util.List;

public class ConversationModel {
    private Boolean success;
    private String message;
    private List<Conversation> result;

    public ConversationModel() {
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

    public List<Conversation> getResult() {
        return result;
    }

    public void setResult(List<Conversation> result) {
        this.result = result;
    }
}
