package com.example.mileagetracker;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "stops")

public class Stops {

    @PrimaryKey(autoGenerate = true)
    Integer id;

    Date dateTime;
    String street;
    String city;
    float start_odometer;
    float end_odometer;

    public Stops(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getStart_odometer() {
        return start_odometer;
    }

    public void setStart_odometer(float start_odometer) {
        this.start_odometer = start_odometer;
    }

    public float getEnd_odometer() {
        return end_odometer;
    }

    public void setEnd_odometer(float end_odometer) {
        this.end_odometer = end_odometer;
    }

}
