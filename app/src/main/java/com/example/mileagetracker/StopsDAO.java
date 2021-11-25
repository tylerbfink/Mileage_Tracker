package com.example.mileagetracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface StopsDAO {

    @Query("Select * from STOPS")
    List<Stops> loadAllStops();

    @Insert
    void insertStop(Stops stops);

    @Update
    void updateStop(Stops stops);

    @Delete
    void deleteStop(Stops stop);

    @Query("Select * from STOPS where id = :id")
    Stops loadStopByID(int id);

    @Query("Select * from STOPS where dateTime = :dateTime")
    Stops loadStopByDate(Date dateTime);

}
