package com.example.demodialog;

import android.util.Log;

import java.io.File;
import java.util.List;

public class VideoOperations {
    public static boolean createCustomFolder(File file) {
        if (file.isDirectory()) {
            Log.d("Folder Work:", "Path already created");
            return true;
        } else {
            Log.d("Folder Work:", "Path is creating");
            if (file.mkdirs()) {
                Log.d("Folder Work:", "Path created successfully");
                return true;
            } else {
                Log.d("Folder Work:", "Failed to create path");
                return false;
            }
        }
    }

    public static void updateVideosIdentifiers(List<VideosIdentifierListDbModel> oldVideosIdentifierListDbModels, DbHandler dbHandler) {
        if (oldVideosIdentifierListDbModels.size() > 0) {
            for (VideosIdentifierListDbModel vd : oldVideosIdentifierListDbModels) {
                vd.setIsDownloaded(0);
                dbHandler.updateVideosIdentifier(vd);
            }
        } else {
            Log.d("Update video ", "no videos available in db");
        }
    }

    public static void compareAndDeleteOldVideos(File file, List<VideosIdentifierListDbModel> oldVideosIdentifierListDbModels) {
        String[] children = file.list();
        for (int i = 0; i < children.length; i++) {
            for (VideosIdentifierListDbModel uuid : oldVideosIdentifierListDbModels) {
                if (children[i].contains(uuid.getKey())) {
                    if (uuid.getIsDownloaded() == 0) {
                        new File(file, children[i]).delete();
                    }
                }
            }
        }
    }

    public static void deleteCorruptedVideo(File file, String videoId) {
        String[] children = file.list();
        for (int i = 0; i < children.length; i++) {
            if (children[i].contains(videoId)) {
                new File(file, children[i]).delete();
            }
        }
    }



}
