package com.gdgkr.firebaseworkshop;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/*
    Utility Class to keep the googleApiClient instance and reuse it from anywhere.
 */
class GoogleApiClientUtil {

    private static GoogleApiClient googleApiClient;

    static private void initialize(
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

    static GoogleApiClient getInstance(FragmentActivity activity) {

        if (googleApiClient == null) {
            // nothing to do w/ OnConnectionFailedListener yet :p
            initialize(activity, null);
            throw new IllegalStateException("initialize MUST be invoked before accessing this");
        }

        return googleApiClient;
    }
}
