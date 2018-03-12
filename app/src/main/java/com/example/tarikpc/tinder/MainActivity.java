package com.example.tarikpc.tinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tarikpc.tinder.model.Data;
import com.example.tarikpc.tinder.model.Errors;
import com.example.tarikpc.tinder.model.User;
import com.example.tarikpc.tinder.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private Button mRegister;

    UserAPI userAPI;
    UserInfo userInfo;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);

        auth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(MainActivity.this, MainFeed.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        try(Realm realmInstance = Realm.getDefaultInstance()) {
            final FirebaseUser user = auth.getCurrentUser();
            Data data = realmInstance.where(Data.class).findFirst();
            if(data != null && user != null){
                Intent intent = new Intent(MainActivity.this, MainFeed.class);
                startActivity(intent);
                finish();
            }
        }

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://ci-api.favor.co/").addConverterFactory(GsonConverterFactory.create()).build();
        userAPI = retrofit.create(UserAPI.class);

        mLogin.setOnClickListener(view -> {
            User user = new User();
            user.setEmail(mUsername.getText().toString());
            user.setPassword(mPassword.getText().toString());
            post("login", user);
        });

        mRegister.setOnClickListener(view -> {
            User user = new User();
            user.setEmail(mUsername.getText().toString());
            user.setPassword(mPassword.getText().toString());
            post("register", user);
        });
    }

    public void post(String action, User user){
        userAPI.loginUser(action, user).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                userInfo = response.body();
                if (userInfo.getData() != null) {
                    if(action.equals("register")) {
                        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                                } else {
                                    try (Realm realmInstance = Realm.getDefaultInstance()) {
                                        realmInstance.executeTransaction((realm) -> realm.insertOrUpdate(userInfo.getData()));
                                    }
                                    String userId = auth.getCurrentUser().getUid();
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("name");
                                    databaseReference.setValue(user.getEmail());
                                    DatabaseReference chatDB = FirebaseDatabase.getInstance().getReference().child("Chats").child(userId);
                                    chatDB.setValue(true);
                                }
                            }
                        });
                    } else if(action.equals("login")){
                        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
                                } else {
                                    try (Realm realmInstance = Realm.getDefaultInstance()) {
                                        realmInstance.executeTransaction((realm) -> realm.insertOrUpdate(userInfo.getData()));
                                    }
                                }
                            }
                        });
                    }
                } else if(userInfo.getError().getErrors() != null) {
                    for (Errors e : userInfo.getError().getErrors()) {
                        if (e.getProperty().equals("email")) {
                            mUsername.setError(e.getConstraints().getIsEmail());
                        } else if (e.getProperty().equals("password")) {
                            mPassword.setError(e.getConstraints().getMinLength());
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, userInfo.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
