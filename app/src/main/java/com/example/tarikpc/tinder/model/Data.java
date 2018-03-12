package com.example.tarikpc.tinder.model;

import io.realm.RealmObject;

/**
 * Created by Tarik PC on 06-03-18.
 */

public class Data extends RealmObject {
    private String token;
    private String firebaseToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
