package com.example.demodialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DownloadDialog extends Dialog {
    private TextView videoDownload;
    private TextView totalVideos;
    private TextView currentVideos;

    public DownloadDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.download_dialog); // Replace with your actual layout

        videoDownload = findViewById(R.id.videoDownload);
        totalVideos = findViewById(R.id.textView6);
        currentVideos = findViewById(R.id.textView15);

        // disable dialog outside touch
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.TOP | Gravity.RIGHT;
            params.x = 0; // Adjust these values as needed
            params.y = 0; // Adjust these values as needed
            window.setAttributes(params);
        }
    }

    public void updateVideoDownload(String text) {
        videoDownload.setText(text);
    }

    public void updateTotalVideos(String text) {
        totalVideos.setText(text);
    }

    public void updateCurrentVideos(String text) {
        currentVideos.setText(text);
    }
}
