package com.example.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.demodialog.DbHandler;
import com.example.demodialog.DownloadCallback;
import com.example.demodialog.DownloadVideos;
import com.example.demodialog.VideoAds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 502;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startBtn = findViewById(R.id.startDownloadBtn);
        startBtn.setOnClickListener(view -> {
        });

    }

    void loadData() {
        if (checkPermission()) {

            File videoPathToSave = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/FromDemoLibrary/");
            String videoType = "menuBoardScreenType"; //change later
            List<VideoAds> videoAdsList = new ArrayList<>();

            // food video
            VideoAds videoAds = new VideoAds(1, "M-61", "https://jumbilinresource.blob.core.windows.net/videoscreen/bs4/16/20247:13:27PM.mp4");
            videoAdsList.add(videoAds);
//
//         //    coffee video
//            VideoAds videoAds1 = new VideoAds(2, "M-61", "https://jumbilinresource.blob.core.windows.net/videoscreen/bs2/14/20244:06:37PM.mp4");
//            videoAdsList.add(videoAds1);
////
//            // water ads video
//            VideoAds videoAds2 = new VideoAds(3, "M-61", "https://jumbilinresource.blob.core.windows.net/videoscreen/bs4/22/20247:01:06AM.mp4");
//            videoAdsList.add(videoAds2);
//
//            //side bakery video
//            VideoAds videoAds3 = new VideoAds(4, "M-61", "https://jumbilinresource.blob.core.windows.net/videoscreen/bs6/17/20244:57:20PM.mp4");
//            videoAdsList.add(videoAds3);


            DownloadVideos downloadVideos = new DownloadVideos(this,
                    videoPathToSave,videoAdsList, videoType, true, new DownloadCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(MainActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                }
            });


            if (videoAdsList.size() > 0) {
                downloadVideos.manageVideos(videoAdsList, "",2);
            } else {
                Log.d("MyApp", "video size less than 0");
                downloadVideos.updateVideosIdentifiers();
                downloadVideos.compareAndDeleteOldVideos();

            }

        }

    }

    public BroadcastReceiver networkConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null) {
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {

                            Log.d("usama lis", "Internet :  connected connected");


                            // internet connected call download
                            loadData();
//                            initializeSocket();

                        } else {

                            Log.d("usama lis", "Internet :  not connected");
                            //  setOffLineSetup();
                        }
                    } else {

                        Log.d("usama lis", "Internet :  not connected");
                    }
                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkConnection, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadData();
        } else {
            finish();
        }
    }
}