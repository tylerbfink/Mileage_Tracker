package com.example.mileagetracker;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

//repository to communicate with db via async tasks
public class StopsRepository {

    private StopsDAO stopDAO;
    private LiveData<List<Stops>> allStops;

    public StopsRepository(Application application) {
        StopDatabase stopDatabase = StopDatabase.getInstance(application);
        stopDAO = stopDatabase.stopsDAO();
        allStops = stopDAO.loadAllStops();
    }

    //read all stops
    public LiveData<List<Stops>> getAllStops() {
        return allStops;
    }

    //inserts new stop
    public void insertStop(Stops stops) {
        new InsertStopAsync(stopDAO).execute(stops);
    }

    //updates stop
    public void updateStop(Stops stops) {
        new UpdateStopAsync(stopDAO).execute(stops);
    }

    //deletes stop
    public void deleteStop(Stops stops) {
        new DeleteStopAsync(stopDAO).execute(stops);
    }

    //loads stop by date -- feature not available due time
    public void loadStopByDate(Stops stops) {
        new LoadStopByDateAsync(stopDAO).execute();
    }

    //async to insert new stop
    private static class InsertStopAsync extends AsyncTask<Stops, Void, Void> {
        private StopsDAO stopsDAO;

        private InsertStopAsync(StopsDAO stopsDAO) {
            this.stopsDAO = stopsDAO;
        }
            @Override
            protected Void doInBackground(Stops... stops) {
                stopsDAO.insertStop(stops[0]);
                return null;
            }
    }

    //async to update stop
    private static class UpdateStopAsync extends AsyncTask<Stops, Void, Void> {
        private StopsDAO stopsDAO;

        private UpdateStopAsync(StopsDAO stopsDAO) {
            this.stopsDAO = stopsDAO;
        }

        @Override
        protected Void doInBackground(Stops... stops) {
            stopsDAO.updateStop(stops[0]);
            return null;
        }
    }

    //async to delete stop
    private static class DeleteStopAsync extends AsyncTask<Stops, Void, Void> {
        private StopsDAO stopsDAO;

        private DeleteStopAsync(StopsDAO stopsDAO) {
            this.stopsDAO = stopsDAO;
        }

        @Override
        protected Void doInBackground(Stops... stops) {
            stopsDAO.deleteStop(stops[0]);
            return null;
        }
    }

    //async to return stops between dates - not complete/implemented
    private static class LoadStopByDateAsync extends AsyncTask<Date, Void, Void> {
        private StopsDAO stopsDAO;
        private LoadStopByDateAsync(StopsDAO stopsDAO) {
            this.stopsDAO = stopsDAO;
        }
        @Override
        protected Void doInBackground(Date... stopDate) {
            stopsDAO.loadStopByDate(stopDate[0]);
            return null;
        }
    }
}