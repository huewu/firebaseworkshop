package com.gdgkr.firebaseworkshop;

import com.google.firebase.auth.FirebaseUser;

/*
    Interface to propagate user login events between Fragments & Activities.
 */
public interface OnUserLoginListener {

    void onLoginCompleted(FirebaseUser user);
    void onLoginFailed();
    void onLogoutCompleted(FirebaseUser user);

}
