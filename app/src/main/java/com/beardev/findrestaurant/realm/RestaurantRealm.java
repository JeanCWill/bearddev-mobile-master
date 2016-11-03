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
}
