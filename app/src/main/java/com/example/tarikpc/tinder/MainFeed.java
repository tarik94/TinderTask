package com.example.tarikpc.tinder;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.tarikpc.tinder.model.Attachment;
import com.example.tarikpc.tinder.model.Data;
import com.example.tarikpc.tinder.model.FeedData;
import com.example.tarikpc.tinder.model.FeedInfo;
import com.example.tarikpc.tinder.model.IssuingUser;
import com.google.firebase.auth.FirebaseAuth;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFeed extends FragmentActivity {

    private Button mSignOut;
    private Button mChat;

    private List<FeedData> profiles;
    private CardAdapter cardAdapter;
    private int i;
    private int page = 1;

    UserAPI userAPI;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        auth = FirebaseAuth.getInstance();

        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        mSignOut = findViewById(R.id.signOut);
        mChat = findViewById(R.id.chat);

        mSignOut.setOnClickListener(view -> {
            try(Realm realmInstance = Realm.getDefaultInstance()) {
                realmInstance.executeTransaction((realm) -> realm.delete(Data.class));
            }
            auth.signOut();
            Intent intent = new Intent(MainFeed.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        mChat.setOnClickListener(view -> {
            Intent intent = new Intent(MainFeed.this, ChatActivity.class);
            startActivity(intent);
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://ci-api.favor.co/").addConverterFactory(GsonConverterFactory.create()).build();
        userAPI = retrofit.create(UserAPI.class);

        profiles = new ArrayList<>();
        cardAdapter = new CardAdapter(this,R.layout.fragment_user_card ,profiles);
        flingContainer.setAdapter(cardAdapter);

        try(Realm realmInstance = Realm.getDefaultInstance()) {
            final List<FeedData> feedData = realmInstance.where(FeedData.class).findAll();
            if(feedData != null && feedData.size() > 0 && feedData.get(0).getIssuingUser() != null){
                profiles.clear();
                for(int i = 0; i < feedData.size(); i++){
                    String firstName = feedData.get(i).getIssuingUser().getFirstName();
                    String lastName = feedData.get(i).getIssuingUser().getLastName();
                    String email = feedData.get(i).getIssuingUser().getEmail();
                    String profileImage = feedData.get(i).getIssuingUser().getProfileImage();
                    IssuingUser issuingUser = new IssuingUser();
                    issuingUser.setFirstName(firstName);
                    issuingUser.setLastName(lastName);
                    issuingUser.setEmail(email);
                    issuingUser.setProfileImage(profileImage);
                    List<Attachment> attachments = new ArrayList<>();
                    for(int j = 0; j < feedData.get(i).getAttachments().size(); j++){
                        String original = feedData.get(i).getAttachments().get(j).getOriginal();
                        Attachment attachment = new Attachment();
                        attachment.setOriginal(original);
                        attachments.add(attachment);
                    }
                    FeedData temp = new FeedData();
                    temp.setIssuingUser(issuingUser);
                    RealmList<Attachment> tempAtta = new RealmList<>();
                    temp.setAttachments(tempAtta);
                    temp.getAttachments().addAll(attachments);
                    profiles.add(temp);
                }
                Collections.reverse(profiles);      //Drugi flip
                cardAdapter.notifyDataSetChanged();
            } else {
                Data data = realmInstance.where(Data.class).findFirst();
                get("Bearer " + data.getToken());
            }
        }

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                try(Realm realmInstance = Realm.getDefaultInstance()){
                    realmInstance.executeTransaction((realm) -> {
                        List<FeedData> temp = realm.where(FeedData.class).findAll();
                        if(temp != null)
                            temp.get(temp.size() - 1).deleteFromRealm();        //Brisanje posljednjeg
                        else{
                            FeedInfo feedInfo = realm.where(FeedInfo.class).findFirst();
                            feedInfo.deleteFromRealm();
                        }
                    });
                }
                if(profiles.size() > 0)
                    profiles.remove(0);
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                somePostMethod("No!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                somePostMethod("Yes!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if(profiles.size() < 3){
                    page++;
                    try(Realm realmInstance = Realm.getDefaultInstance()) {
                        Data data = realmInstance.where(Data.class).findFirst();
                        get("Bearer " + data.getToken());
                    }
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
//                View view = flingContainer.getSelectedView();
//                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
//                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);


            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainFeed.this, "Clicked!", Toast.LENGTH_SHORT);
            }
        });
    }

    private void somePostMethod(String someToken) {
        Boolean onResponse = true;
        Boolean onFailure = true;
        if(onResponse){
            //check db if empty send all then delete
            Toast.makeText(MainFeed.this, someToken, Toast.LENGTH_SHORT).show();
        }
        if(onFailure){
            //put in db
        }
    }

    public void get(String token){
        userAPI.getFeed(token, page).enqueue(new Callback<FeedInfo>() {
            @Override
            public void onResponse(Call<FeedInfo> call, Response<FeedInfo> response) {
                final FeedInfo feedInfo = response.body();
                //profiles.clear();
                if(feedInfo.getData() != null && feedInfo.getData().size() > 0) {
                    profiles.addAll(feedInfo.getData());
                    cardAdapter.notifyDataSetChanged();
                    try (Realm realmInstance = Realm.getDefaultInstance()) {
                        Collections.reverse(feedInfo.getData());        //Prvi flip
                        realmInstance.executeTransaction((realm) -> realm.insertOrUpdate(feedInfo));
                    }
                } else {
                    page --;
                }
            }

            @Override
            public void onFailure(Call<FeedInfo> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}