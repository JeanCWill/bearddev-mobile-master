package com.beardev.findrestaurant;

import java.util.Date;

/**
 * Created by Jean on 11/06/2016.
 */
public class Review {

    private Integer id;
    private Integer restaurant_id;
    private Integer place;
    private Integer price;
    private Integer attendance;
    private Integer food;
    private String descriprion;
    private Date created_at;
    private String name;

    public Review() {
    }

    public Review(Integer id, Integer restaurant_id, Integer place, Integer price, Integer attendance,
                  Integer food, String descriprion, Date created_at, String name) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.place = place;
        this.price = price;
        this.attendance = attendance;
        this.food = food;
        this.descriprion = descriprion;
        this.created_at = created_at;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAttendance() {
        return attendance;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }

    public Integer getFood() {
        return food;
    }

    public void setFood(Integer food) {
        this.food = food;
    }

    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
