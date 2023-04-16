package com.sbplat.chatbridge.server;

public class ServerMessage {
    private String author;
    private String content;

    public ServerMessage(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
