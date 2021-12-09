package com.example.mileagetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class Summary extends AppCompatActivity {

    StopsViewModel stopsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        RecyclerView recyclerView = findViewById(R.id.stops_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        final StopsAdapter adapter = new StopsAdapter();
        recyclerView.setAdapter(adapter);

        stopsViewModel = new ViewModelProvider(this).get(StopsViewModel.class);
        stopsViewModel.getAllStops().observe(this, new Observer<List<Stops>>() {
            @Override
            public void onChanged(List<Stops> stops) {
                adapter.setStops(stops);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Summary.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}