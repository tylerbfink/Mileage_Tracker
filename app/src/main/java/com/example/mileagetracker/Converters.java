package com.example.mileagetracker;

import androidx.room.TypeConverter;

import java.util.Date;

//converts date <--> long for saving in db
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
