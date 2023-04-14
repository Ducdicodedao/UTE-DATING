package com.client.utedating.models;

public class Message {
    private String conversationId;
    private String message;
    private String receiverId;

    public Message(String conversationId, String message, String receiverId) {
        this.conversationId = conversationId;
        this.message = message;
        this.receiverId = receiverId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
