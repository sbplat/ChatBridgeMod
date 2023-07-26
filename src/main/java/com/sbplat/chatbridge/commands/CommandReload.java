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
import com.sbplat.chatbridge.utils.Utils;

public class CommandReload {
    public static String getCommandName() {
        return "chatbridgereload";
    }

    public static List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        return aliases;
    }

    public static void reloadAll() {
        ChatBridge.INSTANCE.getConfig().reload();
        Utils.displayModChatMessage("Reloaded configuration");
        try {
            ChatBridge.INSTANCE.restart();
            Utils.displayModChatMessage("Restarted server and relay bot");
        } catch (IOException e) {
            Utils.displayModChatMessage("Failed to restart server and relay bot");
        }
    }

    public static LiteralCommandNode<FabricClientCommandSource> register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(ClientCommandManager.literal(getCommandName())
            .executes((context) -> {
                reloadAll();
                return 1;
            })
        );
    }
}
