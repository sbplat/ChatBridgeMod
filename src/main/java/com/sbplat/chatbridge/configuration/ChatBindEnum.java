package com.sbplat.chatbridge.configuration;

public enum ChatBindEnum {
    SERVER,
    CHATBRIDGE,
    CHATBRIDGE_AND_SERVER;

    public static ChatBindEnum fromString(String string) {
        try {
            return ChatBindEnum.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ChatBindEnum.SERVER;
        }
    }

    public static ChatBindEnum fromInt(int i) {
        switch (i) {
            case 0:
                return ChatBindEnum.SERVER;
            case 1:
                return ChatBindEnum.CHATBRIDGE;
            case 2:
                return ChatBindEnum.CHATBRIDGE_AND_SERVER;
            default:
                return ChatBindEnum.SERVER;
        }
    }
}
