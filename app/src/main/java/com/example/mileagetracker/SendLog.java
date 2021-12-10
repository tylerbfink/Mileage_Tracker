package com.example.mileagetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SendLog extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextInputEditText start_date_edittext, end_date_edittext;
    Button send_current_button, send_custom_button;
    Date selectedStartDate, selectedEndDate;
    Calendar calendarStart, calendarEnd;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");

    boolean startFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_log);

        start_date_edittext = findViewById(R.id.start_date_edittext);
        end_date_edittext = findViewById(R.id.end_date_edittext);

        send_current_button = findViewById(R.id.send_current_button);
        send_custom_button = findViewById(R.id.send_custom_button);

        //listener to send current week (Saturday - Friday) stops by email
        send_current_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //listener to get start date
        start_date_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFlag = true;
                DatePicker datePickerDialog;
                datePickerDialog = new DatePicker();
                datePickerDialog.show(getSupportFragmentManager(), "PICK DATE");
            }
        });

        //listener to get end date
        end_date_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFlag = false;
                DatePicker datePickerDialog;
                datePickerDialog = new DatePicker();
                datePickerDialog.show(getSupportFragmentManager(), "PICK DATE");
            }
        });

        //listener to send custom dates log by email
        send_custom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStartDate.compareTo(selectedEndDate) < 0) {

                }
                else {
                    Toast.makeText(getBaseContext(), "End date must be after start date! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();

        selectedStartDate = calendarStart.getTime();
        selectedEndDate = calendarEnd.getTime();

        String selectedStartDateString = dateFormat.format(selectedStartDate);
        String selectedEndDateString = dateFormat.format(selectedEndDate);

        start_date_edittext.setText(selectedStartDateString);
        end_date_edittext.setText(selectedEndDateString);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SendLog.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //sets date in variables after picking
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        if (startFlag = true) {
            calendarStart = Calendar.getInstance();

            calendarStart.set(Calendar.YEAR, year);
            calendarStart.set(Calendar.MONTH, month);
            calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            selectedStartDate = calendarStart.getTime();

            String selectedDateString = dateFormat.format(selectedStartDate);
            start_date_edittext.setText(selectedDateString);
        }
        else {
            calendarEnd = Calendar.getInstance();

            calendarEnd.set(Calendar.YEAR, year);
            calendarEnd.set(Calendar.MONTH, month);
            calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            selectedEndDate = calendarEnd.getTime();

            String selectedDateString = dateFormat.format(selectedEndDate);
            end_date_edittext.setText(selectedDateString);
        }
    }
}