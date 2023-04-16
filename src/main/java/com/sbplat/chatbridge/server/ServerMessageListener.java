package com.sbplat.chatbridge.server;

import com.sbplat.chatbridge.server.ServerMessage;

public interface ServerMessageListener {
    public void onMessageReceived(ServerMessage message);
}
