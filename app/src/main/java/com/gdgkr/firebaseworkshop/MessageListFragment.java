package com.gdgkr.firebaseworkshop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class MessageListFragment extends Fragment {

    private static final String TAG = "MessageListFragment";
    public static final String MESSAGES_CHILD = "messages";

    private OnUserLoginListener loginListener;

    private RecyclerView messageListView;
    private Button sendButton;
    private ImageButton attachButton;
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
        attachButton = (ImageButton) view.findViewById(R.id.frag_msg_list_attach);
        inputTextView = (TextInputLayout) view.findViewById(R.id.frag_input_layout);

        initiateLayout();
        initiateRealtimeDatabase();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent returnIntent) {
        // If the selection didn't work
        if (resultCode != RESULT_OK) {
            // Exit without doing anything else
            return;
        } else {
            // Get the file's content URI from the incoming Intent
            Uri returnUri = returnIntent.getData();
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final String fileName = returnUri.getLastPathSegment() + userId;
            FirebaseStorage.getInstance().getReference().child("images").
                    child(userId).child(fileName).putFile(returnUri)
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // show uploading progress
                        }
                    })
                    .addOnCompleteListener(
                    new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    final Uri url = task.getResult().getDownloadUrl();
                    sendMessage(url);
                }
            });
        }
    }

    private void initiateRealtimeDatabase() {

        FirebaseAnalytics.getInstance(getContext()).logEvent(
                FirebaseAnalytics.Event.VIEW_ITEM, null);
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
                if (inputTextView.getEditText().getText().length() > 0) {
                    final String inputMsg = inputTextView.getEditText().getText().toString();
                    if (!inputMsg.isEmpty()) {
                        sendMessage(inputMsg);
                        inputTextView.getEditText().getText().clear();
                        messageListView.requestFocus();
                        hideSoftKeyboard(inputTextView.getEditText());
                    }
                }
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/jpg");

                startActivityForResult(intent, 0);
            }
        });
    }

    private void sendMessage(String message) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MessageData msg = new MessageData(user, message);
        Log.d(TAG, "Send Message:" + message);
        FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).push().setValue(msg);
    }

    private void sendMessage(Uri attachedImageUri) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MessageData msg = new MessageData(user, attachedImageUri);
        FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).push().setValue(msg);
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
