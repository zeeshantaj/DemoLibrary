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

        Log.d("MyApp","application class onCreate");
        // Register the lifecycle observer

        // to call observer class for app lifecycle (to check if app is in background or in foreground)
        AppLifeCycleObserver observer = new AppLifeCycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(observer);

//        boolean IS_Debug = ( 0 != ( getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
//
//        if (IS_Debug){
//            Log.d("MyApp","debug "+IS_Debug);
//        }else {
//            Log.d("MyApp","debug "+IS_Debug);
//        }

//        if (BuildConfig.IS_SIGNED) {
//            Log.d("MyApplication", "This APK is signed.");
//            if (WebConst.BASE_URL.contains("development")){
//                throw new IllegalStateException("BASE_URL contains 'development' in release build!");
//            }
//            // Perform actions specific to the signed APK
//            // Further checks or actions can be added here
//        } else {
//
//            Log.d("MyApplication", "This APK is not signed.");
//
//        }

    }
}
