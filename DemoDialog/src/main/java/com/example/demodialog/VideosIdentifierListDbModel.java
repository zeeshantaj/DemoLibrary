package com.example.demodialog;

public class VideosIdentifierListDbModel {
    String key;
    int isDownloaded;

    public void setKey(String key) {
        this.key = key;
    }

    public int getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(int isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public String getKey() {
        return key;
    }

    public void setKeys(String key) {
        this.key = key;
    }

    public VideosIdentifierListDbModel() {
    }

    public VideosIdentifierListDbModel(String key) {
        this.key = key;
    }
}
