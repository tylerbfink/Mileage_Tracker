package com.example.mileagetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Stops> stopList;
    Context context = this;
    TextView saved_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saved_text = (TextView) findViewById(R.id.new_text);

        DB_function db_function = new DB_function();
        db_function.execute();


    }



    public class DB_function extends AsyncTask<Integer, String, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {

            StopDatabase stopDatabase = StopDatabase.getInstance(context);
            Stops newStop = new Stops (java.util.Calendar.getInstance().getTime());
            stopDatabase.stopsDAO().insertStop(newStop);

            stopList = stopDatabase.stopsDAO().loadAllStops();

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            saved_text.setText(stopList.toString());
        }
    }
}