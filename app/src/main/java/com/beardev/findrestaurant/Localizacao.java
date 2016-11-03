package com.beardev.findrestaurant;

import java.io.Serializable;

/**
 * Created by Jean on 11/06/2016.
 */
public class Localizacao implements Serializable{

    private Double lat;
    private Double lng;
    private String dataHoraRegistro;

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

    public String getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(String dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }

    @Override
    public String toString() {
        return "lat: " + lat + " lng: " + lng;
    }
}
