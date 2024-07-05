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
import android.net.Uri;
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
    boolean downloading = true;
    DownloadDialog downloadDialog;
    DownloadCallback downloadCallback;

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
        //downloadLayout.setVisibility(View.VISIBLE);
        downloadDialog = new DownloadDialog(activity,SCREEN_TYPE);

        // to get round bg of dialog
        downloadDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //to remove the dark bg of dialog
        downloadDialog.getWindow().setDimAmount(0);
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

                        downloading = false;
                    }

                    ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


                    // todo when internet is not connected the dialog dismiss
                    if (networkInfo == null || !networkInfo.isConnected()) {
                        downloading = false;
                        videoDownloaded = false;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadDialog.dismiss();
                             //   Toast.makeText(activity, "No internet connection. Download canceled.", Toast.LENGTH_SHORT).show();
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
//                            if (dl_progress == 0) {
//                                video_download.setText(progessValue + "%");
//                            } else {
//                                video_download.setText(dl_progress + "%");
//                            }

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
//    public BroadcastReceiver networkConnection = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            try {
//                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                if (connectivityManager != null) {
//                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//                    if (networkInfo != null) {
//                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
//                            AppConstants.intAv = true;
//                            Log.d("usama lis", "onNetworkConnectionChanged 111: connected screen ");
////                            initializeMenuSocket();
////                            socketConnectOnInternet();
////                            initializeSocket();
////                            connectedShowProducts();
//                            if (!videoDownloaded) {
//                                videoDownloaded = true;
//                            }
//                        } else {
////                            intConn.socket.close();
//                            AppConstants.intAv = false;
//                            downloadLayout.setVisibility(View.GONE);
//                            Log.d("usama lis", "Internet :  not connected");
////                            setOffLineSetup();
//                        }
//                    } else {
////                        intConn.socket.close();
//                        AppConstants.intAv = false;
//                        downloadLayout.setVisibility(View.GONE);
//                        Log.d("usama lis", "Internet :  not connected");
////                        setOffLineSetup();
//                    }
//                }
//            } catch (Exception ex) {
//                ex.getMessage();
//            }
//        }
//    };
}
