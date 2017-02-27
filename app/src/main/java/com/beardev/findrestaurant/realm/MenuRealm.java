package com.beardev.findrestaurant.realm;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean on 11/06/2016.
 */
public class MenuRealm extends RealmObject implements Serializable {

    @PrimaryKey
    private Integer id;
    private Integer restaurant_id;
    private String menu;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getRestaurant_id() {return restaurant_id;}

    public void setRestaurant_id(Integer restaurant_id) {this.restaurant_id = restaurant_id;}

    public String getMenu() {return menu;}

    public void setMenu(String menu) {this.menu = menu;}
}
