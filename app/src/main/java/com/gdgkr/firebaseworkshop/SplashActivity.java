package com.gdgkr.firebaseworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO do some initial job here.

        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }.start();
    }
}
