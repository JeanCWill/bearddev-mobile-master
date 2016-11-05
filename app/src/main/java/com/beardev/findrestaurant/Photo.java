package com.beardev.findrestaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jean on 11/06/2016.
 */
public class Photo {

    private Integer id;
    private Integer restaurant_id;
    private String photo;

    public Photo() {
    }

    public Photo(int id, Integer restaurant_id, String photo) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurant_id() { return restaurant_id; }

    public void setRestaurant_id(Integer restaurant_id) { this.restaurant_id = restaurant_id; }

    public String getPhoto() { return photo; }

    public void setPhoto(String photo) { this.photo = photo; }
}
