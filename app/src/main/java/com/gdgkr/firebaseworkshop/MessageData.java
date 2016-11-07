package com.gdgkr.firebaseworkshop;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

class MessageData {

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

    MessageData(FirebaseUser user, String text) {
        this(user);
        this.text = text;
    }

    MessageData(FirebaseUser user, Uri attachPhotoUri) {
        this(user);

        if (attachPhotoUri != null) {
            this.photoUrl = attachPhotoUri.toString();
        }
    }

// Empty constructor is required for Firebase RealtimeDatabase Data Sanpshot
    private MessageData() {}

    private MessageData (FirebaseUser user) {
        this.uuid = user.getUid();
        this.name = user.getDisplayName();

        if (user.getPhotoUrl() != null) {
            this.profileUrl= user.getPhotoUrl().toString();
        }
    }

    MessageType getMessageType() {
        return messageType;
    }

    void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    String getPhotoUrl() {
        return photoUrl;
    }

    public String getText() {
        return text;
    }

    String getProfileUrl() {
        return profileUrl;
    }

    String getName() {
        return name;
    }

    String getUuid() {
        return uuid;
    }

    enum MessageType {

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
        }
    }
}
