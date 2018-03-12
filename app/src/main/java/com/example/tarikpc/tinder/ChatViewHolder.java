package com.example.tarikpc.tinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tarikpc.tinder.chatting.ChattingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tarik PC on 09-03-18.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mName;
    public String userId;

    private String chatID;
    private Boolean hasID = false;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    public ChatViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        mName = itemView.findViewById(R.id.profileName);
    }

    @Override
    public void onClick(View view) {
        /*DatabaseReference chatUnique = FirebaseDatabase.getInstance().getReference().child("Chats").child(auth.getCurrentUser().getUid()).child(userId);
        Log.d("before if", "onClick: this is ChatID    " + chatUnique.getKey());
        if(chatUnique.getKey().isEmpty()){
            DatabaseReference currentUserChatDB = FirebaseDatabase.getInstance().getReference().child("Chats").child(auth.getCurrentUser().getUid()).child(userId);
            DatabaseReference selectedUserChatDB = FirebaseDatabase.getInstance().getReference().child("Chats").child(userId).child(auth.getCurrentUser().getUid());
            currentUserChatDB.setValue(true);
            selectedUserChatDB.setValue(true);
            Log.d("end of if", "onClick: this is ChatID" + chatUnique);

*/


            reference.child("Chats").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(userId)){
                        DatabaseReference newMessageDB = reference.child("Chats").child(auth.getCurrentUser().getUid()).child(userId).push();
                        chatID = newMessageDB.getKey();
                        DatabaseReference newMessageDBB = reference.child("Chats").child(userId).child(auth.getCurrentUser().getUid()).child(chatID);
                        DatabaseReference chattingDB = reference.child("Messages").child(chatID);
                        chattingDB.setValue(true);
                        newMessageDB.setValue(true);
                        newMessageDBB.setValue(true);
                    } else {
                        dataSnapshot.child(userId).getValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

//
//        if(!hasID){
//            chatID = reference.child("Chats").child(auth.getCurrentUser().getUid()).child(userId).getKey();
//            Log.d("Fake chat id .. :'(", "onClick: " + chatID);
//        }




       // }
        Intent intent = new Intent(view.getContext(), ChattingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("SelectedId", userId);
        //bundle.putString("ChatID", chatID);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
