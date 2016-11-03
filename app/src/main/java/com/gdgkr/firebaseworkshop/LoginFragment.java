package com.gdgkr.firebaseworkshop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
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

    private void signInWithGoogle() {
        //TODO
    }

    private void signInAsGuest() {
        //TODO
    }

}
