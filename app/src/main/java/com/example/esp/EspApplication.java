package com.example.esp;

import android.app.Application;

import com.example.esp.api.Api;

public class EspApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Api.init();
    }
}
