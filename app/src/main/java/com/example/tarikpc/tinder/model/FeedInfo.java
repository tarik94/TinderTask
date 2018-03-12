package com.example.tarikpc.tinder.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class FeedInfo extends RealmObject{

    @SerializedName("data")
    @Expose
    private RealmList<FeedData> data = null;
    @SerializedName("page")
    @Expose
    private String page;

    public List<FeedData> getData() {
        return data;
    }

    public void setData(RealmList<FeedData> data) {
        this.data = data;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

}