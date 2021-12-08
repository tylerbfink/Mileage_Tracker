package com.example.mileagetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class EditStops extends AppCompatActivity {

    StopsViewModel stopsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stops);

        RecyclerView recyclerView = findViewById(R.id.stops_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        final StopsAdapter adapter = new StopsAdapter();
        recyclerView.setAdapter(adapter);



        stopsViewModel = ViewModelProviders.of(this).get(StopsViewModel.class);
        stopsViewModel.getAllStops().observe(this, new Observer<List<Stops>>() {
            @Override
            public void onChanged(List<Stops> stops) {
                adapter.setStops(stops);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditStops.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}