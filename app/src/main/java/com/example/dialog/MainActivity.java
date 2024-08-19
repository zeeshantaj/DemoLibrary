package com.example.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demodialog.DownloadCallback;
import com.example.demodialog.DownloadVideos;
import com.example.demodialog.VideoAds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.example.dialog.BuildConfig;
public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 502;
    private long previousRxBytes = 0;
    private long previousTimeStamp = 0;
    private final Handler handler = new Handler();
    private TextView internetSpeedTextView;
    private final int REQUEST_CODE_FOREGROUND_SERVICE = 101;
    private Spinner spinner;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwitchCompat switchCompat = findViewById(R.id.relaunchSwitch);
        spinner = findViewById(R.id.relaunchTimerSpinner);
        sharedPreferences = getSharedPreferences("relaunchState", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boolean isChecked = sharedPreferences.getBoolean("isRelaunch", false);
        switchCompat.setChecked(isChecked);



        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                Toast.makeText(MainActivity.this, "Relaunch enabled ", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Relaunch disabled ", Toast.LENGTH_SHORT).show();
            }
            setTimerSpinner(b);
            sharedPreferences = getSharedPreferences("relaunchState", Context.MODE_PRIVATE);
            editor.putBoolean("isRelaunch", b);
            editor.apply();
        });

        Button startBtn = findViewById(R.id.startDownloadBtn);
        startBtn.setOnClickListener(view -> {
            loadData();
        });
        internetSpeedTextView = findViewById(R.id.speedTxt);
        startUpdatingInternetSpeed();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                    requestOverlayPermission();

                }

            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                if (!Settings.canDrawOverlays(this)) {
                    // Permission is not granted
                    requestOverlayPermission();
                }
            }


    }

    private void setTimerSpinner(boolean isEnable){
        if (isEnable){
            spinner.setVisibility(View.VISIBLE);

            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                    this,
                    R.array.relaunchArray,
                    android.R.layout.simple_spinner_item);

            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter1);

//            String time = spinner.getSelectedItem().toString();
//            int givenTime = Integer.parseInt(time);
//            editor.putInt("RelaunchTimer", givenTime);
//            editor.apply();

        }
        else {
            spinner.setVisibility(View.GONE);
        }
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_FOREGROUND_SERVICE);
        }
    }
    public String getInternetSpeed() {
        long currentRxBytes = TrafficStats.getTotalRxBytes();
        long currentTimeStamp = System.currentTimeMillis();

        if (previousRxBytes == 0) {
            previousRxBytes = currentRxBytes;
            previousTimeStamp = currentTimeStamp;
            return "Calculating...";
        }

        long dataConsumed = currentRxBytes - previousRxBytes;
        long timeInterval = currentTimeStamp - previousTimeStamp;

        if (timeInterval == 0) {
            return "Calculating...";
        }

        long speed = (dataConsumed * 1000) / timeInterval; // Speed in bytes per second

        previousRxBytes = currentRxBytes;
        previousTimeStamp = currentTimeStamp;

        if (speed >= 1024 * 1024) {
            return String.format("%.2f MB/s", (float) speed / (1024 * 1024));
        } else if (speed >= 1024) {
            return String.format("%.2f KB/s", (float) speed / 1024);
        } else {
            return speed + " B/s";
        }
    }

    private void startUpdatingInternetSpeed() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String speed = getInternetSpeed();
                internetSpeedTextView.setText("internet speed: "+speed);
                handler.postDelayed(this, 1000); // Update every second
            }
        }, 1000);
    }

    void loadData() {
        if (checkPermission()) {

            File videoPathToSave = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/FromDemoLibrary/");
            String videoType = "menuBoardScreenType"; //change later
            List<VideoAds> videoAdsList = new ArrayList<>();

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
//
//            videoAdsList.add(new VideoAds(5, "M-61", "https://jumbilinresource.blob.core.windows.net/videoscreen/bs8/5/20243:36:45PM.mp4"));
//            videoAdsList.add(new VideoAds(1, "M-61", "https://jumbilinresource.blob.core.windows.net/videoscreen/bs4/16/20247:13:27PM.mp4"));


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

//                downloadVideos.compareAndDeleteOldVideos();

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
                            Log.d("MyApp", "Internet :  connected connected");
                            //loadData();

                        } else {
                            Log.d("MyApp", "Internet :  not connected");
                        }
                    } else {
                        Log.d("MyApp", "Internet :  not connected");
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

    private boolean checkWriteExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API level 33) or later
                if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
                    return false;
                }
            } else {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
                    return false;
                }
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
         //   loadData();
        } else {
            finish();
        }
        if (requestCode == REQUEST_CODE_FOREGROUND_SERVICE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (Settings.canDrawOverlays(this)) {
                    // Permission granted, proceed with your logic
                    Toast.makeText(this, "Overlay permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Overlay permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}