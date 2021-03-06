package com.beardev.findrestaurant.realm;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean on 11/06/2016.
 */
public class RestaurantRealm extends RealmObject implements Serializable {

    @PrimaryKey
    private Integer id;
    private String companyNmae;
    private String description;
    private String cnpj;
    private String fantasyName;
    private Double latitude;
    private Double longitude;
    private Integer city_id;
    private Integer category_id;
    private Date created_at;
    private Date updated_at;
    private String url;
    private Boolean open_sun;
    private Boolean open_mon;
    private Boolean open_tues;
    private Boolean open_wed;
    private Boolean open_thurs;
    private Boolean open_fri;
    private Boolean open_sat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyNmae() {
        return companyNmae;
    }

    public void setCompanyNmae(String companyNmae) {
        this.companyNmae = companyNmae;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getCnpj() { return cnpj; }

    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getFantasyName() { return fantasyName; }

    public void setFantasyName(String fantasyName) { this.fantasyName = fantasyName; }

    public Double getLatitude() { return latitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }

    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getCity_id() { return city_id; }

    public void setCity_id(Integer city_id) { this.city_id = city_id; }

    public Integer getCategory_id() { return category_id; }

    public void setCategory_id(Integer category_id) { this.category_id = category_id; }

    public Date getCreated_at() { return created_at; }

    public void setCreated_at(Date created_at) { this.created_at = created_at; }

    public Date getUpdated_at() { return updated_at; }

    public void setUpdated_at(Date updated_at) { this.updated_at = updated_at; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public Boolean getOpen_sun() {
        return open_sun;
    }

    public void setOpen_sun(Boolean open_sun) {
        this.open_sun = open_sun;
    }

    public Boolean getOpen_mon() {
        return open_mon;
    }

    public void setOpen_mon(Boolean open_mon) {
        this.open_mon = open_mon;
    }

    public Boolean getOpen_tues() {
        return open_tues;
    }

    public void setOpen_tues(Boolean open_tues) {
        this.open_tues = open_tues;
    }

    public Boolean getOpen_wed() {
        return open_wed;
    }

    public void setOpen_wed(Boolean open_wed) {
        this.open_wed = open_wed;
    }

    public Boolean getOpen_thurs() {
        return open_thurs;
    }

    public void setOpen_thurs(Boolean open_thurs) {
        this.open_thurs = open_thurs;
    }

    public Boolean getOpen_fri() {
        return open_fri;
    }

    public void setOpen_fri(Boolean open_fri) {
        this.open_fri = open_fri;
    }

    public Boolean getOpen_sat() {
        return open_sat;
    }

    public void setOpen_sat(Boolean open_sat) {
        this.open_sat = open_sat;
    }
}
