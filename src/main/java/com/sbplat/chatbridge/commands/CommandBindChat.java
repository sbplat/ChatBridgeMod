package com.sbplat.chatbridge.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import com.sbplat.chatbridge.ChatBridge;
import com.sbplat.chatbridge.configuration.ChatBindEnum;
import com.sbplat.chatbridge.utils.Utils;

public class CommandBindChat extends CommandBase {
    @Override
    public String getCommandName() {
        return "chatbridgebind";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("bind");
        return aliases;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <SERVER|CHATBRIDGE|CHATBRIDGE_AND_SERVER>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if (args.length != 1) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        ChatBindEnum chatBindOption;
        try {
            chatBindOption = ChatBindEnum.fromInt(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            chatBindOption = ChatBindEnum.fromString(args[0]);
        }

        Utils.displayModChatMessage("Chat bind option set to " + chatBindOption.toString());
        ChatBridge.INSTANCE.getConfig().setChatBindOption(chatBindOption);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
