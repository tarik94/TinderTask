package com.example.tarikpc.tinder.chatting;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tarikpc.tinder.R;

import java.util.List;

/**
 * Created by Tarik PC on 09-03-18.
 */

public class ChattingAdapter extends RecyclerView.Adapter<ChattingViewHolder>{

    private List<Message> messages;
    private Context context;

    public ChattingAdapter(List<Message> messages, Context context){
        this.messages = messages;
        this.context = context;
    }

    @Override
    public ChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        ChattingViewHolder chatViewHolder = new ChattingViewHolder(layoutView);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChattingViewHolder holder, int position) {
        holder.mMessage.setText(messages.get(position).getMessage());
        if(messages.get(position).getThisUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        } else {
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
