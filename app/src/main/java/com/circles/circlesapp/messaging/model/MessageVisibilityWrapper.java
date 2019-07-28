package com.circles.circlesapp.messaging.model;

public class MessageVisibilityWrapper {

    private Message message;

    public MessageVisibilityWrapper(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
