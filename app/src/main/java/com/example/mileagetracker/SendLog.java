package com.example.mileagetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SendLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_log);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SendLog.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}