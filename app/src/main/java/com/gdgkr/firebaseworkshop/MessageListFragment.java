package com.gdgkr.firebaseworkshop;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageListFragment extends Fragment {

    private static final String TAG = "MessageListFragment";
    public static final String MESSAGES_CHILD = "messages";

    private OnUserLoginListener loginListener;

    private RecyclerView messageListView;
    private Button sendButton;
    private TextInputLayout inputTextView;

    private MessageDataAdapter msgAdapter;

    public MessageListFragment() {
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_msg_list, container, false);

        messageListView = (RecyclerView) view.findViewById(R.id.frag_msg_list);
        sendButton = (Button) view.findViewById(R.id.frag_msg_list_send);
        inputTextView = (TextInputLayout) view.findViewById(R.id.frag_input_layout);

        initiateLayout();
        initiateRealtimeDatabase();
        return view;
    }

    private void initiateRealtimeDatabase() {
    }

    private void initiateLayout() {
        messageListView.requestFocus();

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        messageListView.setLayoutManager(manager);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD);

        msgAdapter = new MessageDataAdapter(ref, FirebaseAuth.getInstance().getCurrentUser());
        msgAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                messageListView.scrollToPosition(msgAdapter.getItemCount() - 1);
            }
        });

        messageListView.setAdapter(msgAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String inputMsg = inputTextView.getEditText().getText().toString();
                if (inputMsg != null && !inputMsg.isEmpty()) {
                    sendMessage(inputMsg);
                    inputTextView.getEditText().getText().clear();
                    messageListView.requestFocus();
                    hideSoftKeyboard(inputTextView.getEditText());
                }
            }
        });
    }

    private void sendMessage(String message) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MessageData msg
                = new MessageData(user.getDisplayName(), message, user.getPhotoUrl().toString());
        FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).push().setValue(msg);

        Log.d(TAG, "Send Message:" + message);
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager
                = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                //TODO implement sign out feature here
                loginListener.onLogoutCompleted(FirebaseAuth.getInstance().getCurrentUser());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
