package com.example.mileagetracker;

public class Favourite {

    String street;
    String city = "";

    public Favourite(String street) {
        this.street = street;
    }

    public Favourite(String street, String city) {
        this.street = street;
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}

