package com.sbplat.chatbridge.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.sbplat.chatbridge.ChatBridge;
import com.sbplat.chatbridge.configuration.ChatBindEnum;
import com.sbplat.chatbridge.utils.Utils;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CommandBindChat {
    public static String getCommandName() {
        return "chatbridgebind";
    }

    public static List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("bind");
        return aliases;
    }

    public static void setChatBindOption(ChatBindEnum chatBindOption) {
        Utils.displayModChatMessage("Chat bind option set to " + chatBindOption.toString());
        ChatBridge.INSTANCE.getConfig().setChatBindOption(chatBindOption);
    }

    public static LiteralCommandNode<FabricClientCommandSource> register(
            CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(ClientCommandManager.literal(getCommandName())
                .then(ClientCommandManager.argument(
                                "bindOptionIndex",
                                IntegerArgumentType.integer(0, ChatBindEnum.values().length - 1))
                        .executes((context) -> {
                            setChatBindOption(ChatBindEnum.fromInt(
                                    IntegerArgumentType.getInteger(context, "bindOptionIndex")));
                            return 1;
                        }))
                .then(ClientCommandManager.argument("bindOptionValue", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (ChatBindEnum chatBindOption : ChatBindEnum.values()) {
                                builder.suggest(chatBindOption.toString().toLowerCase());
                            }
                            return builder.buildFuture();
                        })
                        .executes((context) -> {
                            setChatBindOption(ChatBindEnum.fromString(
                                    StringArgumentType.getString(context, "bindOptionValue")));
                            return 1;
                        })));
    }
}
