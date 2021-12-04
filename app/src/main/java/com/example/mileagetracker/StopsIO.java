package com.example.mileagetracker;

import android.app.Activity;
import androidx.room.Room;

public class StopsIO {
    Activity context;

    StopDatabase stopDatabase = StopDatabase.getInstance(context);

    //StopDatabase stopDatabase = Room.databaseBuilder(context.getApplicationContext(), StopDatabase.class, "stoplist").build();

    Stops newStop = new Stops (java.util.Calendar.getInstance().getTime());

    //newStop.setId(1);

    newStop.setDateTime(new Date());
    newStop.setStreet("Woodlawn");
    newStop.setCity("Guelph");
    newStop.setStart_odometer(101255);
    newStop.setEnd_odometer(101275);

    //stopDatabase.stopsDAO().updateStop(newStop);

    stopDatabase.stopsDAO().insertStop(newStop);
    //stopDatabase.stopsDAO().deleteById(4);
    stopList = stopDatabase.stopsDAO().loadAllStops();
}
