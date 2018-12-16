package com.example.mobeensohail.firstproject;

import android.app.Application;

import com.firebase.client.Firebase;

public class firstproject extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
