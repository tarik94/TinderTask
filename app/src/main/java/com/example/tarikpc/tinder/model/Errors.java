package com.example.tarikpc.tinder.model;

import io.realm.RealmObject;

/**
 * Created by Tarik PC on 06-03-18.
 */

public class Errors extends RealmObject {
    private String property;
    private Constraints constraints;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Constraints getConstraints() {
        return constraints;
    }

    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }
}
