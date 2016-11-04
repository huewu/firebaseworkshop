package com.gdgkr.firebaseworkshop;

import java.util.Random;

public class MessageData {

    private String name;
    private String text;
    private String photoUrl;
    private MessageType messageType;

    public MessageData (String name, String text, String photoUrl) {
        this.name = name;
        this.text = text;
        this.photoUrl = photoUrl;

        if (new Random().nextBoolean()) {
            this.messageType = MessageType.MESSAGE_FROM_OTHERS;
        } else {
            this.messageType = MessageType.MESSAGE_FROM_ME;
        }
    }

    private MessageData(){}

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public enum MessageType {

        MESSAGE_FROM_ME,
        MESSAGE_FROM_OTHERS;

        public static MessageType valueOf(int type) {

            if (type == MESSAGE_FROM_ME.ordinal()) {
                return MessageType.MESSAGE_FROM_ME;
            } else {
                return MessageType.MESSAGE_FROM_OTHERS;
            }
        };
    };
}
