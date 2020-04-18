package com.example.planewars;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(context,"629e18f16af95f05675210310a9b62d1");
    }

    public static Context getContext(){
        return context;
    }
}
