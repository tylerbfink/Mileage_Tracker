package com.example.mileagetracker;


import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

//db functions
@androidx.room.Dao
public interface StopsDAO {

    @Query("Select * from STOPS")
    LiveData<List<Stops>> loadAllStops();

    @Insert
    void insertStop(Stops stops);

    @Update
    void updateStop(Stops stops);

    @Delete
    void deleteStop(Stops stops);

    @Query("Select * from STOPS where dateTime = :dateTime")
    Stops loadStopByDate(Date dateTime);
}
