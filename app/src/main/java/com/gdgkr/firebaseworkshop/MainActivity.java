package com.gdgkr.firebaseworkshop;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnUserLoginListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            showMessageListFragment();
        } else {
            showLoginFragment();
        }
    }

    private void showMessageListFragment() {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.activity_main, new MessageListFragment());
        tr.commit();
    }

    private void showLoginFragment() {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.activity_main, new LoginFragment());
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

        //let users be disconnected from providers
        List<String> providers = user.getProviders();
        if (providers != null) {
            for(String provider : providers) {
                //signout from providers as well.
                if (provider != null) {
                    if (provider.equals("google.com")) {
                        Auth.GoogleSignInApi.signOut(GoogleApiClientUtil.getInstance(this));
                    }
                }
                Log.d(TAG, "Provider:" + provider);
            }
        }

        FirebaseAuth.getInstance().signOut();
    }
}
