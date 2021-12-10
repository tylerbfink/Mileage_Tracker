package com.example.mileagetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Favourites extends AppCompatActivity {

    private final String SAVED_FAVOURITES = "SavedFavourites.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        //recyclerView to see all saved favourites
        RecyclerView recyclerView = findViewById(R.id.favourites_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        final FavouritesAdapter adapter = new FavouritesAdapter(this);
        recyclerView.setAdapter(adapter);

        //touch actions of recyclerView items
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            //action on swipe of favourite (delete)
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setTitle(R.string.confirm_delete_favourite_title)
                        .setMessage(R.string.confirm_delete_favourite_message)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FavouritesAdapter.deleteFavouriteAtPosition((viewHolder.getAdapterPosition()));
                                try {
                                    saveFavouriteArray();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                Toast.makeText(getBaseContext(), "Favourite deleted", Toast.LENGTH_SHORT).show();

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

        //listener to use pressed favourite in addStop activity
        adapter.setOnItemClickListener(new FavouritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Favourite favourite) {
                modifyShared(getBaseContext());
                Intent intent = new Intent(Favourites.this, AddStop.class);
                intent.putExtra("STREET", favourite.getStreet());
                intent.putExtra("CITY", favourite.getCity());
                startActivity(intent);
                finish();
            }
        });
    }

    //writes favourite array to file
    private void saveFavouriteArray() throws IOException {
        if (fileExists(SAVED_FAVOURITES)) {

            try {
                FileOutputStream fos = openFileOutput(SAVED_FAVOURITES, MODE_PRIVATE);

                for (int index = 0; index < FavouritesAdapter.favourites.size(); index++) {
                    String tempFavourite = FavouritesAdapter.favourites.get(index).getStreet() + "," +
                            FavouritesAdapter.favourites.get(index).getCity() + "\n";

                    fos.write(tempFavourite.getBytes());
                }
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //checks if file exists & creates if does not
    private boolean fileExists (String fileName) throws IOException {
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Favourites.this, AddStop.class);
        startActivity(intent);
        finish();
    }

    //saves selected street/city combo to sharedpreferences with flga
    //to notify addStop to load
    private void modifyShared(Context context) {
        SharedPreferences formData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor formDataEditor = formData.edit();

        formDataEditor.putString("streetKeyFlag", "true");
        formDataEditor.putString("cityKeyFlag", "true");
        formDataEditor.commit();
    }
}