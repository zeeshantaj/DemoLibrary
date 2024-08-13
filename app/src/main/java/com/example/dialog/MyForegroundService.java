package com.example.dialog;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION;
import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

public class MyForegroundService extends Service {
    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        Log.d("MyApp", "Foreground Service onStartCommand called");

//        Intent launchIntent = new Intent(this, MainActivity.class);
//        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                | Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//
//        try {
//            startActivity(launchIntent);
//            Log.d("MyApp", "MainActivity launched successfully");
//        } catch (Exception e) {
//            Log.e("MyApp", "Error launching MainActivity: " + e.getMessage());
//        }


//        Intent intent1 = getPackageManager()
//                .getLaunchIntentForPackage(getPackageName());
//        if (intent1 != null) {
//            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent1);
//        try {
//            startActivity(intent1);
//            Log.d("MyApp", "MainActivity launched successfully"+getPackageName());
//        } catch (Exception e) {
//            Log.e("MyApp", "Error launching MainActivity: " + e.getMessage());
//        }
//
//        }
//
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);

        // stop the service after launching the service

        // launch the app
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setComponent(new ComponentName("com.example.dialog","com.example.dialog.MainActivity"));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.example.dialog","com.example.dialog.MainActivity"));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
                Log.d("MyApp", "MainActivity launched successfully");
            } catch (Exception e) {
                Log.e("MyApp", "Error launching MainActivity: " + e.getMessage());
            }

        }

        stopSelf();
        return START_NOT_STICKY;

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyApp","service on create called ");
        // Ensure the notification channel is created (Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NotificationChannel channel = new NotificationChannel(
                    "CHANNEL_ID",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
        // todo launch the app on android 11
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // todo launch the app on android 11


        // Create a notification to run the service in the foreground
        Notification notification = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("App Relaunch Service")
                .setContentText("Relaunching the app...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)  // Ensure you have this icon in your drawable folder
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Optional, for setting the notification priority
                .setContentIntent(pendingIntent)
                .build();

        // Start the service in the foreground with the notification
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(1, notification);
        } else {
            startForeground(1, notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        }

        //startForeground(1, notification);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            ServiceCompat.startForeground(1,notification, FOREGROUND_SERVICE_TYPE_LOCATION);
//        }
    }
}
