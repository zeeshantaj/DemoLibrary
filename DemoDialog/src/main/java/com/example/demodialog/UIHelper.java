package com.example.demodialog;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UIHelper {
    public static void showErrorDialog(Context context, String title, String content, int type) {
        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }
}
