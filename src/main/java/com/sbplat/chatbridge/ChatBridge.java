package com.sbplat.chatbridge;

import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.concurrent.*;

import net.minecraft.client.Minecraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbplat.chatbridge.commands.*;
import com.sbplat.chatbridge.configuration.*;
import com.sbplat.chatbridge.events.*;
import com.sbplat.chatbridge.server.*;
import com.sbplat.chatbridge.utils.*;

public class ChatBridge implements ClientModInitializer {
	public static ChatBridge INSTANCE;

	public static Boolean available = false;

	private Config config;

	private ServerSingleClient server;
	private ServerRelayBot relayBot;

    public static final Logger LOGGER = LoggerFactory.getLogger("chatbridge");

    private void preInit() throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }));
        File configDir = new File(Minecraft.getInstance().gameDirectory, "config");
        config = new Config(Paths.get(configDir.getAbsolutePath(), "chatbridge.json").toString());
        restart();
    }

    private void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			CommandManager.registerCommands(dispatcher);
		});
    }

    private void postInit() {
    }

	@Override
	public void onInitializeClient() {
		INSTANCE = this;
		try {
			preInit();
			init();
			postInit();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	                    server.sendMessage(Minecraft.getInstance().getUser().getName());
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
