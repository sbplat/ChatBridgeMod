package com.sbplat.chatbridge.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import com.sbplat.chatbridge.ChatBridge;

public class CommandSendMessage {
    public static String getCommandName() {
        return "chatbridgesendmessage";
    }

    public static List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("send");
        return aliases;
    }

    public static void sendMessage(String message) {
        try {
            ChatBridge.INSTANCE.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LiteralCommandNode<FabricClientCommandSource> register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(ClientCommandManager.literal(getCommandName())
            .then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
                .executes((context) -> {
                    sendMessage(StringArgumentType.getString(context, "message"));
                    return 1;
                })
            )
        );
    }
}
