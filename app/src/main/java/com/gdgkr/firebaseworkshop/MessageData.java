package com.gdgkr.firebaseworkshop;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public class MessageData {

    public enum MessageType {

        MESSAGE_FROM_ME, MESSAGE_FROM_OTHERS, PHOTO_FROM_ME, PHOTO_FROM_OTHERS;

        public static MessageType valueOf(int type) {

            if (type == MESSAGE_FROM_ME.ordinal()) {
                return MESSAGE_FROM_ME;
            } else if (type == MESSAGE_FROM_OTHERS.ordinal()) {
                return MESSAGE_FROM_OTHERS;
            } else if (type == PHOTO_FROM_ME.ordinal()) {
                return PHOTO_FROM_ME;
            } else {
                return PHOTO_FROM_OTHERS;
            }
        };
    }

    // unique user id
    private String uuid;

    // display username
    private String name;

    // user porfile image url (can be null)
    private String profileUrl;

    // text message
    private String text;

    // attached image
    private String photoUrl;

    private MessageType messageType;

    public MessageData (FirebaseUser user, String text) {
        this(user);
        this.text = text;
    }

    public MessageData (FirebaseUser user, Uri attachPhotoUri) {
        this(user);

        if (attachPhotoUri != null) {
            this.photoUrl = attachPhotoUri.toString();
        }
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getText() {
        return text;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    private MessageData() {};
    private MessageData (FirebaseUser user) {
        this.uuid = user.getUid();
        this.name = user.getDisplayName();

        if (user.getPhotoUrl() != null) {
            this.profileUrl= user.getPhotoUrl().toString();
        }
    }
}
