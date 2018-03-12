package com.example.tarikpc.tinder.model;

import io.realm.RealmObject;

/**
 * Created by Tarik PC on 06-03-18.
 */

public class UserInfo extends RealmObject {
    private Data data;
    private Error error;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
