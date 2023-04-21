package com.sbplat.chatbridge.utils;

import java.text.MessageFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.sbplat.chatbridge.ChatBridge;
import com.sbplat.chatbridge.server.ServerMessage;

public class Utils {
    public static void displayRawChatMessage(String message) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            player.addChatMessage(new ChatComponentText(message));
        }
    }

    public static void displayChatMessage(ServerMessage message) {
        // displayRawChatMessage(EnumChatFormatting.RED + "<" + EnumChatFormatting.DARK_AQUA + message.getAuthor() + EnumChatFormatting.RED + "> " + EnumChatFormatting.WHITE + message.getContent());
        displayRawChatMessage(MessageFormat.format(ChatBridge.INSTANCE.getConfig().getChatMessageFormat(), message.getAuthor(), message.getContent()));
    }
}
