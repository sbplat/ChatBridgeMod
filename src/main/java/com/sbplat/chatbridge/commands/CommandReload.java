package com.sbplat.chatbridge.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import com.sbplat.chatbridge.ChatBridge;
import com.sbplat.chatbridge.utils.Utils;

public class CommandReload extends CommandBase {
    @Override
    public String getCommandName() {
        return "chatbridgereload";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        return aliases;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if (args.length != 0) {
            throw new WrongUsageException(getCommandUsage(sender));
        }
        ChatBridge.INSTANCE.getConfig().reload();
        Utils.displayRawChatMessage("[ChatBridge] Reloaded configuration");
        try {
            ChatBridge.INSTANCE.restart();
            Utils.displayRawChatMessage("[ChatBridge] Restarted server and relay bot");
        } catch (IOException e) {
            Utils.displayRawChatMessage("[ChatBridge] Failed to restart server and relay bot");
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
