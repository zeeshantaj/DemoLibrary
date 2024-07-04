package com.example.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import com.example.demodialog.DbHandler;
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

//        overlayPermission();
//        readWriteAllFileAccessPermission();
        loadData();
    }

    void loadData(){
        if (checkPermission()){
            ConstraintLayout downloadLayout = findViewById(R.id.downloadLayout);
            TextView videoDownloadTxt = findViewById(R.id.videoDownload);
            TextView totalVideoTxt = findViewById(R.id.textView6);
            TextView currentVideoTxt = findViewById(R.id.textView15);

            File videoPathToSave = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/FromLibraryDownloadedVideo/");
            String videoType = "menuBoardScreenType"; //change later
            List<VideoAds> videoAdsList = new ArrayList<>();

            // food video
            VideoAds videoAds = new VideoAds(1,"M-61","https://jumbilinresource.blob.core.windows.net/videoscreen/bs4/16/20247:13:27PM.mp4");
            videoAdsList.add(videoAds);

            // coffee video
            VideoAds videoAds1 = new VideoAds(2,"M-61","https://jumbilinresource.blob.core.windows.net/videoscreen/bs2/14/20244:06:37PM.mp4");
            videoAdsList.add(videoAds1);

            // water ads video
            VideoAds videoAds2 = new VideoAds(3,"M-61","https://jumbilinresource.blob.core.windows.net/videoscreen/bs4/22/20247:01:06AM.mp4");
            videoAdsList.add(videoAds2);



            DbHandler dbHandler = new DbHandler(this);


            DownloadVideos downloadVideos = new DownloadVideos(this,
                    videoPathToSave,downloadLayout,videoDownloadTxt,totalVideoTxt,currentVideoTxt,
                    videoType,videoAdsList,dbHandler,this);

//            downloadVideos.compareAndDeleteOldVideos();
//            downloadVideos.updateVideosIdentifiers();

            if (videoAdsList.size() > 0) {
                downloadVideos.manageVideos(videoAdsList, "");
            }else {
                Log.d("MyApp","video size less than 0");
                downloadVideos.compareAndDeleteOldVideos();
                downloadVideos.updateVideosIdentifiers();

            }

        }

    }

//    private void overlayPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(this)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, 232);
//        }
//    }
//
//    private void readWriteAllFileAccessPermission() {
//        if (Build.VERSION.SDK_INT >= 30) {
//            if (!Environment.isExternalStorageManager()) {
//                Intent getpermission = new Intent();
//                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                startActivity(getpermission);
//            }
//        }
//    }

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