package com.sbplat.chatbridge.events;

public class ClientChatEvent extends Event {
    private String message;

    public ClientChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String newMessage) {
        message = newMessage;
    }
}
