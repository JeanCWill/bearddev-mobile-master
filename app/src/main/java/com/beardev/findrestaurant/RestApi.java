package com.beardev.findrestaurant;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jean on 11/06/2016.
 */
public interface RestApi {

    @GET("restaurants_mobile.json")
    Call<JsonArray> listRestaurants();

    @GET("photos_mobile.json")
    Call<JsonArray> listPhotos(@Query("restaurant_id") Integer restaurantId );

    @GET("menus_mobile.json")
    Call<JsonArray> listMenus(@Query("restaurant_id") Integer restaurantId );

    @GET("review_mobiles.json")
    Call<JsonArray> listReviews(@Query("restaurant_id") Integer restaurantId );

    @POST("review_mobiles")
    Call<Integer> sendReview(@Query("place") Integer place, @Query("price") Integer price,
                             @Query("attendance") Integer attendance, @Query("food") Integer food,
                             @Query("descriprion") String descriprion, @Query("restaurant_id") Integer restaurant_id,
                             @Query("name") String name);

/*
    @GET("linhas/{codigoLinha}")
    Call<Localizacao> buscaUltimaLocalizacaoOnibus(@Path("codigoLinha") Integer codigoLinha );

    @POST("pontos")
    Call<Integer> sendReview(@Query("lat") Double lat, @Query("lon") Double lon, @Query("linha_id") Integer id);*/
}
