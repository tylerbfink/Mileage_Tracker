package com.example.mileagetracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StopsViewModel extends AndroidViewModel {

    private StopsRepository stopsRepository;
    private LiveData<List<Stops>> allStops;

    public StopsViewModel(@NonNull Application application) {
        super(application);

        stopsRepository = new StopsRepository(application);
        allStops = stopsRepository.getAllStops();
    }

    LiveData<List<Stops>> getAllStops() {
        return allStops;
    }

    public void insert(Stops stops) {
        stopsRepository.insertStop(stops);
    }

    public void update(Stops stops) {
        stopsRepository.updateStop(stops);
    }

    public void delete(Stops stops) {
        stopsRepository.deleteStop(stops);
    }


}
