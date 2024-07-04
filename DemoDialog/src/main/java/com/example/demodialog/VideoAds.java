package com.example.demodialog;

public class VideoAds {
    int id;
    String videoScreenId;
    String videoUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoScreenId() {
        return videoScreenId;
    }

    public void setVideoScreenId(String videoScreenId) {
        this.videoScreenId = videoScreenId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public VideoAds(int id, String videoScreenId, String videoUrl) {
        this.id = id;
        this.videoScreenId = videoScreenId;
        this.videoUrl = videoUrl;
    }

    public VideoAds() {
    }
}
