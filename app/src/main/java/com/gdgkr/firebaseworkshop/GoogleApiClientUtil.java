package com.gdgkr.firebaseworkshop;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by chansuk on 2016. 11. 3..
 */

public class GoogleApiClientUtil {

    private static GoogleApiClient googleApiClient;

    /*
    public static void initialize(
            FragmentActivity activity, GoogleApiClient.OnConnectionFailedListener listener) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    */

    public static void initialize(GoogleApiClient client) {
        googleApiClient = client;
    }

    public static GoogleApiClient getInstance() {

        if (googleApiClient == null)
            throw new IllegalStateException("initialize MUST be invoked before accessing this");

        return googleApiClient;
    }
}
