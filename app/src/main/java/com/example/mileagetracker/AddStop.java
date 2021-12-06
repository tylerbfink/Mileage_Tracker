package com.example.mileagetracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddStop extends AppCompatActivity {

    private static final String[] PERMISSIONS_REQUIRED = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST_CODE = 10;

    TextView current_street;

    GetStreet getStreet = new GetStreet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop);

        // checks if location permissions have been given or asks for them
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, LOCATION_REQUEST_CODE);
        }

        current_street = (TextView) findViewById(R.id.current_street);

        try {
            current_street.setText(getStreet.returnStreet(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                Toast.makeText(this, "Location permission required for this feature!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getStreet() throws IOException {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List <Address> currentFullAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            current_street.setText(String.valueOf(currentFullAddress.get(0).getThoroughfare()));
        }
        catch (NullPointerException e) {
            Toast.makeText(this, "Location not found!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddStop.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}