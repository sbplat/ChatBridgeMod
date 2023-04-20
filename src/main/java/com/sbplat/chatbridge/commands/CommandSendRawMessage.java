package com.sbplat.chatbridge.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import net.minecraftforge.client.ClientCommandHandler;

import com.sbplat.chatbridge.ChatBridge;

public class CommandSendRawMessage extends CommandBase {
    @Override
    public String getCommandName() {
        return "raw";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <raw message>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        String message = String.join(" ", args);
        if (message.isEmpty()) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        // First, try invoking the message on the client side.
        if (ClientCommandHandler.instance.executeCommand(sender, message) == 0) {
            // The message was not handled by the client, so let vanilla handle it.
            Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
