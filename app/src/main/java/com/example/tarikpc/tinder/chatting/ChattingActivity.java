package com.example.tarikpc.tinder.chatting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tarikpc.tinder.*;
import com.example.tarikpc.tinder.model.ChatUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChattingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChattingAdapter;
    private LinearLayoutManager mLayoutManager;

    private ArrayList<Message> messages = new ArrayList<>();

    private String selectedUser;
    private String currentUserId;
    private String chatID;

    private EditText mSendMessage;
    private Button mSend;

    DatabaseReference mDatabaseChat;
    DatabaseReference mDatabaseChatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        selectedUser = getIntent().getExtras().get("SelectedId").toString();

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId).child(selectedUser);

        getChatID2();

        mSendMessage = findViewById(R.id.sendingMessage);
        mSend = findViewById(R.id.sendMessage);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        mRecyclerView = findViewById(R.id.chattingList);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(ChattingActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mChattingAdapter = new ChattingAdapter(messages, ChattingActivity.this);
        mRecyclerView.setAdapter(mChattingAdapter);
    }

    private void sendMessage() {
        String sendMessageText = mSendMessage.getText().toString();
        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDB = mDatabaseChatting.push();
            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserId);
            newMessage.put("text", sendMessageText);
            newMessageDB.setValue(newMessage);
        }
        mSendMessage.setText(null);
    }
/*
    private void getChatID(){
        mDatabaseChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Iterable<DataSnapshot> onlyOne = dataSnapshot.getChildren();
                    for (DataSnapshot ds : onlyOne  ) {
                        chatID = ds.getKey();
                    }
                    //chatID = dataSnapshot.getValue().toString();

                    Log.d("WTF !!! ", chatID);
                    mDatabaseChatting = FirebaseDatabase.getInstance().getReference().child("Messages").child(chatID);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(mDatabaseChatting == null){
            getChatID();
        }
    }
*/
    private void getChatID2() {
        FirebaseDatabase.getInstance().getReference().child("Chats").child(selectedUser).child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Iterable<DataSnapshot> onlyOne = dataSnapshot.getChildren();
                    for (DataSnapshot ds : onlyOne  ) {
                        chatID = ds.getKey();
                    }
                    mDatabaseChatting = FirebaseDatabase.getInstance().getReference().child("Messages").child(chatID);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages() {
        mDatabaseChatting.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String from = null;
                    if(dataSnapshot.child("text").getValue() != null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue() != null){
                        from = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if(message != null && from != null){
                        Boolean currentUser = false;
                        if(from.equals(currentUserId)){
                            currentUser = true;
                        }
                        Message newMessage = new Message(message, from, currentUser);
                        messages.add(newMessage);
                        mChattingAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
