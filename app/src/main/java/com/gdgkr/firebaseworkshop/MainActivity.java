package com.gdgkr.firebaseworkshop;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements OnUserLoginListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.add(R.id.activity_main, new LoginFragment());
        tr.commit();
    }

    @Override
    public void onLoginCompleted(FirebaseUser user) {
        showMessageListFragment();
    }

    @Override
    public void onLoginFailed() {
        Toast.makeText(this, "Fail to login", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLogoutCompleted(FirebaseUser user) {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.activity_main, new LoginFragment());
        tr.commit();

        //TODO disconnect from providers
        //user.getProviders()
    }

    private void showMessageListFragment() {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.activity_main, new MessageListFragment());
        tr.commit();
    }

}
