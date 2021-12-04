package com.example.mileagetracker;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS_REQUIRED = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int READ_WRITE_REQUEST_CODE = 10;

    List<Stops> stopList;
    Context context = this;
    TextView saved_text;

    Button switchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //main menu listView
        ListView mainListView = (ListView) findViewById(R.id.main_options_listview);
        Resources res = getResources();
        String[] main_menu_options = res.getStringArray(R.array.main_options_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_view, main_menu_options);
        mainListView.setAdapter(adapter);

        //listener for click of listView item
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, AddStop.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, EditStops.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, Summary.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, Summary.class);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, SendLog.class);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, Settings.class);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, About.class);
                        break;
                }
                startActivity(intent);
                finish();
            }
        });


        //DB_function db_function = new DB_function();
        //db_function.execute();

        if (hasPermission()) {
            //startCamera(); //start camera if permission has been granted by user;
        }
        else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, READ_WRITE_REQUEST_CODE);
        }
    }
/*
    private boolean hasReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION_READ, READ_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private boolean hasWritePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION_WRITE, WRITE_REQUEST_CODE);
            return false;
        }
        return true;
    }

 */

    private boolean hasPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_WRITE_REQUEST_CODE) {
            if (hasPermission()) {
                Toast.makeText(this, "It's all alright now!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Read/Write permission required for app.", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
    }

    //********************************************
    //Database
    //********************************************
    public class DB_function extends AsyncTask<Integer, String, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {

            StopDatabase stopDatabase = StopDatabase.getInstance(context);


            Stops newStop = new Stops (java.util.Calendar.getInstance().getTime());


            //newStop.setId(1);

            newStop.setDateTime(new Date());
            newStop.setStreet("Woodlawn");
            newStop.setCity("Guelph");
            newStop.setStart_odometer(101255);
            newStop.setEnd_odometer(101275);

            //stopDatabase.stopsDAO().updateStop(newStop);

            //stopDatabase.Dao().insertStop(newStop);
            //stopDatabase.stopsDAO().deleteById(4);
            //stopList = stopDatabase.Dao().loadAllStops();

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            saved_text.setText(String.valueOf(stopList.size()));
            //saved_text.setText(String.valueOf(stopList.get(0).getDate()));
        }
    }

}