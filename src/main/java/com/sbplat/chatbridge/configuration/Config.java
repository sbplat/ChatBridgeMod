package com.sbplat.chatbridge.configuration;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    private String configPath;
    private Configuration config;

    private ChatBindEnum chatBindOption;
    private String chatMessageFormat;
    private String token;
    private String channelID;

    public Config(String configPath) {
        this.configPath = configPath;
        reload();
    }

    public void reload() {
        config = new Configuration(getConfigFile());
        config.load();
        String chatBindEnumString = config.getString("bind", "Chat", "SERVER", "Chat bind option (SERVER, CHATBRIDGE, CHATBRIDGE_AND_SERVER)");
        chatBindOption = ChatBindEnum.fromString(chatBindEnumString);
        chatMessageFormat = config.getString("chatMessageFormat", "Chat", "\u00A7c<\u00A73{0}\u00A7c> \u00A7f{1}", "Chat message format");
        token = config.getString("token", "Discord", "YOUR_DISCORD_BOT_TOKEN_HERE", "Discord bot token");
        channelID = config.getString("channelID", "Discord", "00000000000000000000", "Discord relay channel ID");
        config.save();
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
        config.load();
        setConfig("bind", "Chat", "SERVER", chatBindOption.toString());
        setConfig("chatMessageFormat", "Chat", "\u00A7c<\u00A73{0}\u00A7c> \u00A7f{1}", chatMessageFormat);
        setConfig("token", "Discord", "YOUR_DISCORD_BOT_TOKEN_HERE", token);
        setConfig("channelID", "Discord", "00000000000000000000", channelID);
        config.save();
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

    private void setConfig(String name, String category, String defaultValue, String value) {
        config.get(category, name, defaultValue).set(value);
    }
}
