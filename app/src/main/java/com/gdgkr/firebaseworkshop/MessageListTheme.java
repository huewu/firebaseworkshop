package com.gdgkr.firebaseworkshop;

import android.graphics.Color;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Remote Config Container.
 */
class MessageListTheme {

    private static final String TAG = "MessageListTheme";

    static int ColorText;
    static int ColorMessageBackground;

    static void initialize(FirebaseRemoteConfig config) {

        Log.d(TAG, "ColorText:" + config.getString("message_text_color")
                + ", ColorBackground:" + config.getString("message_background_color"));

        ColorText = Color.parseColor(config.getString("message_text_color"));
        ColorMessageBackground = Color.parseColor(config.getString("message_background_color"));

    }
}
