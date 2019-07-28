package com.circles.circlesapp.messaging.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageDetailsResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Message> messagesList;

    @SerializedName("errors")
    private MessageDetailsErrors errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Message> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    public MessageDetailsErrors getErrors() {
        return errors;
    }

    public void setErrors(MessageDetailsErrors errors) {
        this.errors = errors;
    }
}
