package com.example.mileagetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditStop extends AppCompatActivity implements Serializable {

    Stops stopToEdit;
    Stops tempStop = new Stops(new Date());

    TextInputEditText edit_street_edittext, edit_city_edittext;
    TextInputEditText edit_date_edittext, edit_starting_km_edittext, edit_ending_km_edittext;
    Button edit_back_button, edit_save_stop_button;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy - hh:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stop);

        //retrieves stop to edit from intent
        if(getIntent().getExtras() != null) {
            stopToEdit = (Stops) getIntent().getSerializableExtra("stopToEdit");
        }

        edit_street_edittext = findViewById(R.id.edit_street_edittext);
        edit_city_edittext = findViewById(R.id.edit_city_edittext);
        edit_date_edittext = findViewById(R.id.edit_date_edittext);
        edit_starting_km_edittext = findViewById(R.id.edit_km_start_edittext);
        edit_ending_km_edittext = findViewById(R.id.edit_km_end_edittext);

        edit_save_stop_button = findViewById(R.id.edit_save_button);
        edit_back_button = findViewById(R.id.edit_go_back_button);

        //loads data from stop into edittexts where able
        if (!stopToEdit.getStreet().equals("")) {
            edit_street_edittext.setText(stopToEdit.getStreet());
        }
        if (!stopToEdit.getCity().equals("")) {
            edit_city_edittext.setText(stopToEdit.getCity());
        }

        String selectedDateString = dateFormat.format(stopToEdit.getDate());
        edit_date_edittext.setText(selectedDateString);

        if (stopToEdit.getStart_odometer() != 0.0) {
            edit_starting_km_edittext.setText(String.valueOf(stopToEdit.getStart_odometer()));
        }
        if (stopToEdit.getEnd_odometer() != 0.0) {
            edit_ending_km_edittext.setText(String.valueOf(stopToEdit.getEnd_odometer()));
        }

        //communication with db
        StopsViewModel stopsViewModel = new ViewModelProvider(this).get(StopsViewModel.class);

        //listener to save edited stop
        edit_save_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempStop.setID(stopToEdit.getID());

                tempStop.setStreet(edit_street_edittext.getText().toString());
                tempStop.setCity(edit_city_edittext.getText().toString());

                try {
                    tempStop.setDateTime(dateFormat.parse(edit_date_edittext.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!edit_starting_km_edittext.getText().toString().equals("")) {
                    tempStop.setStart_odometer(Float.valueOf(edit_starting_km_edittext.getText().toString()));
                }
                if (!edit_ending_km_edittext.getText().toString().equals("")) {
                    tempStop.setEnd_odometer(Float.valueOf(edit_ending_km_edittext.getText().toString()));
                }

                stopsViewModel.update(tempStop);

                //after save returns to EditStops activity
                Intent intent = new Intent(EditStop.this, EditStops.class);
                startActivity(intent);
                finish();
            }
        });

        //listener to go back without saving edit
        edit_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditStop.this, EditStops.class);
        startActivity(intent);
        finish();
    }
}