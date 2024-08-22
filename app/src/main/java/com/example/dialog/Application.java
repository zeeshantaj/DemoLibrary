package com.example.dialog;

import android.content.pm.ApplicationInfo;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WebConst webConst = new WebConst();
//        webConst.checkForDevelopmentUrls();

        Log.d("MyApp", "application class onCreate");
        // Register the lifecycle observer

        // to call observer class for app lifecycle (to check if app is in background or in foreground)
        AppLifeCycleObserver observer = new AppLifeCycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(observer);

//        if (!BuildConfig.DEBUG) {
//            Log.d("MyApp","released ");
//        }else {
//            Log.d("MyApp","debug ");
//        }
    }
}
