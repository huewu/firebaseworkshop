package com.gdgkr.firebaseworkshop;

import java.util.Random;

public class MessageData {

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
    }

    public String name;
    public String text;
    public String photoUrl;

    public MessageType messageType;

    private MessageData() {
        this.messageType = MessageType.MESSAGE_FROM_OTHERS;
    };

    public MessageData (String name, String text, String photoUrl) {
        this.name = name;
        this.text = text;
        this.photoUrl = photoUrl;

    }
}
