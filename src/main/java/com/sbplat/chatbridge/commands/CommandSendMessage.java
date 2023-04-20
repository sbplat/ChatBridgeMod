package com.sbplat.chatbridge.commands;

import java.io.IOException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import com.sbplat.chatbridge.ChatBridge;

public class CommandSendMessage extends CommandBase {
    @Override
    public String getCommandName() {
        return "c";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return getCommandName() + " <message>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        StringBuilder messageBuilder = new StringBuilder();
        for (String arg : args) {
            messageBuilder.append(arg).append(" ");
        }
        String message = messageBuilder.toString().trim();

        try {
            ChatBridge.INSTANCE.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
