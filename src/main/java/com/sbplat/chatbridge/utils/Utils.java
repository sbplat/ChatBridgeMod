package com.sbplat.chatbridge.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;

import com.sbplat.chatbridge.server.ServerMessage;

public class Utils {
    public static void displayRawChatMessage(String message) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            player.addChatMessage(new ChatComponentText(message));
        }
    }

    public static void displayChatMessage(ServerMessage message) {
        displayRawChatMessage(String.format("<%s> %s", message.getAuthor(), message.getContent()));
    }
}
