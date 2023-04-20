package com.sbplat.chatbridge.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ClientChatEvent extends Event {
    private String message;

    public ClientChatEvent(String message) {
        this.message = message;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public String getMessage() {
        return message;
    }
}
