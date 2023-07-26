package com.sbplat.chatbridge.server;

import java.io.*;
import java.net.*;
import java.util.*;

import net.minecraft.client.Minecraft;

import com.sbplat.chatbridge.ChatBridge;
import com.sbplat.chatbridge.utils.Utils;

public class ServerRelayBot {
    private Process process;
    private File botFile;

    public ServerRelayBot(String relativeBotPath) throws IOException {
        try {
            botFile = copyBotResource(relativeBotPath);
        } catch (IOException e) {
            System.out.println("Error copying bot.py: " + e.getMessage());
            throw e;
        }
    }

    public void start(String ip, int port) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("py", "-3", botFile.getAbsolutePath(), ip, String.valueOf(port), ChatBridge.INSTANCE.getConfig().getToken(), ChatBridge.INSTANCE.getConfig().getChannelID());
        pb.inheritIO();
        process = pb.start();
        createExitHookThread(new Runnable() {
            @Override
            public void run() {
                ChatBridge.INSTANCE.available = false;
                Utils.displayModChatMessage("Relay bot process exited with code " + process.exitValue());
            }
        });
    }

    public void stop() {
        if (process != null) {
            process.destroy();
        }
    }

    public Boolean isRunning() {
        if (process != null) {
            return process.isAlive();
        } else {
            return false;
        }
    }

    private File copyBotResource(String relativeBotPath) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("assets/chatbridge/" + relativeBotPath);
        if (in == null) {
            throw new IOException("Resource " + relativeBotPath + " not found.");
        }
        // Create a directory called chatbridge in .minecraft.
        File botDirectory = new File(Minecraft.getInstance().gameDirectory, "chatbridge");
        if (!botDirectory.exists()) {
            botDirectory.mkdir();
        }
        // Delete the bot.py file if it already exists.
        File botFile = new File(botDirectory, "bot.py");
        if (botFile.exists()) {
            botFile.delete();
        }
        // Copy the bot.py file from the jar over.
        botFile.createNewFile();
        FileOutputStream out = new FileOutputStream(botFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        out.close();
        return botFile;
    }

    private void createExitHookThread(final Runnable callback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    System.out.println("Error waiting for bot process: " + e.getMessage());
                }
                System.out.println("Bot process exited with code " + process.exitValue());
                callback.run();
            }
        });
        thread.start();
    }
}
