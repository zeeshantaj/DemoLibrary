package com.example.demodialog;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.LongFunction;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

public class DownloadVideos {

    Activity activity;
    DbHandler dbHandler;
    List<VideosIdentifierListDbModel> oldVideosIdentifierListDbModels;
    File videoPath;
    DownloadManager.Request request1;
    DownloadManager downloadManager1;
    String videoType;
    private List<VideoAds> videoAdsList;
    int currentVideoIndex = 0;
    int totalVideos = 0;
    int dl_progress = 0;
    int progessValue = 0;
    boolean videoDownloaded = true;
    boolean downloading;
    DownloadDialog downloadDialog;
    DownloadCallback downloadCallback;
    double downloadedMB;
    double fileSizeMB;

    public DownloadVideos(Activity activity,
                          File videoPath,
                          List<VideoAds> videoAdsList,
                          String videoType,
                          boolean downloading,DownloadCallback downloadCallback) {
        this.activity = activity; // Changed to Activity context
        this.videoPath = videoPath;
        this.videoAdsList = videoAdsList;
        this.videoType = videoType;
        this.downloading = downloading;
        this.downloadCallback = downloadCallback;
        dbHandler = new DbHandler(activity);
    }

    public void manageVideos(List<VideoAds> videoAdsList, String layoutName,int SCREEN_TYPE) {
        // SCREEN_TYPE = 1 is for main screen dialog
        // SCREEN_TYPE = 2 is for config screen dialog
        downloadDialog = new DownloadDialog(activity,SCREEN_TYPE);

        // to get round bg of dialog
        downloadDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (SCREEN_TYPE == 1){
            //to remove the dark bg of dialog
            downloadDialog.getWindow().setDimAmount(0);
        }
        downloadDialog.show();

        if (VideoOperations.createCustomFolder(videoPath)) {
            updateVideosIdentifiers();
            currentVideoIndex = 0;
            totalVideos = videoAdsList.size();
            downloadVideo(videoAdsList.get(0).getVideoUrl(), videoAdsList.get(0).getId(), currentVideoIndex, totalVideos);
        } else {
            UIHelper.showErrorDialog(activity, "Folder creation failed", "Check for storage permissions", 1);
        }
    }

    public void updateVideosIdentifiers() {
        oldVideosIdentifierListDbModels = dbHandler.getVideosIdentifier();
        VideoOperations.updateVideosIdentifiers(oldVideosIdentifierListDbModels, dbHandler);
    }

    public void compareAndDeleteOldVideos() {
        oldVideosIdentifierListDbModels = dbHandler.getVideosIdentifier();
        VideoOperations.compareAndDeleteOldVideos(videoPath, oldVideosIdentifierListDbModels);
    }

    private void downloadVideo(String videoUrl, int id, int vIndex, int totalVideos) {

        String randomId = UUID.randomUUID().toString();
        dbHandler.addVideosIdentifier(randomId, 1);


        downloadDialog.updateTotalVideos((vIndex + 1) + "/" + totalVideos);
        downloadDialog.updateCurrentVideos("Add Video " + (vIndex + 1));
        downloadDialog.updateVideoDownload("0%");


        request1 = new DownloadManager.Request(Uri.parse(videoUrl));
        request1.setTitle("Ad Video " + vIndex);
        request1.setDescription("Downloading Video.. Please Wait");
        String cookie1 = CookieManager.getInstance().getCookie(videoUrl);
        request1.addRequestHeader("cookie1", cookie1);
        request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //***************************************new work for android 10 & Above*******************************************************
        String fileName = randomId + videoType + " " + ".mp4";
        File file = new File(videoPath, fileName);
        request1.setDestinationUri(Uri.fromFile(file));

        //***************************************new work for android 10 & Above*******************************************************
        downloadManager1 = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
        final long downloadId = downloadManager1.enqueue(request1);

        new Thread(new Runnable() {
            @SuppressLint("Range")
            @Override
            public void run() {
                downloading = true;

                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId); //filter by id which you have receieved when reqesting download from download manager
                    Cursor cursor = downloadManager1.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        Log.d("MyApp","download complete ");
                        downloading = false;
                    }


                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_RUNNING){
                        long fileSizeBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                        if (fileSizeBytes > 0) {
//                            // Convert bytes to megabytes
                            fileSizeMB = (double) fileSizeBytes / (1024 * 1024);
                            downloadedMB = (double) downloadedBytes / (1024 * 1024);
                            String fileSizeText = String.format("%.2f MB / %.2f MB ", downloadedMB, fileSizeMB);


                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Update the UI with the downloaded size and total size in MB
                                    downloadDialog.showFileSize(fileSizeText);

                                }
                            });
                        }
                    }

                    //when video paused dismiss the dialog
                    if (status == DownloadManager.STATUS_PAUSED){
                        downloading = false;
                        downloadDialog.dismiss();
                        Log.d("MyApp","downloading paused ");
                        break;
                    }

                    // when video failed dismiss the dialog
                    if (status == DownloadManager.STATUS_FAILED){
                        downloading = false;
                        downloadDialog.dismiss();
                        Log.d("MyApp","downloading failed ");
                        break;
                    }

                    ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


                    //  when internet is not connected the dialog dismiss
                    if (networkInfo == null || !networkInfo.isConnected()) {
                        downloading = false;
                        videoDownloaded = false;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadDialog.dismiss();
                                if (downloadCallback != null) {
                                    downloadCallback.onFailure("No internet connection.");
                                }
                            }
                        });
                        break;
                    }

                    if (bytes_total != 0) {
                        dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                        progessValue = dl_progress;
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            downloadDialog.updateVideoDownload(dl_progress + "%");

                            //if (video_download.getText().toString().equals("100%")) {
                            if (dl_progress == 100) {
                                currentVideoIndex++;
                                if (currentVideoIndex < videoAdsList.size()) {
                                    downloadVideo(videoAdsList.get(currentVideoIndex).getVideoUrl(),
                                            videoAdsList.get(currentVideoIndex).getId(), currentVideoIndex, totalVideos);
                                } else {
                                    compareAndDeleteOldVideos();
                                    downloadDialog.dismiss();
                                    downloadCallback.onSuccess();
                                }
                            }
                        }
                    });
                    cursor.close();
                }
            }
        }).start();
    }
}
