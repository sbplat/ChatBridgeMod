package com.sbplat.chatbridge.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CommandManager {
    public static final List<Class> commandClasses = Arrays.asList(
        CommandBindChat.class,
        CommandOnline.class,
        CommandReload.class,
        CommandSendMessage.class,
        CommandSendRawMessage.class
    );

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        for (Class clazz : commandClasses) {
            try {
                Method register = clazz.getMethod("register", CommandDispatcher.class);
                Object commandNode = register.invoke(null, dispatcher);
                for (String alias : (List<String>) clazz.getMethod("getCommandAliases").invoke(null)) {
                    dispatcher.register(ClientCommandManager.literal(alias).redirect((LiteralCommandNode<FabricClientCommandSource>) commandNode));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
