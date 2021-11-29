package com.example.mileagetracker;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Stops.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class StopDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "stoplist";
    private static StopDatabase sInstance;

    public static synchronized StopDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), StopDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return sInstance;
    }

    public abstract StopsDAO stopsDAO();
}
