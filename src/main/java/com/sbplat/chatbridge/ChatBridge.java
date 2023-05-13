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

    private Config config;

    private ServerSingleClient server;
    private Process relayBotProcess;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }));

        config = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());

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

        startRelayBot("localhost", server.getPort());

        server.listen();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CommandBindChat());
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
        server.sendMessage(message);
        Utils.displayChatMessage(new ServerMessage("You", message));
    }

    private void startRelayBot(String ip, int port) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("assets/chatbridge/bot.py");
        // Create a directory called chatbridge in .minecraft.
        File relayBotDirectory = new File(Minecraft.getMinecraft().mcDataDir, "chatbridge");
        if (!relayBotDirectory.exists()) {
            relayBotDirectory.mkdir();
        }
        // Delete the bot.py file if it already exists.
        File relayBotFile = new File(relayBotDirectory, "bot.py");
        if (relayBotFile.exists()) {
            relayBotFile.delete();
        }
        // Copy the bot.py file from the jar over.
        try {
            relayBotFile.createNewFile();
            FileOutputStream out = new FileOutputStream(relayBotFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProcessBuilder pb = new ProcessBuilder("py", "-3", relayBotFile.getAbsolutePath(), ip, String.valueOf(port), config.getToken(), config.getChannelID());
        pb.inheritIO();

        try {
            relayBotProcess = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutdown() {
        if (server != null) {
            try {
                server.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (relayBotProcess != null) {
            relayBotProcess.destroy();
        }
    }
}
