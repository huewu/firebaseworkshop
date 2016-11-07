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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private OnUserLoginListener loginListener;

    public LoginFragment() {
        // Required empty public constructor
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
        // Create signInIntent and start SignIn activity using GoogleAPI (from play services)
        Intent signInIntent
                = Auth.GoogleSignInApi.getSignInIntent(GoogleApiClientUtil.getInstance(getActivity()));
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Handle signin activity result from GoogleAPI
        switch (requestCode) {
            case RC_GOOGLE_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess() && result.getSignInAccount() != null) {
                    // Google Sign In was successful, authenticate with Firebase
                    final GoogleSignInAccount account = result.getSignInAccount();
                    final AuthCredential credential
                            = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(
                            getActivity(), signInCompleteListener);
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

    private void signInAsGuest() {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(signInCompleteListener);
    }

    private final OnCompleteListener<AuthResult> signInCompleteListener
            = new OnCompleteListener<AuthResult>() {

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
