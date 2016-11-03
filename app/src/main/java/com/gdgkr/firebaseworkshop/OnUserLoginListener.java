package com.gdgkr.firebaseworkshop;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by chansuk on 2016. 11. 3..
 */

public interface OnUserLoginListener {

    void onLoginCompleted(FirebaseUser user);
    void onLoginFailed();
    void onLogoutCompleted(FirebaseUser user);

}
