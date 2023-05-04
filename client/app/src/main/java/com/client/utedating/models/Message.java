package com.client.utedating.models;

import java.time.LocalDateTime;
import java.util.Date;

public class Message {
    private String _id;
    private String receiver;
    private String content;
    private Date sentAt;

    public Message(){

    }
    public Message(String _id, String receiver, String content, Date sentAt) {
        this._id = _id;
        this.receiver = receiver;
        this.content = content;
        this.sentAt = sentAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }
}
