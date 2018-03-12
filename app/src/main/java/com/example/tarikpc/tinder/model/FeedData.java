package com.example.tarikpc.tinder.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class FeedData extends RealmObject {

    @SerializedName("issuingUser")
    @Expose
    private IssuingUser issuingUser;
    @SerializedName("attachments")
    @Expose
    private RealmList<Attachment> attachments = null;

    public IssuingUser getIssuingUser() {
        return issuingUser;
    }

    public void setIssuingUser(IssuingUser issuingUser) {
        this.issuingUser = issuingUser;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(RealmList<Attachment> attachments) {
        this.attachments = attachments;
    }

}