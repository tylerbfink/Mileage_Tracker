package com.example.mileagetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class EditStops extends AppCompatActivity {

    StopsViewModel stopsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stops);

        RecyclerView recyclerView = findViewById(R.id.stops_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
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

        //deletes stop on swipe left or swipe right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(viewHolder.itemView.getContext())
                    .setTitle(R.string.confirm_delete_title)
                    .setMessage(R.string.confirm_delete_message)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopsViewModel.delete(adapter.getStopAtPosition(viewHolder.getAdapterPosition()));
                        Toast.makeText(EditStops.this, "Stop deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                })
                    .create()
                    .show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new StopsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Stops stops) {
                Toast.makeText(EditStops.this, "Stop clicked", Toast.LENGTH_SHORT).show();
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