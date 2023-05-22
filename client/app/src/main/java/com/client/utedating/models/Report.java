package com.client.utedating.models;

public class Report {
    String sender;
    String receiver;
    Boolean isApproved;
    String title;

    public Report(String sender, String receiver, Boolean isApproved, String title) {
        this.sender = sender;
        this.receiver = receiver;
        this.isApproved = isApproved;
        this.title = title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
