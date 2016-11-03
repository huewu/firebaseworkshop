package com.gdgkr.firebaseworkshop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private OnUserLoginListener loginListener;


    public LoginFragment() {
        // Required empty public constructor
        // Initialize FirebaseAuth
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
        // TODO implement Google sign in
        notifyLoginCompleted(null);
    }

    private void signInAsGuest() {
        // TODO implement Guest sign in
        notifyLoginCompleted(null);
    }
}
