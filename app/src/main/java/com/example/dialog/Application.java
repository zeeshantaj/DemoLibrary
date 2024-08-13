package com.example.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WebConst webConst = new WebConst();
//        webConst.checkForDevelopmentUrls();

        Log.d("MyApp","application class onCreate");
        // Register the lifecycle observer


        AppLifeCycleObserver observer = new AppLifeCycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(observer);

    }
}
