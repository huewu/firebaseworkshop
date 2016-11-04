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

import java.util.ArrayList;

public class MessageDataAdapter extends RecyclerView.Adapter<MessageDataAdapter.MessageViewHolder> {

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

    private ArrayList<MessageData> msgDataArrayList = new ArrayList<>();

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
    public int getItemCount() {
        return msgDataArrayList.size();
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        final MessageData msg = msgDataArrayList.get(position);

        holder.TextView.setText(msg.getText());
        if (msg.getPhotoUrl()== null) {
            holder.ImageView.setImageDrawable(ContextCompat.getDrawable(holder.ViewContext,
                    R.drawable.firebase_logo));
        } else {
            Glide.with(holder.ViewContext)
                    .load(msg.getPhotoUrl())
                    .into(holder.ImageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return msgDataArrayList.get(position).getMessageType().ordinal();
    }

    public void addMessageData(MessageData data) {
        if (msgDataArrayList.add(data)) {
            notifyItemChanged(msgDataArrayList.size()-1);
        }
    }
}
