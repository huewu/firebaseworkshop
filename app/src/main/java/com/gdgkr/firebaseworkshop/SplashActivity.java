package com.gdgkr.firebaseworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO do some initial job here.

        final FirebaseRemoteConfig config = initRemoteConfig();
        config.fetch(60)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseRemoteConfig.getInstance().activateFetched();
                    MessageListTheme.initialize(config);
                    finish();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error fetching config: " + e.getMessage());
                    MessageListTheme.initialize(config);
                    finish();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            });
    }

    private FirebaseRemoteConfig initRemoteConfig() {
        // Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        // Define default config values. Defaults are used when fetched config values are not
        // available. Eg: if an error occurred fetching values from the server.
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("message_text_color", "#FF3E3E3E");
        defaultConfigMap.put("message_background_color", "#FF202020");

        // Apply config settings and default values.
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        config.setConfigSettings(firebaseRemoteConfigSettings);
        config.setDefaults(defaultConfigMap);
        return config;
    }
}
