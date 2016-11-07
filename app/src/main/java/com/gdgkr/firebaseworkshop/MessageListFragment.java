package com.gdgkr.firebaseworkshop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class MessageListFragment extends Fragment {

    public static final String MESSAGES_CHILD = "messages";
    private static final String TAG = "MessageListFragment";
    private OnUserLoginListener loginListener;

    private RecyclerView messageListView;
    private Button sendButton;
    private ImageButton attachButton;
    private TextInputLayout inputTextView;

    private DatabaseReference dataReferene;
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
        return view;
    }

    private void initiateLayout() {
        messageListView.requestFocus();

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        messageListView.setLayoutManager(manager);

        dataReferene = FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD);
        msgAdapter = new MessageDataAdapter(dataReferene, FirebaseAuth.getInstance().getCurrentUser());
        msgAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                // Scroll to Bottom
                messageListView.scrollToPosition(msgAdapter.getItemCount() - 1);
            }
        });

        messageListView.setAdapter(msgAdapter);

        inputTextView.getEditText().setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER
                                && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                            handleInputText();
                            return true;
                        }
                        return false;
                    }
                });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleInputText();
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/jpg");

                // launch ImagePicker activity on the system.
                // NO error check to simplify the code as much as possible.
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent returnIntent) {

        // Handle the result form ImangePicker activtiy.

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
                            // TODO show uploading progress
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot task) {
                            final Uri url = task.getDownloadUrl();
                            sendPhoto(url);
                        }
                    });
        }
    }

    private void sendPhoto(Uri attachedImageUri) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MessageData msg = new MessageData(user, attachedImageUri);
        FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).push().setValue(msg);
    }

    private void handleInputText() {
        final String inputMsg = inputTextView.getEditText().getText().toString();
        if (!inputMsg.isEmpty()) {
            sendMessage(inputMsg);
            inputTextView.getEditText().getText().clear();
            messageListView.requestFocus();
            hideSoftKeyboard(inputTextView.getEditText());
        }
    }

    private void sendMessage(String message) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MessageData msg = new MessageData(user, message);
        Log.d(TAG, "Send Message:" + message);
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
