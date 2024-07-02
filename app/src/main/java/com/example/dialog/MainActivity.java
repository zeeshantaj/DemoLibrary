package com.example.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.demodialog.ShowDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShowDialog.showToast(this,"haha");
    }
}