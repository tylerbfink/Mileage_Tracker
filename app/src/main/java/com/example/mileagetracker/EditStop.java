package com.example.mileagetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

public class EditStop extends AppCompatActivity implements Serializable {

    Stops stopToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stop);

        if(getIntent().getExtras() != null) {
            stopToEdit = (Stops) getIntent().getSerializableExtra("stopToEdit");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditStop.this, EditStops.class);
        intent.putExtra("stopToEdit", stopToEdit);
    }
}