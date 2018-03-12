package com.example.tarikpc.tinder.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tarikpc.tinder.R;

/**
 * Created by Tarik PC on 09-03-18.
 */

public class ChattingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mMessage;
    public LinearLayout mContainer;

    public ChattingViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.messageBubbleText);
        mContainer = itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View view) {

    }
}
