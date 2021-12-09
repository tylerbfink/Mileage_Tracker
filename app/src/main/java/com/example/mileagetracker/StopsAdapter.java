package com.example.mileagetracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StopsAdapter extends ListAdapter<Stops, StopsAdapter.StopHolder> {

    private OnItemClickListener listener;
    private List<Stops> stops = new ArrayList<>();

    StopsAdapter() {
        super(DIFF_CALLBACK);
    }

    // creating a call back for item of recycler view.
    private static final DiffUtil.ItemCallback<Stops> DIFF_CALLBACK = new DiffUtil.ItemCallback<Stops>() {
        @Override
        public boolean areItemsTheSame(Stops oldItem, Stops newItem) {
            return oldItem.getID() == newItem.getID();
        }

        @Override
        public boolean areContentsTheSame(Stops oldItem, Stops newItem) {
            return oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getStreet().equals(newItem.getStreet()) &&
                    oldItem.getCity().equals(newItem.getCity()) &&
                    oldItem.getStart_odometer() == (newItem.getStart_odometer()) &&
                    oldItem.getEnd_odometer() == (newItem.getEnd_odometer());
        }
    };

    @NonNull
    @Override
    public StopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stops_view, parent, false);
        return new StopHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StopHolder holder, int position) {
        Stops currentStop = stops.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy - hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentStop.getDate());

        Date selectedDate = calendar.getTime();
        holder.stop_date_text.setText(dateFormat.format(selectedDate));

        if (currentStop.getStreet() != null) {
            holder.street_text.setText(currentStop.getStreet());
        }

        if (currentStop.getCity() != null) {
            holder.city_text.setText(currentStop.getCity());
        }

        if (currentStop.getStart_odometer() != 0) {
            holder.start_km_text.setText("Start: " + String.valueOf(currentStop.getStart_odometer()));
        }

        if (currentStop.getEnd_odometer() != 0) {
            holder.end_km_text.setText("End: " + String.valueOf(currentStop.getEnd_odometer()));
        }
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    public void setStops (List<Stops> stops) {
        this.stops = stops;
        notifyDataSetChanged();
    }

    public Stops getStopAtPosition (int position) {
        return stops.get(position);
    }


    class StopHolder extends RecyclerView.ViewHolder {
        TextView stop_date_text, street_text, city_text;
        TextView start_km_text, end_km_text;

        public StopHolder(View view) {
            super(view);
            stop_date_text = view.findViewById(R.id.stop_date_text);
            street_text = view.findViewById(R.id.stop_street_text);
            city_text = view.findViewById(R.id.stop_city_text);
            start_km_text = view.findViewById(R.id.start_km_text);
            end_km_text = view.findViewById(R.id.end_km_text);


            // adding on click listener for each item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(stops.get(position));
                     }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Stops stops);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
