package com.beardev.findrestaurant.realm;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean on 11/06/2016.
 */
public class PhotoRealm extends RealmObject implements Serializable {

    @PrimaryKey
    private Integer id;
    private Integer restaurant_id;
    private String photo;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getRestaurant_id() {return restaurant_id;}

    public void setRestaurant_id(Integer restaurant_id) {this.restaurant_id = restaurant_id;}

    public String getPhoto() {return photo;}

    public void setPhoto(String photo) {this.photo = photo;}
}
