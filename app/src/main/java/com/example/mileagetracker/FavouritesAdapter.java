package com.example.mileagetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouritesAdapter extends ListAdapter<Favourite, FavouritesAdapter.FavouriteHolder> {

    private FavouritesAdapter.OnItemClickListener listener;
    public static List<Favourite> favourites;

    FavouritesAdapter(Context context) {
        super(DIFF_CALLBACK);
        ReadFavourites readFavourites = new ReadFavourites();
        favourites = readFavourites.readFile(context);
        Collections.reverse(favourites);
    }

    // creating a call back for item of recycler view.
    private static final DiffUtil.ItemCallback<Favourite> DIFF_CALLBACK = new DiffUtil.ItemCallback<Favourite>() {
        @Override
        public boolean areItemsTheSame(Favourite oldItem, Favourite newItem) {
            return oldItem.getStreet().equals(newItem.getStreet());
        }

        @Override
        public boolean areContentsTheSame(Favourite oldItem, Favourite newItem) {
            return oldItem.getCity().equals(newItem.getCity());
        }
    };

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_view, parent, false);
        return new FavouriteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {
        Favourite favourite = favourites.get(position);

        holder.street_text.setText(favourite.getStreet());

        if (favourite.getCity().length() != 0) {
            holder.city_text.setText(favourite.getCity());
        }
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    class FavouriteHolder extends RecyclerView.ViewHolder {
        TextView street_text, city_text;

        public FavouriteHolder(View view) {
            super(view);
            street_text = view.findViewById(R.id.street_text);
            city_text = view.findViewById(R.id.city_text);


            // adding on click listener for each item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(favourites.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Favourite favourite);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static void deleteFavouriteAtPosition(int position) {
        FavouritesAdapter.favourites.remove(position);
    }
}