package com.example.mileagetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.StopHolder> {

    private List<Stops> stops = new ArrayList<>();

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

        holder.stop_date_text.setText(String.valueOf(currentStop.getDate()));
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

        }
    }
}
