package com.gdgkr.firebaseworkshop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.add(R.id.activity_main, new LoginFragment());
            tr.commit();
        }


        initLibs();
    }

    private void initLibs() {
        GoogleApiClientUtil.initialize(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                // To handle the error which cannot be recovered automatically.
            }
        });
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
        List<String> providers = user.getProviders();
        for(String provider : providers) {
            //signout from providers as well.
            if (provider != null) {
                if (provider.equals("google.com")) {
                    Auth.GoogleSignInApi.signOut(GoogleApiClientUtil.getInstance());
                }
            }
            Log.d(TAG, "Provider:" + provider);
        }

        FirebaseAuth.getInstance().signOut();
    }

    private void showMessageListFragment() {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.activity_main, new MessageListFragment());
        tr.commit();
    }

}
