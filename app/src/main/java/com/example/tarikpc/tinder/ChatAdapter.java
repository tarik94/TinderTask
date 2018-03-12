package com.example.tarikpc.tinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tarikpc.tinder.ChatViewHolder;
import com.example.tarikpc.tinder.R;
import com.example.tarikpc.tinder.model.ChatUser;

import java.util.List;

/**
 * Created by Tarik PC on 09-03-18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{

    private List<ChatUser> chatUsers;
    private Context context;

    public ChatAdapter(List<ChatUser> chatUsers, Context context){
        this.chatUsers = chatUsers;
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        ChatViewHolder chatViewHolder = new ChatViewHolder(layoutView);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.mName.setText(chatUsers.get(position).getName());
        holder.userId = chatUsers.get(position).getUserId();
    }

    @Override
    public int getItemCount() {
        return chatUsers.size();
    }
}
