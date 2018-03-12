package com.example.tarikpc.tinder.chatting;

/**
 * Created by Tarik PC on 09-03-18.
 */

public class Message {
    private String message;
    private String from;
    private Boolean thisUser;

    public Message(String message, String from, Boolean thisUser) {
        this.message = message;
        this.from = from;
        this.thisUser = thisUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Boolean getThisUser() {
        return thisUser;
    }

    public void setThisUser(Boolean thisUser) {
        this.thisUser = thisUser;
    }
}
