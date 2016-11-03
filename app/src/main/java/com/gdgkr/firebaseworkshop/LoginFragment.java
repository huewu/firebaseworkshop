package com.gdgkr.firebaseworkshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginFragment";
    private OnUserLoginListener loginListener;

    private FirebaseAuth mFirebaseAuth;

    public LoginFragment() {
        // Required empty public constructor
        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnUserLoginListener) {
            loginListener = (OnUserLoginListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (loginListener != null) {
            loginListener = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButtonHandler btnHandler = new ButtonHandler();
        view.findViewById(R.id.login_google).setOnClickListener(btnHandler);
        view.findViewById(R.id.login_guest).setOnClickListener(btnHandler);
        return view;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (loginListener != null) {
            loginListener.onLoginFailed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RC_GOOGLE_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    // Google Sign In failed
                    Log.e(TAG, "Google Sign In failed.");
                    notifyLoginFailed();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void notifyLoginCompleted(final FirebaseUser user) {
        if (loginListener != null) {
            loginListener.onLoginCompleted(user);
        }
    }

    protected void notifyLoginFailed() {
        if (loginListener != null) {
            loginListener.onLoginFailed();
        }
    }

    private class ButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.login_google:
                    signInWithGoogle();
                    break;
                case R.id.login_guest:
                    signInAsGuest();
                    break;
                default:
            }
        }
    }

    private static final int RC_GOOGLE_SIGN_IN = 1013;
    private void signInWithGoogle() {
        Intent signInIntent
                = Auth.GoogleSignInApi.getSignInIntent(GoogleApiClientUtil.getInstance());
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void signInAsGuest() {
        //TODO Implement Guest SignIn
        mFirebaseAuth.signInAnonymously().addOnCompleteListener(signInCompleteListener);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(
                getActivity(), signInCompleteListener);
    }

    private final OnCompleteListener signInCompleteListener = new OnCompleteListener() {

        @Override
        public void onComplete(@NonNull Task task) {
            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful()) {
                Log.w(TAG, "signInWithCredential", task.getException());
                Toast.makeText(getActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            } else {
                notifyLoginCompleted(FirebaseAuth.getInstance().getCurrentUser());
            }
        }
    };
}
