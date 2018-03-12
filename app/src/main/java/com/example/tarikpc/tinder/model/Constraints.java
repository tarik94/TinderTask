package com.example.tarikpc.tinder.model;

import io.realm.RealmObject;

/**
 * Created by Tarik PC on 06-03-18.
 */

public class Constraints extends RealmObject {
    private String isEmail;
    private String minLength;

    public String getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(String isEmail) {
        this.isEmail = isEmail;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }
}
