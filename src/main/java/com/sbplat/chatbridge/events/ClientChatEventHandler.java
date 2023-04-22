package com.sbplat.chatbridge.events;

import net.minecraft.client.Minecraft;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.sbplat.chatbridge.ChatBridge;
import com.sbplat.chatbridge.configuration.ChatBindEnum;

public class ClientChatEventHandler {
    @SubscribeEvent
    public void onClientChat(ClientChatEvent event) {
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
            ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().thePlayer, "/chatbridgesendmessage " + event.getMessage());
        }

        if (event.isCanceled()) {
            // Add the message to the "sent" list.
            Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
        }
    }
}
