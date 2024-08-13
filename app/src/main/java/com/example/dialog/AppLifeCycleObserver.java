package com.example.dialog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OneTimeWorkRequestKt;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class AppLifeCycleObserver implements LifecycleObserver {
    private Context context;

    public AppLifeCycleObserver(Context context) {
        this.context = context;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {

        // App goes to the background (or is closed)
      //scheduleAppRelaunch();
        //doRestart(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("relaunchState", Context.MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean("isRelaunch", true);
        if (isChecked){
            Log.d("MyApp","app is in background ");
            Log.d("MyApp","relaunch checked is enabled");
            WorkRequest relaunchRequest = new OneTimeWorkRequest.Builder(RelaunchWork.class)
                    .setInitialDelay(15, TimeUnit.SECONDS)
                    .addTag("relaunchWork")
                    .build();

            WorkManager.getInstance(context).enqueue(relaunchRequest);

        }else {
            Log.d("MyApp","relaunch checked is disabled ");
        }


    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAppResume(){
        Log.d("MyApp","app is resume");
        WorkManager.getInstance(context).cancelAllWorkByTag("relaunchWork");
        Log.d("MyApp", "Scheduled relaunch canceled");
    }
}
