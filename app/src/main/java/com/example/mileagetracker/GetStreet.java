package com.example.mileagetracker;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetStreet {

    public String returnStreet(Context context) throws IOException {

        String currentStreet = "";

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> currentFullAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
           currentStreet = currentFullAddress.get(0).getThoroughfare();
        }
        catch (NullPointerException e) {
            Toast.makeText(context, "Location not found!", Toast.LENGTH_SHORT).show();
        }

        return currentStreet;
    }
}
