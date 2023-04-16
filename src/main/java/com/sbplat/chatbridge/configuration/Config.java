package com.sbplat.chatbridge.configuration;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    private Configuration config;
    private String token;
    private String channelID;

    public Config(File configFile) {
        config = new Configuration(configFile);
        config.load();
        token = config.getString("token", "Discord", "YOUR_DISCORD_BOT_TOKEN_HERE", "Discord bot token");
        channelID = config.getString("channelID", "Discord", "00000000000000000000", "Discord relay channel ID");
        config.save();
    }

    public String getToken() {
        return token;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public void save() {
        config.load();
        setConfig("token", "Discord", "YOUR_DISCORD_BOT_TOKEN_HERE", token);
        setConfig("channelID", "Discord", "00000000000000000000", channelID);
        config.save();
    }

    private void setConfig(String name, String category, String defaultValue, String value) {
        config.get(category, name, defaultValue).set(value);
    }
}
