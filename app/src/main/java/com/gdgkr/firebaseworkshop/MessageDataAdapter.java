package com.gdgkr.firebaseworkshop;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class MessageDataAdapter extends FirebaseRecyclerAdapter<MessageData,
        MessageDataAdapter.MessageViewHolder> {

    private FirebaseUser user;


    public MessageDataAdapter(DatabaseReference ref, FirebaseUser user) {
        super(MessageData.class, 0, MessageViewHolder.class, ref);
        this.user = user;
    }

    /*
     MessageViewHolder
     */
    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        final Context ViewContext;
        final TextView TextView;
        final ImageView ProfileImageView;
        final ImageView PhotoImageView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            TextView = (TextView) itemView.findViewById(R.id.message_item_text);
            ProfileImageView = (ImageView) itemView.findViewById(R.id.message_item_user_photo);
            PhotoImageView = (ImageView) itemView.findViewById(R.id.message_item_photo);
            ViewContext = itemView.getContext();

            if (TextView != null) {
                TextView.setTextColor(MessageListTheme.ColorText);
                TextView.setBackgroundColor(MessageListTheme.ColorMessageBackground);
            }

            if (PhotoImageView != null) {
                PhotoImageView.setBackgroundColor(
                        MessageListTheme.ColorMessageBackground);
            }
        }
    }


    protected MessageData parseSnapshot(DataSnapshot snapshot) {
        MessageData msg = snapshot.getValue(MessageData.class);

        if (msg != null
                && msg.getUuid() != null && msg.getUuid().equals(user.getUid())) {
            if (msg.getPhotoUrl() != null) {
                msg.setMessageType(MessageData.MessageType.PHOTO_FROM_ME);
            } else {
                msg.setMessageType(MessageData.MessageType.MESSAGE_FROM_ME);
            }

        } else {
            if (msg.getPhotoUrl() != null) {
                msg.setMessageType(MessageData.MessageType.PHOTO_FROM_OTHERS);
            } else {
                msg.setMessageType(MessageData.MessageType.MESSAGE_FROM_OTHERS);
            }
        }

        return msg;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MessageData.MessageType msgType = MessageData.MessageType.valueOf(viewType);
        final int layout;

        switch (msgType) {
            case MESSAGE_FROM_ME:
                layout = R.layout.message_item_mine;
                break;
            case MESSAGE_FROM_OTHERS:
                layout = R.layout.message_item_others;
                break;
            case PHOTO_FROM_ME:
                layout = R.layout.photo_item_mine;
                break;
            case PHOTO_FROM_OTHERS:
                layout = R.layout.photo_item_others;
                break;
            default:
                layout = R.layout.message_item_mine;
                break;
        }

        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getMessageType().ordinal();
    }

    @Override
    protected void populateViewHolder(MessageViewHolder holder, MessageData msg, int position) {
        if (msg.getProfileUrl() == null) {
            holder.ProfileImageView.setImageDrawable(ContextCompat.getDrawable(holder.ViewContext,
                    R.drawable.firebase_logo));
        } else {
            Glide.with(holder.ViewContext)
                    .load(msg.getProfileUrl())
                    .into(holder.ProfileImageView);
        }

        if (msg.getPhotoUrl() != null && !msg.getPhotoUrl().isEmpty()) {
            // Photo message
            Glide.with(holder.ViewContext)
                    .load(msg.getPhotoUrl())
                    .into(holder.PhotoImageView);
            //holder.PhotoImageView.setBackgroundColor(MessageListTheme.ColorMessageBackground);
        } else {
            // Text message
            holder.TextView.setText(msg.getText());
        }
    }

    public void addMessageData(MessageData data) {
        // DO nothing
    }
}
