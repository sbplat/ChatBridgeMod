package com.sbplat.chatbridge.configuration;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    private Configuration config;

    private ChatBindEnum chatBindOption;
    private String token;
    private String channelID;

    public Config(File configFile) {
        config = new Configuration(configFile);
        config.load();
        String chatBindEnumString = config.getString("bind", "Chat", "SERVER", "Chat bind option (SERVER, CHATBRIDGE, CHATBRIDGE_AND_SERVER)");
        chatBindOption = ChatBindEnum.fromString(chatBindEnumString);
        token = config.getString("token", "Discord", "YOUR_DISCORD_BOT_TOKEN_HERE", "Discord bot token");
        channelID = config.getString("channelID", "Discord", "00000000000000000000", "Discord relay channel ID");
        config.save();
    }

    public ChatBindEnum getChatBindOption() {
        return chatBindOption;
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
        setConfig("token", "Discord", "YOUR_DISCORD_BOT_TOKEN_HERE", token);
        setConfig("channelID", "Discord", "00000000000000000000", channelID);
        config.save();
    }

    private void setConfig(String name, String category, String defaultValue, String value) {
        config.get(category, name, defaultValue).set(value);
    }
}
