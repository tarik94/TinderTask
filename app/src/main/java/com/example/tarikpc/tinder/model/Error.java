package com.example.tarikpc.tinder.model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Tarik PC on 06-03-18.
 */

public class Error extends RealmObject {
    private String message;
    private RealmList<Errors> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RealmList<Errors> getErrors() {
        return errors;
    }

    public void setErrors(RealmList<Errors> errors) {
        this.errors = errors;
    }
}
