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
        final ImageView ImageView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            TextView = (TextView) itemView.findViewById(R.id.message_item_text);
            ImageView = (ImageView) itemView.findViewById(R.id.message_item_user_photo);
            ViewContext = itemView.getContext();
        }
    }


    protected MessageData parseSnapshot(DataSnapshot snapshot) {
        MessageData msg = snapshot.getValue(MessageData.class);

        if (msg != null
                && msg.name != null && msg.name.equals(user.getDisplayName())) {
            msg.messageType = MessageData.MessageType.MESSAGE_FROM_ME;
        } else {
            msg.messageType = MessageData.MessageType.MESSAGE_FROM_OTHERS;
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
            default:
                layout = R.layout.message_item_mine;
                break;
        }

        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).messageType.ordinal();
    }

    @Override
    protected void populateViewHolder(MessageViewHolder holder, MessageData msg, int position) {
        holder.TextView.setText(msg.text);
        if (msg.photoUrl == null) {
            holder.ImageView.setImageDrawable(ContextCompat.getDrawable(holder.ViewContext,
                    R.drawable.firebase_logo));
        } else {
            Glide.with(holder.ViewContext)
                    .load(msg.photoUrl)
                    .into(holder.ImageView);
        }
    }

    public void addMessageData(MessageData data) {
        // DO nothing
    }
}
