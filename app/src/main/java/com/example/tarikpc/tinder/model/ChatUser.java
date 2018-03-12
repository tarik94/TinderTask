package com.example.tarikpc.tinder.model;

/**
 * Created by Tarik PC on 09-03-18.
 */

public class ChatUser {
    private String userId;
    private String name;

    public ChatUser(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
