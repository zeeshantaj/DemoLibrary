package com.example.dialog;

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

        // to call observer class for app lifecycle (to check if app is in background or in foreground)
        AppLifeCycleObserver observer = new AppLifeCycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(observer);

        if (!BuildConfig.DEBUG && WebConst.BASE_URL.contains("development")) {
            throw new IllegalStateException("BASE_URL contains 'development' in release build!");
        }
        else {
            Log.d("MyApp","else APK Released in mode "+BuildConfig.BUILD_TYPE);
        }

    }
}
