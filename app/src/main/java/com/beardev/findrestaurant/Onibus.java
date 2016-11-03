package com.beardev.findrestaurant;

import java.io.Serializable;

/**
 * Created by Jean on 11/06/2016.
 */
public class Onibus implements Serializable{

    private Restaurant restaurant;
    private Double lat;
    private Double lng;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
