package com.example.mileagetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class AddStop extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static final String[] PERMISSIONS_REQUIRED = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST_CODE = 10;

    private final String SAVED_FAVOURITES = "SavedFavourites.txt";

    TextInputEditText street_edittext, city_edittext;
    TextInputEditText date_edittext, starting_km_edittext, ending_km_edittext;
    Button add_favourite_button, use_favourite_button;
    Button back_button, save_stop_button, open_camera;

    Date selectedDate;
    StopsViewModel stopsViewModel;

    List<Favourite> favouriteStopList = Collections.synchronizedList(new ArrayList<Favourite>());
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop);

        stopsViewModel = new ViewModelProvider(this).get(StopsViewModel.class);
        stopsViewModel.getAllStops().observe(this, new Observer<List<Stops>>() {
            @Override
            public void onChanged(List<Stops> stops) {
            }
        });

        street_edittext = findViewById(R.id.street_edittext);
        city_edittext = findViewById(R.id.city_edittext);
        date_edittext = findViewById(R.id.date_edittext);
        starting_km_edittext = findViewById(R.id.km_start_edittext);
        ending_km_edittext = findViewById(R.id.km_end_edittext);

        save_stop_button = findViewById(R.id.save_stop_button);
        back_button = findViewById(R.id.back_button);
        open_camera = findViewById(R.id.open_camera_button);
        add_favourite_button = findViewById(R.id.add_favourite_button);
        use_favourite_button = findViewById(R.id.use_favourite_button);

        add_favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favourite tempFavourite;
                String addStreet = "";
                String addCity = "";

                if (street_edittext.getText().length() != 0) {
                    addStreet = street_edittext.getText().toString();
                    if (city_edittext.getText().length() != 0) {
                        addCity = city_edittext.getText().toString();
                        tempFavourite = new Favourite(addStreet, addCity);
                    }
                    else {
                        tempFavourite = new Favourite(addStreet);

                    }
                    favouriteStopList.add(tempFavourite);

                    try {
                        saveFavourite(tempFavourite);
                        //Toast.makeText(AddStop.this, "Favourite saved! ", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(AddStop.this, "Street required to save! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        use_favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(AddStop.this, Favourites.class);
                startActivity(intent);
                finish();
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy - hh:mm a");
        Calendar calendar = Calendar.getInstance();
        selectedDate = calendar.getTime();
        String selectedDateString = dateFormat.format(selectedDate);
        date_edittext.setText(selectedDateString);

        //onclick listener to open camera activity for OCR
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddStop.this, CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //onclick listener to bring up date pick when date edittext clicked
        date_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePickerDialog;
                datePickerDialog = new DatePicker();
                datePickerDialog.show(getSupportFragmentManager(), "PICK DATE");
            }
        });

        //onclick to go back
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //onclick listener to save the new stop
        save_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date stopDate = null;
                Float stopStartKM = null, stopEndKM = null;

                String stopStreet = street_edittext.getText().toString();
                String stopCity = city_edittext.getText().toString();

                if (selectedDate != null) {
                    stopDate = selectedDate;
                    if (!starting_km_edittext.getText().toString().equals("")) {
                        stopStartKM = Float.valueOf(starting_km_edittext.getText().toString());
                    }
                    if (!ending_km_edittext.getText().toString().equals("")) {
                        stopEndKM = Float.valueOf(ending_km_edittext.getText().toString());
                    }
                    saveStop(stopStreet, stopCity, stopDate, stopStartKM, stopEndKM);
                }
                else {
                    dateRequired();
                }
            }
        });

        // checks if location permissions have been given or asks for them
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, LOCATION_REQUEST_CODE);
        }

        Context context = this;
        try {
            GetStreetCity.getPosition(context);
            if (!GetStreetCity.gpsStreet.equals("")) {
                street_edittext.setText(GetStreetCity.gpsStreet);
            }
            if (!GetStreetCity.gpsCity.equals("")) {
                city_edittext.setText(GetStreetCity.gpsCity);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //TextInputEditText city_edittext = findViewById(R.id.input_edit_text);
        //city_edittext.setText("Garbage...");
    }

    //toasts if data missing when save button pressed
    public void dateRequired() {
        Toast.makeText(this, "Date required to save!", Toast.LENGTH_SHORT).show();
    }

    // returns true if location permissions already granted
    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // requests location permissions if not already granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (hasPermission()) {
                // placeholder to continue
            } else {
                Toast.makeText(this, "Permission required for current location.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddStop.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy - hh:mm a");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        selectedDate = calendar.getTime();

        String selectedDateString = dateFormat.format(selectedDate);
        date_edittext.setText(selectedDateString);
    }

    private void saveStop(String stopStreet, String stopCity,
                          Date date, Float km_start, Float km_end) {

        Stops newStop = new Stops(date);

        if (!stopStreet.equals("")) {
            newStop.setStreet(stopStreet);
        }
        if (!stopCity.equals("")) {
            newStop.setCity(stopCity);
        }
        if (km_start != null) {
            newStop.setStart_odometer(km_start);
        }
        if (km_end != null) {
            newStop.setEnd_odometer(km_end);
        }

        stopsViewModel.insert(newStop);
        Toast.makeText(this, "Stop saved... ", Toast.LENGTH_SHORT).show();
    }


    //writes current favourite to file
    private void saveFavourite(Favourite favourite) throws IOException {
        if (fileExists(SAVED_FAVOURITES)) {

            try {
                FileOutputStream fos = openFileOutput(SAVED_FAVOURITES, MODE_PRIVATE | MODE_APPEND);
                String tempFavourite = favourite.getStreet() + "," +
                        favourite.getCity() + "\n";

                fos.write(tempFavourite.getBytes());
                fos.close();

                Toast.makeText(this, "Favourite saved! ", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //checks if file exists & creates if does not
    private boolean fileExists (String fileName) throws IOException {
        File file = getBaseContext().getFileStreamPath(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        return file.exists();
    }

}