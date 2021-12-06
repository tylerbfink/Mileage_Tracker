package com.example.mileagetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class EditStops extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stops);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditStops.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}