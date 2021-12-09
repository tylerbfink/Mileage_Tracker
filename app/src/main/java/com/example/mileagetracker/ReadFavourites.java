package com.example.mileagetracker;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReadFavourites {

    List<Favourite> favouriteStopList = Collections.synchronizedList(new ArrayList<Favourite>());
    private final String SAVED_FAVOURITES = "SavedFavourites.txt";

    public List<Favourite> readFile(Context context) {

        if (fileExists(context, SAVED_FAVOURITES)) {
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(SAVED_FAVOURITES);

            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String sLine = null;

            try {
                while ((sLine = br.readLine()) != null) {
                    ArrayList<String> tempArray = new ArrayList<String>(Arrays.asList(sLine.split(",")));
                    Favourite favourite = new Favourite(tempArray.get(0));
                    if (tempArray.get(1).length() != 0) {
                        favourite.setCity(tempArray.get(1));
                    }

                    favouriteStopList.add(favourite);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

        return favouriteStopList;
    }

    //checks if file exists
    public boolean fileExists (Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }
}
