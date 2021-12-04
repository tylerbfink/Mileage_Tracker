package com.example.mileagetracker;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import androidx.room.TypeConverters;

@Database(entities = {Stops.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class StopDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "stoplist";

    private static StopDatabase sInstance;
    public abstract StopsDAO stopsDAO();

    //instance of stopsDB
    public static synchronized StopDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), StopDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(stopDbCallback)
                    .build();
        }
        return sInstance;
    }

    //callback for stopsDB
    private static RoomDatabase.Callback stopDbCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new dbAsyncTask(sInstance).execute();
        }
    };

    //async task to complete DB calls
    private static class dbAsyncTask extends AsyncTask<Void, Void, Void> {
        dbAsyncTask(StopDatabase instance) {
            StopsDAO stopsDAO = instance.stopsDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
