package com.sbplat.chatbridge;

import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.concurrent.*;

import net.minecraft.client.Minecraft;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import com.sbplat.chatbridge.commands.*;
import com.sbplat.chatbridge.configuration.*;
import com.sbplat.chatbridge.events.*;
import com.sbplat.chatbridge.server.*;
import com.sbplat.chatbridge.utils.*;

@Mod(modid = ChatBridge.MOD_ID, name = ChatBridge.NAME, version = ChatBridge.VERSION)
public class ChatBridge {
    public static final String MOD_ID = "chatbridge";
    public static final String NAME = "ChatBridge";
    public static final String VERSION = "0.0.1";

    @Mod.Instance(ChatBridge.MOD_ID)
    public static ChatBridge INSTANCE;

    public static Boolean available = false;

    private Config config;

    private ServerSingleClient server;
    private ServerRelayBot relayBot;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }));
        config = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
        restart();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CommandBindChat());
        ClientCommandHandler.instance.registerCommand(new CommandOnline());
        ClientCommandHandler.instance.registerCommand(new CommandReload());
        ClientCommandHandler.instance.registerCommand(new CommandSendMessage());
        ClientCommandHandler.instance.registerCommand(new CommandSendRawMessage());

        MinecraftForge.EVENT_BUS.register(new ClientChatEventHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    public Config getConfig() {
        return config;
    }

    public void sendMessage(String message) throws IOException {
        if (!available) {
            Utils.displayModChatMessage("ChatBridge is not available. Check the logs for more information.");
            return;
        }
        server.sendMessage(message);
        Utils.displayChatMessage(new ServerMessage("You", message));
    }

    public void restart() throws IOException {
        shutdown();
        server = new ServerSingleClient(0, new ServerMessageListener() {
            @Override
            public void onMessageReceived(ServerMessage message) {
                if (message.getContent().equals("/online")) {
                    try {
                        server.sendMessage(Minecraft.getMinecraft().getSession().getUsername());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                Utils.displayChatMessage(message);
            }
        });
        relayBot = new ServerRelayBot("bot.py");
        relayBot.start("localhost", server.getPort());
        server.listen();
        available = true;
    }

    private void shutdown() {
        available = false;
        if (server != null) {
            try {
                server.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (relayBot != null) {
            relayBot.stop();
        }
    }
}
