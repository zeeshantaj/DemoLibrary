package com.example.dialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RelaunchWork extends Worker {
    Context context;
    public RelaunchWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Relaunch the app
        Log.d("MyApp","do work called");
//        Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
//        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        getApplicationContext().startActivity(launchIntent);
//
//


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            Intent serviceIntent = new Intent(getApplicationContext(), MyForegroundService.class);
            ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

        }

//        else {
//            // todo working in android 9
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setComponent(new ComponentName("com.example.dialog","com.example.dialog.MainActivity"));
//        //intent.setComponent(new ComponentName(getPackageName(),MainActivity.class));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        }



        return Result.success();
    }
}
