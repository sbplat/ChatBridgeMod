package com.sbplat.chatbridge.configuration;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Config {

    private String configPath;

    private ChatBindEnum chatBindOption;
    private String chatMessageFormat;
    private String token;
    private String channelID;

    public Config(String configPath) {
        this.configPath = configPath;
        reload();
    }

    public void reload() {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> map = null;
        try {
            map = gson.fromJson(new FileReader(getConfigFile()), mapType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (map == null) {
            map = new HashMap<>();
        }
        chatBindOption = ChatBindEnum.fromString(map.getOrDefault("bind", "SERVER"));
        chatMessageFormat = map.getOrDefault("chatMessageFormat", "\u00A7c<\u00A73{0}\u00A7c> \u00A7f{1}");
        token = map.getOrDefault("token", "YOUR_DISCORD_BOT_TOKEN_HERE");
        channelID = map.getOrDefault("channelID", "00000000000000000000");
        save();
    }

    public ChatBindEnum getChatBindOption() {
        return chatBindOption;
    }

    public String getChatMessageFormat() {
        return chatMessageFormat;
    }

    public String getToken() {
        return token;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChatBindOption(ChatBindEnum chatBindOption) {
        this.chatBindOption = chatBindOption;
        save();
    }

    public void setChatMessageFormat(String chatMessageFormat) {
        this.chatMessageFormat = chatMessageFormat;
        save();
    }

    public void setToken(String token) {
        this.token = token;
        save();
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
        save();
    }

    public void save() {
        Map<String, String> map = new HashMap<>();
        map.put("bind", chatBindOption.toString());
        map.put("chatMessageFormat", chatMessageFormat);
        map.put("token", token);
        map.put("channelID", channelID);
        try (FileWriter writer = new FileWriter(getConfigFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getConfigFile() {
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return configFile;
    }
}
