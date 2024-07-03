package com.example.demodialog;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    public static void showToast1(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
