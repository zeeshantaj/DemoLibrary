package com.example.demodialog;

public interface DownloadCallback {
    void onSuccess();
    void onFailure(String error);
}
