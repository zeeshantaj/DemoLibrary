package com.example.demodialog;

import android.content.Context;
import android.widget.Toast;

public class ShowDialog {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
