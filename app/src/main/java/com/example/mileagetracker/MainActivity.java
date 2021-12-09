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
    }
}