package com.sbplat.chatbridge.events;

import net.minecraft.client.Minecraft;

import com.sbplat.chatbridge.ChatBridge;
import com.sbplat.chatbridge.commands.CommandSendMessage;
import com.sbplat.chatbridge.configuration.ChatBindEnum;

public class ClientChatEventHandler {
    public static void onClientChat(ClientChatEvent event) {
        // Ignore commands.
        if (event.getMessage().startsWith("/")) {
            return;
        }

        ChatBindEnum chatBindOption = ChatBridge.INSTANCE.getConfig().getChatBindOption();

        if (chatBindOption == ChatBindEnum.CHATBRIDGE) {
            // Don't send the message to the server.
            event.setCanceled(true);
        }

        if (chatBindOption == ChatBindEnum.CHATBRIDGE || chatBindOption == ChatBindEnum.CHATBRIDGE_AND_SERVER) {
            // Invoke the send message command.
            CommandSendMessage.sendMessage(event.getMessage());
        }

        if (event.isCanceled()) {
            // Add the message to the "sent" list.
            Minecraft.getInstance().gui.getChat().addRecentChat(event.getMessage());
        }
    }
}
