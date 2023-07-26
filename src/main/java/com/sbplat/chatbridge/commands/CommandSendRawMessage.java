package com.sbplat.chatbridge.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import com.sbplat.chatbridge.ChatBridge;
import com.sbplat.chatbridge.utils.Utils;

public class CommandSendRawMessage {
    public static String getCommandName() {
        return "chatbridgesendrawmessage";
    }

    public static List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("raw");
        return aliases;
    }

    public static LiteralCommandNode<FabricClientCommandSource> register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // This command is a placeholder so it can be autocompleted.
        // The actual command is handled by the ChatScreen mixin in mixin/MixinChatScreen.java.
        return dispatcher.register(ClientCommandManager.literal(getCommandName())
            .then(ClientCommandManager.argument("rawMessage", StringArgumentType.greedyString())
                .executes((context) -> {
                    Utils.displayModChatMessage("How did this get executed?");
                    return 1;
                })
            )
        );
    }
}
