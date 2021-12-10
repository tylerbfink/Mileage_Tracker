package com.example.mileagetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//returns device street and city if available
public class GetStreetCity {

    public static String gpsCity = "";
    public static String gpsStreet = "";

    private static List<Address> currentFullAddress;

    public static void getPosition(Context context) throws IOException {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            currentFullAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            gpsStreet = currentFullAddress.get(0).getThoroughfare();
            gpsCity = currentFullAddress.get(0).getLocality();
        }
        catch (NullPointerException e) {
        }
    }
}
